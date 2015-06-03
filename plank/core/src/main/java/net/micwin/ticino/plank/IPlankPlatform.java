
package net.micwin.ticino.plank ;

/**
 * A platform where a plank application could be run.
 * 
 * @author micwin
 */

public interface IPlankPlatform {

    /**
     * Starts specified application.
     * 
     * @param pApplication
     */
    void start (IPlankApplication pApplication) ;

    /**
     * close all applications gracefully and then terminate.
     */
    void shutDown () ;

}
