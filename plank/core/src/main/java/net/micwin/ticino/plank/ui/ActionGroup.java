
package net.micwin.ticino.plank.ui ;

import java.util.* ;

import net.micwin.ticino.plank.actions.ShutdownPlatformAction ;

public class ActionGroup {

    private String                  fName ;
    private final List<ActionGroup> fNodes       = new LinkedList<ActionGroup> () ;
    private final List<ActionItem>  fActionItems = new LinkedList<ActionItem> () ;

    public ActionGroup (final String pName) {

        fName = pName ;

    }

    public String getName () {

        return fName ;
    }

    public void setName (final String pName) {

        fName = pName ;
    }

    public List<ActionGroup> getNodes () {

        return fNodes ;
    }

    public void addNode (final ActionGroup pNewNode) {

        fNodes.add (pNewNode) ;

    }

    public void addAction (final String pString , final ShutdownPlatformAction pShutdownPlatformAction) {

        fActionItems.add (new ActionItem (pString , pShutdownPlatformAction)) ;

    }

    public List<ActionItem> getActionItems () {

        return fActionItems ;
    }

}
