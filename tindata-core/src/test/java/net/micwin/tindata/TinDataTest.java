package net.micwin.tindata;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

// Tests the tindata core
public class TinDataTest {

	/**
	 * A mock dummy event.
	 * 
	 * @author MicWin
	 * 
	 */
	class DummyEvent {

	}

	/**
	 * A dummy receiver that receives the dummy event.
	 */
	class DummyReceiver {

		private int received;

		/**
		 * Dont do this in real life since the method then might get shadowed by
		 * other methods not associated with event handling.
		 * 
		 * @param evt
		 */
		public void receive(Object evt) {
			received++;
		}
	}

	/**
	 * This is a simple test with a mock event and mock receiver. It
	 * demonstrates the default event work flow: registering, dsipatching,
	 * receiving.
	 */
	@Test
	public void testWorkflow() {

		DummyReceiver receiver = new DummyReceiver();
		TinData.register(DummyEvent.class, receiver);

		TinData.dispatch(new DummyEvent());
		assertEquals(1, receiver.received);
	}

	/**
	 * This is an absurd test that perfectly demonstrates the absolute
	 * flexibility of tinData. In fact, this is absolute pojo; you dont have to
	 * create event or payload classes if there already is something like an
	 * already programmed event or payload class around, like in wicket, or JMS.
	 * <p />
	 * <strong>CAUTION!</strong> Using very basic types like Integers really
	 * only is advisable for demonstration purposes. For instance, the problem
	 * comes with registration where *all* methods receiving a primitive int or
	 * an Integer automatically become possible receiver methods. You dont want
	 * that.
	 */
	@Test
	public void testAbsurd() {

		// create a container to put the payload in.
		final List<Integer> payload = new LinkedList<Integer>();

		// register an anonymous adapter as receiver, that puts the received
		// "event" (i.e. the Integer) into the list above.
		TinData.register(Integer.class, new Object() {
			@SuppressWarnings("unused")
			public void receive(Integer i) {
				// this is the receiver method.
				payload.add(i);
			}
		});

		// trigger receiver twice
		TinData.dispatch(4);
		TinData.dispatch(42);

		// check that both integers have been put into the list.
		assertEquals((Integer) 4, payload.get(0));
		assertEquals((Integer) 42, payload.get(1));

		TinData.register(String.class, new Object() { public void yaho(String s) { System.out.println(s); } });
		TinData.dispatch("hello, world!");
	}
}
