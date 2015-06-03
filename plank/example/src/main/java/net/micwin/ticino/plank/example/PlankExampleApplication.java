
package net.micwin.ticino.plank.example ;

import net.micwin.ticino.actions.ShutdownPlatformAction ;
import net.micwin.ticino.plank.* ;
import net.micwin.ticino.plank.ui.* ;

import org.slf4j.* ;

/**
 * Configures the plank example application.
 * 
 * @author micwin
 */

public class PlankExampleApplication implements IPlankApplication {

    private static final Logger      L = LoggerFactory.getLogger (PlankExampleApplication.class) ;
    private IPlankApplicationContext fContext ;

    @Override
    public void initialize (final IPlankApplicationContext pContext) {

        fContext = pContext ;
        L.info ("initialized.") ;

    }

    @Override
    public IPlankSession openNewSession () {

        final PlankExampleSession lSession = new PlankExampleSession (this) ;
        lSession.setUi (createInitialUi (lSession)) ;
        return lSession ;
    }

    private PlankUI createInitialUi (final PlankExampleSession pSession) {

        final PlankUI lPlankUi = new PlankUI () ;

        final PlankSection lGlobalSection = new PlankSection () ;

        final PlankActions lGlobalActions = new PlankActions ("actions") ;
        lGlobalActions.addAction ("shutdown" , new ShutdownPlatformAction ()) ;

        lGlobalSection.setActions (lGlobalActions) ;
        lPlankUi.setSection (PlankSection.GLOBAL , lGlobalSection) ;
        return lPlankUi ;
    }

    @Override
    public String getName () {

        return PlankExampleApplication.class.getSimpleName () ;
    }

    @Override
    public IPlankApplicationContext getContext () {

        return fContext ;
    }

    @Override
    public void shutDown () {

        fContext = null ;
        L.info ("shut down") ;

    }

}
