
package net.micwin.ticino.plank.swing.application ;

import java.util.* ;

import net.micwin.ticino.plank.* ;

/**
 * Application COntext of swing applications.
 * 
 * @author micwin
 */

public class SwingApplicationContext implements IPlankApplicationContext {

    private final SwingApplicationPlatform fSwingApplicationPlatform ;

    public SwingApplicationContext (final SwingApplicationPlatform pSwingApplicationPlatform) {

        fSwingApplicationPlatform = pSwingApplicationPlatform ;
    }

    @Override
    public String getLabel (final String pLabelKey , final Locale pLocale) {

        final ResourceBundle lBundle = ResourceBundle.getBundle ("plank" , pLocale) ;

        if (!lBundle.containsKey (pLabelKey)) {
            return "!!!" + pLabelKey ;
        }
        return lBundle.getString (pLabelKey) ;

    }

    @Override
    public IPlankPlatform getPlatform () {

        return fSwingApplicationPlatform ;
    }

}
