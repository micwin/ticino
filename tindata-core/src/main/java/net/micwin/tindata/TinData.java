package net.micwin.tindata;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * The TinData core class. To register a receiver, call <code>register</code>
 * with an event class and a receiver providing an accessible public method that
 * has the event class as sole parameter:
 * 
 * <pre>
 * TinData.register(MyEvent.class, new Object() {
 * 	public void myReceiverMethod(MyEvent evt) {
 * 		// do something meaningful
 * 	}
 * });
 * </pre>
 * 
 * @author MicWin
 * 
 */
public class TinData {

	/**
	 * Holds a weak reference to a receiver. This will not prevent the receiver
	 * to get garbage collected, so make sure to store the receiver elsewhere,
	 * for example you could pass in a spring bean.
	 * 
	 * @author MicWin
	 * 
	 */
	private static class ReceiverDescriptor {
		WeakReference<Object> receiverReference;
		Method method;
		long creationTime = System.currentTimeMillis();
	}

	static Map<Class<?>, List<ReceiverDescriptor>> receiverMap = new HashMap<Class<?>, List<ReceiverDescriptor>>();

	/**
	 * The public method to register a new event receiver.
	 * 
	 * @param eventClass
	 *            the class of event to be received.
	 * @param receiver
	 *            The receiver to get the event.
	 */
	public synchronized static void register(Class<?> eventClass,
			Object receiver) {
		Method method = detectReceiverMethod(receiver, eventClass);
		registerInternal(receiver, method, eventClass);
	}

	/**
	 * Registers a receiver to an internal map.
	 * 
	 * @param receiver
	 * @param method
	 * @param eventClass
	 */
	private static void registerInternal(Object receiver, Method method,
			Class<?> eventClass) {
		List<ReceiverDescriptor> receivers = receiverMap.get(eventClass);
		if (receivers == null) {
			receivers = new LinkedList<ReceiverDescriptor>();
			receiverMap.put(eventClass, receivers);
		}

		ReceiverDescriptor rd = new ReceiverDescriptor();
		rd.receiverReference = new WeakReference<Object>(receiver);
		rd.method = method;

		receivers.add(rd);
	}

	/**
	 * Analyzes the receiver for Methods that satisfy to receive events of type
	 * <code>eventClass</code>.
	 * 
	 * @param receiver
	 * @param eventClass
	 * @return
	 */
	private static Method detectReceiverMethod(Object receiver,
			Class<?> eventClass) {
		Method[] methods = receiver.getClass().getMethods();
		List<Method> results = new LinkedList<Method>();

		for (Method method : methods) {

			if (method.getParameterTypes() == null
					|| method.getParameterTypes().length != 1) {
				// wrong amount of parameters -> try next
				continue;
			}

			if (method.getParameterTypes()[0].isAssignableFrom(eventClass)
					&& !"equals".equals(method.getName())) {
				// hit!
				results.add(method);
			}
		}

		if (results.size() == 1) {
			// only one hit - perfect
			return results.get(0);
		}

		if (results.size() > 1) {
			throw new IllegalArgumentException(
					"receiver '"
							+ receiver
							+ "' has more that one accessible method with a single parameter of type '"
							+ eventClass.getName() + "' : "
							+ results.toString());
		}

		// no hit _> exception
		throw new IllegalArgumentException(
				"receiver '"
						+ receiver
						+ "' does not have an accessible method with a single parameter of type '"
						+ eventClass.getName() + "'");

	}

	/**
	 * Dispatch an event to receivers.
	 * 
	 * @param event
	 *            The event object to dispatch.
	 */
	public static synchronized void dispatch(Object event) {

		Collection<ReceiverDescriptor> receivers = new TreeSet<TinData.ReceiverDescriptor>(
				new Comparator<ReceiverDescriptor>() {

					@Override
					public int compare(ReceiverDescriptor o1,
							ReceiverDescriptor o2) {
						if (o1 == o2
								|| o1.receiverReference == o2.receiverReference) {
							return 0;
						}

						if (o1.creationTime < o2.creationTime) {
							return -1;
						}
						return 1;
					}
				});

		collectReceiver(event.getClass(), receivers);

		// no receivers registered, so bye-bye
		if (receivers == null || receivers.size() < 1) {
			return;
		}

		// when finding a garbage collected receiver, store here
		List<ReceiverDescriptor> defunct = new LinkedList<ReceiverDescriptor>();

		try {
			for (ReceiverDescriptor receiverDescriptor : receivers) {
				Object receiver = receiverDescriptor.receiverReference.get();
				if (receiver == null) {

					defunct.add(receiverDescriptor);
					continue;
				}
				try {
					receiverDescriptor.method.setAccessible(true);
					receiverDescriptor.method.invoke(receiver, event);
				} catch (Exception e) {
					// wrap non runtime exception
					throw new DispatchException(receiver, event, e);
				}
			}
		} finally {
			// throw away gc'ed items
			receivers.removeAll(defunct);
		}

	}

	/**
	 * collects receiver descriptors of super classes and interfaces.
	 * 
	 * @param eventClass
	 * @param receiverCollection
	 *            the collection receivers are put in.
	 */
	private static void collectReceiver(Class<?> eventClass,
			Collection<ReceiverDescriptor> receiverCollection) {
		if (receiverMap.get(eventClass) != null) {
			receiverCollection.addAll(receiverMap.get(eventClass));
		}
		if (!eventClass.isInterface()
				&& eventClass.getSuperclass() != Object.class) {
			collectReceiver(eventClass.getSuperclass(), receiverCollection);
		}
		for (Class interfaceClass : eventClass.getInterfaces()) {
			collectReceiver(interfaceClass, receiverCollection);
		}
	}
}
