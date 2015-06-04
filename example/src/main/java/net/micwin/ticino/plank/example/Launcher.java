
package net.micwin.ticino.plank.example ;

import net.micwin.ticino.plank.* ;
import net.micwin.ticino.plank.swing.application.SwingApplicationPlatform ;

import org.apache.commons.configuration.ConfigurationException ;

/**
 * This is a sample application to show the versatility of ticino.
 * 
 * @author micwin
 */
public class Launcher {

    public static void main (final String [] pArgs) throws ConfigurationException {

        // get application instance
        final IPlankApplication lApplication = new PlankExampleApplication () ;

        // construct platform
        final IPlankPlatform lPlatform = new SwingApplicationPlatform (pArgs) ;

        // bring them together.
        lPlatform.start (lApplication) ;

    }
}
