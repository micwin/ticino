
package net.micwin.ticino.plank.swing.application ;

import java.awt.event.ActionEvent ;
import java.util.Locale ;

import javax.swing.* ;

import net.micwin.ticino.actions.ActionItem ;
import net.micwin.ticino.plank.* ;
import net.micwin.ticino.plank.ui.PlankSection ;

/**
 * A jframe as companion of a plank UI.
 * 
 * @author micwin
 */
public class PlankSwingFrame extends JFrame {

    private static final long   serialVersionUID = -9165917481729675078L ;

    private final IPlankSession fSession ;

    private JMenuBar            fMenuBar ;

    public PlankSwingFrame (final IPlankSession pSession) {

        fSession = pSession ;

        final String lName = pSession.getApplication ().getName () ;

        setTitle (lName) ;
        setSize (800 , 600) ;
        setLocationRelativeTo (null) ;
        setDefaultCloseOperation (WindowConstants.DISPOSE_ON_CLOSE) ;

        updateSwingComponents () ;
    }

    /**
     * Updates the swing components to correspond to ui.
     * 
     * @param pSession
     */
    private void updateSwingComponents () {

        updateMenuBar () ;
        // updateQuickActions () ;
        // updateContent () ;

    }

    private void updateMenuBar () {

        // clear menu bar
        if (fMenuBar == null) {
            fMenuBar = new JMenuBar () ;
            setJMenuBar (fMenuBar) ;
        } else {
            fMenuBar.removeAll () ;
        }

        fMenuBar.add (composeGlobalSectionMenu ()) ;

    }

    private JMenu composeGlobalSectionMenu () {

        final PlankSection lGlobalSection = fSession.getUi ().getSection (PlankSection.GLOBAL) ;
        return composeSectionMenu (PlankSection.GLOBAL , lGlobalSection) ;
    }

    private JMenu composeSectionMenu (final String pName , final PlankSection pSection) {

        final String lLabelKey = "section." + pName ;
        final String lLabel = fSession.getApplication ().getContext ().getLabel (lLabelKey , Locale.getDefault ()) ;

        final JMenu jMenu = new JMenu (lLabel) ;

        composeItems (lLabelKey , pSection.getActions () , jMenu) ;

        return jMenu ;
    }

    private void composeItems (final String pParentName , final PlankActions pParent , final JMenu pParentMenu) {

        if (pParent == null) {
            return ;
        }

        for (final PlankActions lNode : pParent.getNodes ()) {
            final String lNodeName = pParentName + '.' + lNode.getName () ;
            final JMenu lNodeMenu = new JMenu (fSession.getApplication ().getContext ()
                    .getLabel (lNodeName , Locale.getDefault ())) ;

            composeItems (lNodeName , lNode , lNodeMenu) ;

            pParentMenu.add (lNodeMenu) ;

        }

        for (final ActionItem lAction : pParent.getActionItems ()) {
            final String lLabelKey = pParentName + "." + lAction.getName () ;
            final String lLabel = fSession.getApplication ().getContext ().getLabel (lLabelKey , Locale.getDefault ()) ;
            final JMenuItem lItem = new JMenuItem () ;
            lItem.setAction (new AbstractAction () {

                @Override
                public void actionPerformed (final ActionEvent pE) {

                    lAction.getAction ().perform (fSession) ;
                }

            }) ;

            lItem.setText (lLabel) ;

            pParentMenu.add (lItem) ;
        }

    }
}
