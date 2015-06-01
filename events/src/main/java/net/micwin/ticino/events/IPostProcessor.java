package net.micwin.ticino.events;

/**
 * A post processor is an event receiver that gets the event as last instance.
 * It also gets exceptions et al.
 * 
 * @author micwin
 *
 */
public interface IPostProcessor<E> {

	/**
	 * Called when the event passes as a regular success.
	 * 
	 * @param pProcessedEvent
	 */
	public void done(E pProcessedEvent);

	/**
	 * Called when the event processing ends up with an exception.
	 * 
	 * @param pException
	 */
	public void done(DispatchException pException);

}
