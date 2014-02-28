package net.micwin.ticino.spring;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.micwin.ticino.EventScope;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ConfiguratorTest {

	private EventScope<Object> scope;

	@Before
	public void setUp() throws Exception {
		scope = new EventScope<Object>();
	}

	@Test
	@Ignore
	@SuppressWarnings({ "rawtypes", "unchecked" })
	/**
	 * Known issue: sort of racing condition in garbage collector when comparing two Descriptors.
	 */
	public void testConfigurator() {

		final List hits = new LinkedList();

		Map<Class<? extends Object>, List<?>> registrations = new HashMap<Class<? extends Object>, List<?>>();

		Object p1 = new Object() {

			public void process1(Number event) {
				hits.add(toString());
			}

			public String toString() {
				return "process1";
			}

		};
		Object p2 = new Object() {

			public void process2(Number event) {
				hits.add(toString());
			}

			public String toString() {
				return "process2";
			}

		};
		registrations.put(Number.class, Arrays.asList(p1, p2));

		Object p3 = new Object() {

			public void process(List event) {
				hits.add(toString());
			}

			public String toString() {
				return "process3";
			}

		};
		Object p4 = new Object() {

			public void process(List event) {
				hits.add(toString());
			}

			@Override
			public String toString() {
				return "process4";
			}
		};
		Object p5 = new Object() {

			public void process5(List event) {
				hits.add(toString());
			}

			public String toString() {
				return "process5";
			}
		};
		List<Object> numberListeners = Arrays.asList(p3, p4, p5);

		registrations.put(List.class, numberListeners);

		Configurator<Object> config = new Configurator<Object>(registrations,
				scope);

		scope.dispatch(new LinkedList());

		assertEquals(3, hits.size());
		assertTrue(hits.contains("process3"));
		assertTrue(hits.contains("process4"));
		assertTrue(hits.contains("process5"));

		hits.clear();

		scope.dispatch(4.5);

		assertEquals(2, hits.size());
		assertTrue(hits.contains("process1"));
		assertTrue(hits.contains("process2"));

	}
}
