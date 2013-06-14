package net.micwin.ticino;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


import org.junit.Before;
import org.junit.Test;

public class EventScopeTest {

	private EventScope eventScope;

	

	/**
	 * An interface for a dummy event
	 * 
	 * @author MicWin
	 * 
	 */
	interface IDummyEvent {

	}

	/**
	 * A mock dummy event.
	 * 
	 * @author MicWin
	 * 
	 */
	class DummyEventImpl implements IDummyEvent {

	}

	/**
	 * A dummy receiver that receives the dummy event.
	 */
	class DummyReceiver {

		private int received;

		/**
		 * Dont do this (using Object as event type) in real life since the method then might get shadowed by
		 * other methods not associated with event handling.
		 * 
		 * @param evt
		 */
		public void receive(Object evt) {
			received++;
		}
	}
	
	@Before
	public void before() {
		eventScope = new EventScope("unitTest") ; 
	}

	@Test
	public void testInit() {
		assertEquals("unitTest", eventScope.getScopeName());
		assertEquals(0, eventScope.receiverMap.size());
	}


	/**
	 * This is a simple test with a mock event and mock receiver. It
	 * demonstrates the default event work flow: registering, dsipatching,
	 * receiving.
	 */
	@Test
	public void testWorkflow() {

		DummyReceiver receiver = new DummyReceiver();
		eventScope.register(DummyEventImpl.class, receiver);

		eventScope.dispatch(new DummyEventImpl());
		assertEquals(1, receiver.received);
	}

	/**
	 * This is an absurd test that perfectly demonstrates the absolute
	 * flexibility of ticino. In fact, this is absolute pojo; you dont have to
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
		eventScope.register(Integer.class, new Object() {
			@SuppressWarnings("unused")
			public void receive(Integer i) {
				// this is the receiver method.
				payload.add(i);
			}
		});

		// trigger receiver twice
		eventScope.dispatch(4);
		eventScope.dispatch(42);

		// check that both integers have been put into the list (and hence rthe
		// receivers have been processed).
		assertEquals((Integer) 4, payload.get(0));
		assertEquals((Integer) 42, payload.get(1));
	}

	@Test
	public void testIllegalClass() {

		try {
			eventScope.register(String.class, new Object() {

				// receiver that has two methods that possibly could catch the
				// event
				@SuppressWarnings("unused")
				public void receive1(String evt) {

				}

				@SuppressWarnings("unused")
				public void receive2(String evt) {
				}
			});

			fail("illegal receiver accepted");

		} catch (IllegalArgumentException iae) {
			// w^5

		}
	}


	@Test
	public void testReceiverThrowingException() {
		final Collection<String> c = new LinkedList<String>();
		try {

			// register first receiver, throwing an exception.
			eventScope.register(String.class, new Object() {
				@SuppressWarnings("unused")
				public void receive(String message) throws IOException {
					throw new IOException();
				}
			});
			// register second receiver, expecting work to do (but not reached)
			eventScope.register(String.class, new Object() {
				@SuppressWarnings("unused")
				public void receive(String message) {
					c.add(message);
				}
			});

			// firing dummy event
			eventScope.dispatch("Hello, receivers!");

			// exception not thrown
			fail();
		} catch (Exception ise) {

			assertSame(DispatchException.class, ise.getClass());

			// assert that the second receiver has not been called
			assertEquals(0, c.size());
		}
	}

	@Test
	public void testSuperClassRegistration() {

		// register a receiver upon the interface
		DummyReceiver interfaceReceiver = new DummyReceiver();
		eventScope.register(IDummyEvent.class, interfaceReceiver);

		// register a receiver upon the implementation
		DummyReceiver implReceiver = new DummyReceiver();
		eventScope.register(DummyEventImpl.class, implReceiver);

		//
		// dispatch an impl event
		eventScope.dispatch(new DummyEventImpl());

		// assert impl calls
		// expected: both receivers received the event once
		assertEquals(1, implReceiver.received);
		assertEquals(1, interfaceReceiver.received);

		//
		// dispatch an abstract adaptor event
		eventScope.dispatch(new IDummyEvent() {
		});

		// expectation: only the interface receiver received the event.
		assertEquals(2, interfaceReceiver.received);
		assertEquals(1, implReceiver.received);

	}

}
