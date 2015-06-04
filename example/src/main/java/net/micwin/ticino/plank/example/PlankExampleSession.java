
package net.micwin.ticino.plank.example ;

import net.micwin.ticino.plank.* ;
import net.micwin.ticino.plank.ui.PlankUI ;

public class PlankExampleSession implements IPlankSession {

    private PlankUI                       fUi ;
    private final PlankExampleApplication fPlankExampleApplication ;

    public PlankExampleSession (final PlankExampleApplication pPlankExampleApplication) {

        fPlankExampleApplication = pPlankExampleApplication ;

    }

    @Override
    public PlankUI getUi () {

        return fUi ;
    }

    @Override
    public IPlankApplication getApplication () {

        return fPlankExampleApplication ;
    }

    public void setUi (final PlankUI pPlankUi) {

        fUi = pPlankUi ;

    }

}
