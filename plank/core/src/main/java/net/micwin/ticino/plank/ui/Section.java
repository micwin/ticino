
package net.micwin.ticino.plank.ui ;


/**
 * A section of the ui. Can contain menus, links and a content which all are
 * activated when the section comes into view.
 * 
 * @author micwin
 */

public class Section {

    /**
     * Identifier for the always visible section.
     */
    public static final String GLOBAL = "global" ;

    private ActionGroup       fActions ;

    public ActionGroup getActions () {

        return fActions ;
    }

    public void setActions (final ActionGroup pActions) {

        fActions = pActions ;
    }

}
