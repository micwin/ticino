package net.micwin.ticino.wiring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;

import org.junit.Test;

public class WiringBuilderTest {

	@Test
	public void testSmoke() {
		WiringBuilder lBuilder = new WiringBuilder();
		lBuilder.add("Hello, World");
		lBuilder.add(new LinkedList<String>());
		lBuilder.add(1);
		lBuilder.add(1.5);
		lBuilder.add(new DummyNode());
		Wiring lWiring = lBuilder.wire();

		DummyNode lNode = lWiring.get (DummyNode.class);
		

		assertEquals("Hello, World", lNode.fName);
		assertEquals(1, lNode.fIntegerValue);
		assertEquals(1.5, lNode.fDoubleValue, 0.01);
	}

}
