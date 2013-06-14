package net.micwin.ticino;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests the ticino core. Since logic has been moved to EventScope, this tests
 * are doubled there.
 * 
 * @author MicWin
 * 
 */
public class TicinoTest {

	@Before
	public void before() {
		Ticino.eventScope = null;
	}

	@Test
	public void testGetScope() {
		assertNotNull(Ticino.getScope());
	}
}
