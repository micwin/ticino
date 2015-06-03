
package net.micwin.ticino.actions ;

import net.micwin.ticino.plank.IPlankSession ;

/**
 * An action that causes the plank application to shut down.
 * 
 * @author micwin
 */

public class ShutdownApplicationAction implements IPlankAction {

    @Override
    public void perform (final IPlankSession pSession) {

        pSession.getApplication ().shutDown () ;

    }

}
