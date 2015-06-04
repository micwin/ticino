
package net.micwin.ticino.plank ;

import net.micwin.ticino.plank.ui.PlankUI ;

/**
 * A unser session.
 * 
 * @author micwin
 */

public class PlankSession {

    private PlankUI                 fPlankUi ;
    private final IPlankApplication fApplication ;

    public PlankSession (final IPlankApplication pPlankApplication) {

        fApplication = pPlankApplication ;

    }

    /**
     * returns the current UI state. Note that in plank the ui state is nothing
     * global, but heavily user specific. The initial state is built in
     * {@link IPlankApplication#openNewSession()} and modified througout the
     * course of user interaction.
     * 
     * @return
     */
    public PlankUI getUi () {

        return fPlankUi ;
    }

    public IPlankApplication getApplication () {

        return fApplication ;
    }

    public void setUi (final PlankUI pPlankUI) {

        fPlankUi = pPlankUI ;

    }

}
