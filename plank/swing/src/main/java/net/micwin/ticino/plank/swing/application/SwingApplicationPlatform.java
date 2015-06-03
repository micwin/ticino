
package net.micwin.ticino.plank.swing.application ;

import net.micwin.ticino.plank.* ;

public class SwingApplicationPlatform implements IPlankPlatform {

    private SwingApplicationContext fContext ;
    private PlankSwingFrame         fFrame ;
    private IPlankApplication       fApplication ;

    @Override
    public void start (final IPlankApplication pApplication) {

        fApplication = pApplication ;

        fContext = new SwingApplicationContext (this) ;

        pApplication.initialize (fContext) ;

        final IPlankSession lSession = pApplication.openNewSession () ;

        fFrame = new PlankSwingFrame (lSession) ;

        fFrame.setVisible (true) ;
    }

    @Override
    public void shutDown () {

        fApplication.shutDown () ;
        fFrame.setVisible (false) ;
        try {
            Thread.sleep (1000) ;
        } catch (final InterruptedException e) {
            e.printStackTrace () ;
        }

        System.exit (0) ;
    }

}
