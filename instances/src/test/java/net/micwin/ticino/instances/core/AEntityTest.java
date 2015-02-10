
package net.micwin.ticino.instances.core ;

import static org.junit.Assert.* ;

import org.junit.* ;

public class AEntityTest {

    AEntity fEntity ;

    @Before
    public void before () {

        fEntity = new AEntity () {
        } ;
    }

    @Test
    public void testGetPhysicalId () {

        assertNull (fEntity.fPhysicalId) ;
        assertNotNull (fEntity.getPhysicalId ()) ;
        assertNotNull (fEntity.fPhysicalId) ;
    }

    @Test
    public void testGetStateCounter () {

        assertEquals (0 , fEntity.getStateCounter ()) ;
    }

    @Test
    public void testRaiseStateCounter () {

        assertEquals (1 , fEntity.raiseStateCounter ()) ;
        assertEquals (2 , fEntity.raiseStateCounter ()) ;
        assertEquals (3 , fEntity.raiseStateCounter ()) ;

    }

    @Test
    public void testRaiseStateCounterInt () {

        assertEquals (7 , fEntity.raiseStateCounter (7)) ;
        assertEquals (10 , fEntity.raiseStateCounter (3)) ;
        assertEquals (20 , fEntity.raiseStateCounter (10)) ;

    }

}
