
package net.micwin.ticino.plank ;

import java.util.* ;

import net.micwin.ticino.actions.* ;

public class PlankActions {

    private String                   fName ;
    private final List<PlankActions> fNodes       = new LinkedList<PlankActions> () ;
    private final List<ActionItem>   fActionItems = new LinkedList<ActionItem> () ;

    public PlankActions (final String pName) {

        fName = pName ;

    }

    public String getName () {

        return fName ;
    }

    public void setName (final String pName) {

        fName = pName ;
    }

    public List<PlankActions> getNodes () {

        return fNodes ;
    }

    public void addNode (final PlankActions pNewNode) {

        fNodes.add (pNewNode) ;

    }

    public void addAction (final String pString , final ShutdownPlatformAction pShutdownPlatformAction) {

        fActionItems.add (new ActionItem (pString , pShutdownPlatformAction)) ;

    }

    public List<ActionItem> getActionItems () {

        return fActionItems ;
    }

}
