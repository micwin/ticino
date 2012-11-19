package net.micwin.tindata;

/**
 * Thrown when something goes wrong while dispatching an event.
 * 
 * @author MicWin
 * 
 */
public class DispatchException extends RuntimeException {

	private static final long serialVersionUID = -8048734860684723856L;

	public DispatchException(Object receiver, Object event, Exception e) {
		super("cannot dispatch event '" + event + "'", e);
	}

}
