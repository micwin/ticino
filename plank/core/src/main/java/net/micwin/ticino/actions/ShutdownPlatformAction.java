
package net.micwin.ticino.actions ;

import net.micwin.ticino.plank.IPlankSession ;

/**
 * Shuts the whole platform gracefully down.
 * 
 * @author micwin
 */
public class ShutdownPlatformAction implements IPlankAction {

    @Override
    public void perform (final IPlankSession pSession) {

        pSession.getApplication ().getContext ().getPlatform ().shutDown () ;
    }
}
