
package net.micwin.ticino.plank.actions ;

import net.micwin.ticino.plank.PlankSession ;

/**
 * Shuts the whole platform gracefully down.
 * 
 * @author micwin
 */
public class ShutdownPlatformAction implements IPlankAction {

    @Override
    public void perform (final PlankSession pSession) {

        pSession.getApplication ().getContext ().getPlatform ().shutDown () ;
    }
}
