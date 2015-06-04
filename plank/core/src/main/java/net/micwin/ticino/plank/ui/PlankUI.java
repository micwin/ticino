
package net.micwin.ticino.plank.ui ;

import java.util.* ;

/**
 * Contains the UI.
 * 
 * @author micwin
 */
public class PlankUI {

    Map<String , Section> fSection           = new HashMap<String , Section> () ;

    private String             fActiveSectionName = null ;

    Section               fActiveSection ;

    /**
     * Returns the section with specified name. Section "global" is always
     * visible.
     * 
     * @param pName
     * @return
     */
    public Section getSection (final String pName) {

        return fSection.get (pName) ;
    }

    public void setSection (final String pName , final Section pPlankSection) {

        fSection.put (pName , pPlankSection) ;

        if (fActiveSection == null && !Section.GLOBAL.equals (pName)) {
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
