
package net.micwin.ticino.entities ;

import java.util.UUID ;

/**
 * A helper class to simplify creation of ticino entities. Use this to create a
 * delegate. If you desire a base class to extend, use {@link AEntity}.
 * 
 * @author micwin
 */
public final class EntitySupport {

    transient UUID fPhysicalId ;
    int            fStateCounter = 0 ;

    public UUID getPhysicalId () {

        if (fPhysicalId == null) {

            fPhysicalId = UUID.randomUUID () ;
        }
        return fPhysicalId ;
    }

    public int getStateCounter () {

        return fStateCounter ;
    }

    /**
     * Raises the state counter by 1 and returns its new value.
     * 
     * @return new value after being risen.
     */
    public int raiseStateCounter () {

        return ++fStateCounter ;
    }

    /**
     * Raises the state counter by a given amount and return its new value.
     * 
     * @param pAmount
     * @return
     */
    public int raiseStateCounter (final int pAmount) {

        fStateCounter += pAmount ;
        return fStateCounter ;
    }

}
