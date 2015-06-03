
package net.micwin.ticino.plank.ui ;

import java.util.* ;

/**
 * Contains the UI.
 * 
 * @author micwin
 */
public class PlankUI {

    Map<String , PlankSection> fSection           = new HashMap<String , PlankSection> () ;

    private String             fActiveSectionName = null ;

    PlankSection               fActiveSection ;

    /**
     * Returns the section with specified name. Section "global" is always
     * visible.
     * 
     * @param pName
     * @return
     */
    public PlankSection getSection (final String pName) {

        return fSection.get (pName) ;
    }

    public void setSection (final String pName , final PlankSection pPlankSection) {

        fSection.put (pName , pPlankSection) ;

        if (fActiveSection == null && !PlankSection.GLOBAL.equals (pName)) {
            fActiveSectionName = pName ;
            fActiveSection = pPlankSection ;
        }
    }

    /**
     * @return the activeSectionName
     */
    public String getActiveSectionName () {

        return fActiveSectionName ;
    }

}
