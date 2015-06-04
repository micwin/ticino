
package net.micwin.ticino.plank.actions ;

import net.micwin.ticino.plank.PlankSession ;

/**
 * An action that causes the plank application to shut down.
 * 
 * @author micwin
 */

public class ShutdownApplicationAction implements IPlankAction {

    @Override
    public void perform (final PlankSession pSession) {

        pSession.getApplication ().shutDown () ;

    }

}
