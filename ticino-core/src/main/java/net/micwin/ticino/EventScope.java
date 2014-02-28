package net.micwin.ticino;

import java.lang.ref.SoftReference;
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
	 * Holds a soft reference to a receiver. This will not prevent the receiver
	 * to get garbage collected, so make sure to store the receiver elsewhere,
	 * for example you could pass in a spring bean.
	 * 
	 * @author MicWin
	 * 
	 */
	private class ReceiverDescriptor {
		SoftReference<Object> receiverReference;
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
	 * @return this to enable chaining.
	 */
	public synchronized EventScope<T> register(Class<? extends T> eventClass,
			Object receiver) {
		Method method = detectReceiverMethod(receiver, eventClass);
		registerInternal(receiver, method, eventClass);
		return this;
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
		rd.receiverReference = new SoftReference<Object>(receiver);
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
							+ "' has more than one accessible method with a single parameter of type '"
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

						// direct check descriptor
						if (o1 == o2) {
							return 0;
						}

						boolean o1IsNull = o1 == null
								|| o1.receiverReference == null
								|| o1.receiverReference.get() == null;
						boolean o2IsNull = o2 == null
								|| o2.receiverReference == null
								|| o2.receiverReference.get() == null;

						if (o1IsNull && o2IsNull) {
							return 0;
						}

						if (o1IsNull) {
							return 1;
						}

						if (o2IsNull) {
							return -1;
						}

						// direct check receiverReference
						if (o1.receiverReference == o2.receiverReference) {
							return 0;
						}

						// direct check object
						if (o1.receiverReference.get() == o2.receiverReference
								.get()) {
							return 0;
						}

						// check creationDate
						if (o1.creationTime < o2.creationTime) {
							return -1;
						} else if (o1.creationTime > o2.creationTime) {
							return 1;
						}

						// when coming to this point, we really have a problem
						// to *order* the calls by registration.
						// so we assume: if two registration happen in the same
						// millisecond, then they are anonymous adapters

						int classComparison = o1.receiverReference
								.get()
								.getClass()
								.getName()
								.compareTo(
										o2.receiverReference.get().getClass()
												.getName());

						if (classComparison != 0) {
							return classComparison;
						}

						// same class

						// check wether we can use Comparable

						try {

							Comparable c1 = (Comparable) o1.receiverReference
									.get();
							Comparable c2 = (Comparable) o2.receiverReference
									.get();
							int comparison = c1.compareTo(c2);
							if (comparison != 0) {
								return comparison;
							}

						} catch (ClassCastException e) {
							// no, not a comparable
						}

						// same class, so we try to differentiate using the hash
						// code
						Integer h1 = o1.receiverReference.get().hashCode();
						Integer h2 = o2.receiverReference.get().hashCode();

						return h1.compareTo(h2);
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
