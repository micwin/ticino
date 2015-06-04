
package net.micwin.ticino.plank.ui ;

import net.micwin.ticino.plank.actions.IPlankAction ;

public class ActionItem {

    private final String       fName ;
    private final IPlankAction fAction ;

    public ActionItem (final String pName, final IPlankAction pAction) {

        fName = pName ;
        fAction = pAction ;
    }

    /**
     * @return the name
     */
    public String getName () {

        return fName ;
    }

    /**
     * @return the action
     */
    public IPlankAction getAction () {

        return fAction ;
    }

}
