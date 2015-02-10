
package net.micwin.ticino.instances.core ;

import java.util.UUID ;

/**
 * A helper class to simplify creation of ticino entities.
 * 
 * @author micwin
 */
public abstract class AEntity implements IEntity {

    transient UUID fPhysicalId ;
    int            fStateCounter = 0 ;

    @Override
    public UUID getPhysicalId () {

        if (fPhysicalId == null) {

            fPhysicalId = UUID.randomUUID () ;
        }
        return fPhysicalId ;
    }

    @Override
    public int getStateCounter () {

        return fStateCounter ;
    }

    /**
     * Raises the state counter by 1 and returns its new value.
     * 
     * @return new value after being risen.
     */
    protected int raiseStateCounter () {

        return ++fStateCounter ;
    }

    /**
     * Raises the state counter by a given amopunt and return its new value.
     * 
     * @param pAmount
     * @return
     */
    protected int raiseStateCounter (final int pAmount) {

        fStateCounter += pAmount ;
        return fStateCounter ;
    }

}
