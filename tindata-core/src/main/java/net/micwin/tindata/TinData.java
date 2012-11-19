package net.micwin.tindata;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The core tinData event manager.
 * 
 * @author MicWin
 * 
 */
public class TinData {

	private static class ReceiverDescriptor {
		Object receiver;
		Method method;
	}

	private static Map<Class<?>, List<ReceiverDescriptor>> receiverMap = new HashMap<Class<?>, List<ReceiverDescriptor>>();

	/**
	 * The public method to register a new event receiver.
	 * 
	 * @param eventClass
	 *            the class of event to be received.
	 * @param receiver
	 *            The receiver to get the event.
	 */
	public synchronized static void register(Class<?> eventClass, Object receiver) {
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
	private static void registerInternal(Object receiver, Method method, Class<?> eventClass) {
		List<ReceiverDescriptor> receivers = receiverMap.get(eventClass);
		if (receivers == null) {
			receivers = new LinkedList<ReceiverDescriptor>();
			receiverMap.put(eventClass, receivers);
		}

		ReceiverDescriptor rd = new ReceiverDescriptor();
		rd.receiver = receiver;
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
	private static Method detectReceiverMethod(Object receiver, Class<?> eventClass) {
		Method[] methods = receiver.getClass().getMethods();
		for (Method method : methods) {

			if (method.getParameterTypes() == null || method.getParameterTypes().length != 1) {
				// wrong amount of parameters -> try next
				continue;
			}

			if (method.getParameterTypes()[0].isAssignableFrom(eventClass)) {
				// hit!
				return method;
			}

		}

		// no hit _> exception
		throw new IllegalArgumentException("receiver '" + receiver
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

		List<ReceiverDescriptor> receivers = receiverMap.get(event.getClass());
		// no receivers registered, so bye-bye
		if (receivers == null || receivers.size() < 1) {
			return;
		}

		for (ReceiverDescriptor receiverDescriptor : receivers) {
			try {
				receiverDescriptor.method.invoke(receiverDescriptor.receiver, event);
			} catch (Exception e) {
				throw new DispatchException(receiverDescriptor.receiver, event, e);
			}
		}
	}
}
