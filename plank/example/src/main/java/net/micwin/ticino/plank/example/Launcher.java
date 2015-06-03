
package net.micwin.ticino.plank.example ;

import net.micwin.ticino.plank.* ;
import net.micwin.ticino.plank.swing.application.SwingApplicationPlatform ;

import org.apache.commons.configuration.ConfigurationException ;

/**
 * This is a sample application to show the versatility of ticino plank.
 * 
 * @author micwin
 */
public class Launcher {

    public static void main (final String [] args) throws ConfigurationException {

        final IPlankApplication lApplication = new PlankExampleApplication () ;

        final IPlankPlatform lPlatform = new SwingApplicationPlatform () ;

        lPlatform.start (lApplication) ;

    }
}
