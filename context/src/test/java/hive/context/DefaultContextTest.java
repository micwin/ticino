package hive.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

public class DefaultContextTest {

	private DefaultContext<String> fCtx;

	@Before
	public void before() {
		fCtx = new DefaultContext<String>("A", "B", "C");
	}

	@Test
	public void testDefaultContext() {

		assertEquals(3, fCtx.getCurrentElementCount());
		assertEquals(Integer.MAX_VALUE, fCtx.getMaxStructureSize());
		assertEquals(Integer.MAX_VALUE, fCtx.getMaxTheoreticalSize());

	}

	@Test
	public void testIterator() {
		Iterator<String> lIterator = fCtx.iterator();
		assertTrue(lIterator.hasNext());
		assertEquals("A", lIterator.next());
		assertEquals("B", lIterator.next());
		assertEquals("C", lIterator.next());
		assertFalse(lIterator.hasNext());

	}

	@Test
	public void testLookupPredicateOfElementTypeInt() {
		IModifyableContext<String> lLookup = fCtx.lookup(pElement -> true, 2);
		assertEquals(2, lLookup.getCurrentElementCount());

	}

	@Test
	public void testLookupPredicateOfElementTypeIntTargetContextType() {
		DefaultContext<String> lTarget = new DefaultContext<>(Arrays.asList());
		IModifyableContext<String> lLookup = fCtx.lookup(pElement -> true, 2, lTarget);
		assertSame(lTarget, lLookup);
		assertEquals(2, lLookup.getCurrentElementCount());
	}

	@Test
	public void testGetMaxStructureSize() {
		assertEquals(Integer.MAX_VALUE, fCtx.getMaxStructureSize());
	}

	@Test
	public void testGetMaxTheoreticalSize() {
		assertEquals(Integer.MAX_VALUE, fCtx.getMaxTheoreticalSize());
	}

	@Test
	public void testGetCurrentElementCount() {
		assertEquals(3, fCtx.getCurrentElementCount());
	}

	@Test
	public void testPut() {

		DefaultContext<String> lPut = fCtx.put("D");
		assertSame(fCtx, lPut);
		assertEquals(4, fCtx.getCurrentElementCount());
		assertEquals(1, fCtx.lookup((s) -> "D".equals(s)).getCurrentElementCount());

	}

	@Test
	public void testPutAll() {

		DefaultContext<String> lNewElements = new DefaultContext<>(Arrays.asList("X", "Y", "Z"));
		fCtx.putAll(lNewElements);

		assertEquals(6, fCtx.getCurrentElementCount());
		assertEquals(1, fCtx.lookup((s) -> "X".equals(s)).getCurrentElementCount());
		assertEquals(1, fCtx.lookup((s) -> "Y".equals(s)).getCurrentElementCount());
		assertEquals(1, fCtx.lookup((s) -> "Z".equals(s)).getCurrentElementCount());
	}

	@Test
	public void testValidator() {

		fCtx.setValidator((s) -> "A".equals(s));
		fCtx.put("A");

	}

	@Test(expected = IllegalArgumentException.class)
	public void testValidator_fail() {
		fCtx.setValidator((s) -> "A".equals(s));
		fCtx.put("B");
	}
}
