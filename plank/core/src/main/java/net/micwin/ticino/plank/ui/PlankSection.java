
package net.micwin.ticino.plank.ui ;

import net.micwin.ticino.plank.PlankActions ;

/**
 * A section of the ui. Can contain menus, links and a content which all are
 * activated when the section comes into view.
 * 
 * @author micwin
 */

public class PlankSection {

    /**
     * Identifier for the always visible section.
     */
    public static final String GLOBAL = "global" ;

    private PlankActions       fActions ;

    public PlankActions getActions () {

        return fActions ;
    }

    public void setActions (final PlankActions pActions) {

        fActions = pActions ;
    }

}
