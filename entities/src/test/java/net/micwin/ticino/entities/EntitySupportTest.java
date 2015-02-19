
package net.micwin.ticino.entities ;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class EntitySupportTest {

    EntitySupport fEntitySupport ;

    @Before
    public void before () {

        fEntitySupport = new EntitySupport() ;
    }

    @Test
    public void testGetPhysicalId () {

        assertNull (fEntitySupport.fPhysicalId) ;
        assertNotNull (fEntitySupport.getPhysicalId ()) ;
        assertNotNull (fEntitySupport.fPhysicalId) ;
    }

    @Test
    public void testGetStateCounter () {

        assertEquals (0 , fEntitySupport.getStateCounter ()) ;
    }

    @Test
    public void testRaiseStateCounter () {

        assertEquals (1 , fEntitySupport.raiseStateCounter ()) ;
        assertEquals (2 , fEntitySupport.raiseStateCounter ()) ;
        assertEquals (3 , fEntitySupport.raiseStateCounter ()) ;

    }

    @Test
    public void testRaiseStateCounterInt () {

        assertEquals (7 , fEntitySupport.raiseStateCounter (7)) ;
        assertEquals (10 , fEntitySupport.raiseStateCounter (3)) ;
        assertEquals (20 , fEntitySupport.raiseStateCounter (10)) ;

    }

}
