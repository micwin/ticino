package net.micwin.ticino;

/**
 * The Ticino core class. To register a receiver, call <code>register</code>
 * with an event class and a receiver providing an accessible public method that
 * has the event class as sole parameter:
 * 
 * <pre>
 * Ticino.register(MyEvent.class, new Object() {
 * 	public void myReceiverMethod(MyEvent evt) {
 * 		// do something meaningful
 * 	}
 * });
 * </pre>
 * 
 * @author MicWin
 * 
 */
public class Ticino {

	/**
	 * The eventScope used by this static access proxy.
	 */
	static EventScope eventScope;

	public static void register(Class<?> evtClass, Object receiver) {
		getScope().register(evtClass, receiver);
	}

	static EventScope getScope() {
		if (eventScope == null) {
			eventScope = new EventScope("classloader");
		}
		return eventScope;
	}

	public static void dispatch(Object event) {
		getScope().dispatch(event);
	}
}
