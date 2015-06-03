
package net.micwin.ticino.plank ;

import net.micwin.ticino.plank.ui.PlankUI ;

/**
 * A unser session.
 * 
 * @author micwin
 */

public interface IPlankSession {

    /**
     * returns the current UI state. Note that in plank the ui state is nothing
     * global, but heavily user specific. The initial state is built in
     * {@link IPlankApplication#openNewSession()} and modified througout the
     * course of user interaction.
     * 
     * @return
     */
    PlankUI getUi () ;

    IPlankApplication getApplication () ;

}
