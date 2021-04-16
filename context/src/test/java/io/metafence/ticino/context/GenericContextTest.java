
package io.metafence.ticino.context;

import static org.junit.Assert.* ;

import java.util.* ;

import org.junit.* ;

public class GenericContextTest {

    private GenericContext<String> fCtx ;

    @Before
    public void before () {

        fCtx = new GenericContext<String> ("A" , "B" , "C") ;
    }

    @Test
    public void testDefaultContext () {

        assertEquals (3 , fCtx.getCurrentElementCount ()) ;
        assertEquals (Integer.MAX_VALUE , fCtx.getMaxStructureSize ()) ;
        assertEquals (Integer.MAX_VALUE , fCtx.getMaxTheoreticalSize ()) ;

    }

    @Test
    public void testIterator () {

        final Iterator<String> lIterator = fCtx.iterator () ;
        assertTrue (lIterator.hasNext ()) ;
        assertEquals ("A" , lIterator.next ()) ;
        assertEquals ("B" , lIterator.next ()) ;
        assertEquals ("C" , lIterator.next ()) ;
        assertFalse (lIterator.hasNext ()) ;

    }

    @Test
    public void testLookupPredicateOfElementTypeInt () {

        final IModifyableContext<String> lLookup = fCtx.lookup (pElement -> true , 2) ;
        assertEquals (2 , lLookup.getCurrentElementCount ()) ;

    }

    @Test
    public void testLookupPredicateOfElementTypeIntTargetContextType () {

        final GenericContext<String> lTarget = new GenericContext<> (Arrays.asList ()) ;
        final IModifyableContext<String> lLookup = fCtx.lookup (pElement -> true , 2 , lTarget) ;
        assertSame (lTarget , lLookup) ;
        assertEquals (2 , lLookup.getCurrentElementCount ()) ;
    }

    @Test
    public void testGetMaxStructureSize () {

        assertEquals (Integer.MAX_VALUE , fCtx.getMaxStructureSize ()) ;
    }

    @Test
    public void testGetMaxTheoreticalSize () {

        assertEquals (Integer.MAX_VALUE , fCtx.getMaxTheoreticalSize ()) ;
    }

    @Test
    public void testGetCurrentElementCount () {

        assertEquals (3 , fCtx.getCurrentElementCount ()) ;
    }

    @Test
    public void testPut () {

        final GenericContext<String> lPut = fCtx.put ("D") ;
        assertSame (fCtx , lPut) ;
        assertEquals (4 , fCtx.getCurrentElementCount ()) ;
        assertEquals (1 , fCtx.lookup ( (s) -> "D".equals (s)).getCurrentElementCount ()) ;

    }

    @Test
    public void testPutAll () {

        final GenericContext<String> lNewElements = new GenericContext<> (Arrays.asList ("X" , "Y" , "Z")) ;
        fCtx.putAll (lNewElements) ;

        assertEquals (6 , fCtx.getCurrentElementCount ()) ;
        assertEquals (1 , fCtx.lookup ( (s) -> "X".equals (s)).getCurrentElementCount ()) ;
        assertEquals (1 , fCtx.lookup ( (s) -> "Y".equals (s)).getCurrentElementCount ()) ;
        assertEquals (1 , fCtx.lookup ( (s) -> "Z".equals (s)).getCurrentElementCount ()) ;
    }

}
