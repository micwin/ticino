package net.micwin.ticino;

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
 * A eventScope in which an event should get dispatched and receivers should
 * register to their specified events..
 * 
 * @author MicWin
 * 
 */
public class EventScope<T> {

	private String name;
	Map<Class<? extends T>, List<ReceiverDescriptor>> receiverMap = new HashMap<Class<? extends T>, List<ReceiverDescriptor>>();

	/**
	 * Creates a eventScope with given name.
	 * 
	 * @param name
	 */
	public EventScope(String name) {
		this.name = name;
	}

	/**
	 * Creates a eventScope with name "&lt;none&gt;".
	 * 
	 * @param name
	 */
	public EventScope() {
		this("<none>");
	}

	public String getScopeName() {
		return name;
	}

	/**
	 * Holds a weak reference to a receiver. This will not prevent the receiver
	 * to get garbage collected, so make sure to store the receiver elsewhere,
	 * for example you could pass in a spring bean.
	 * 
	 * @author MicWin
	 * 
	 */
	private class ReceiverDescriptor {
		WeakReference<Object> receiverReference;
		Method method;
		long creationTime = System.currentTimeMillis();
	}

	/**
	 * The public method to register a new event receiver.
	 * 
	 * @param eventClass
	 *            the class of event to be received.
	 * @param receiver
	 *            The receiver to get the event.
	 */
	public synchronized void register(Class<? extends T> eventClass,
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
	private void registerInternal(Object receiver, Method method,
			Class<? extends T> eventClass) {
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
	private Method detectReceiverMethod(Object receiver,
			Class<? extends T> eventClass) {
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
	public synchronized <Q extends T> Q dispatch(Q event) {

		Collection<ReceiverDescriptor> receivers = new TreeSet<ReceiverDescriptor>(
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
			return event;
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
		return event;

	}

	/**
	 * collects receiver descriptors of super classes and interfaces.
	 * 
	 * @param eventClass
	 * @param receiverCollection
	 *            the collection receivers are put in.
	 */
	private void collectReceiver(Class<?> eventClass,
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
