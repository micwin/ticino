package net.micwin.ticino;

/**
 * The static Ticino context. To register a receiver, call <code>register</code>
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
 * This static {@link EventScope} is bound to the class loader, not to the vm.
 * Hence, events dispatched in one jee application will not necessarily get
 * passed to receivers registered in another application on the same container
 * instance.
 * 
 * @author MicWin
 * 
 */
public class Ticino {

	/**
	 * The eventScope used by this static access proxy.
	 */
	@SuppressWarnings("rawtypes")
	static EventScope eventScope;

	@SuppressWarnings("unchecked")
	public static void register(Class<?> evtClass, Object receiver) {
		getScope().register(evtClass, receiver);
	}

	@SuppressWarnings("rawtypes")
	public static EventScope getScope() {
		if (eventScope == null) {
			eventScope = new EventScope("classloader");
		}
		return eventScope;
	}

	@SuppressWarnings("unchecked")
	public static void dispatch(Object event) {
		getScope().dispatch(event);
	}
}
