
package net.micwin.ticino.plank ;

import java.util.Locale ;

/**
 * THer application context holding config an resource bundle short cuts.
 * 
 * @author micwin
 */
public interface IPlankApplicationContext {

    IPlankPlatform getPlatform () ;

    /**
     * Returns the human readable label with specified key for specified locale.
     * 
     * @param pLabelKey
     * @param pLocale
     * @return
     */
    String getLabel (String pLabelKey , Locale pLocale) ;

}
