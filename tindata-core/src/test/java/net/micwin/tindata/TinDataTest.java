package net.micwin.tindata;

import static org.junit.Assert.*;

import org.junit.Test;

// Tests the tindata core
public class TinDataTest {

	class EventBase {
	}

	class DummyEvent extends EventBase {

	}

	class DummyReceiver {

		private int received;

		public void receive(EventBase evt) {
			received++;
		}
	}

	@Test
	public void testWorkflow() {

		DummyReceiver receiver = new DummyReceiver();
		TinData.register(DummyEvent.class, receiver);

		TinData.dispatch(new DummyEvent());
		assertEquals(1, receiver.received);

	}
}
