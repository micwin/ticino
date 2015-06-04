
package net.micwin.ticino.plank ;

/**
 * The entry point for a plank application.
 * 
 * @author micwin
 */
public interface IPlankApplication {

    /**
     * Initializes the application with given config.
     * 
     * @param pContext
     */
    void initialize (IPlankApplicationContext pContext) ;

    /**
     * Connects a new user to the application. If the application requires to
     * log in, the session and its user are considered to not being logged in.
     * 
     * @return
     */
    PlankSession openNewSession () ;

    /**
     * returns human readable name of application
     * 
     * @return
     */
    String getName () ;

    IPlankApplicationContext getContext () ;

    /**
     * Shuts the plank application down.
     */
    void shutDown () ;

}
