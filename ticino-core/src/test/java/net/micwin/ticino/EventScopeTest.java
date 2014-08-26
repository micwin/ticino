
package net.micwin.ticino;


import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings({ "unchecked", "rawtypes" })
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
         * Dont do this (using Object as event type) in real life since the method then might get shadowed by other
         * methods not associated with event handling.
         * 
         * @param evt
         */
        public void receive(final Object evt) {
            this.received++;
        }
    }

    @Before
    public void before() {
        this.eventScope = new EventScope("unitTest");
    }

    @Test
    public void testInit() {
        Assert.assertEquals("unitTest", this.eventScope.getScopeName());
        Assert.assertEquals(0, this.eventScope.receiverMap.size());
    }

    /**
     * This is a simple test with a mock event and mock receiver. It demonstrates the default event work flow:
     * registering, dsipatching, receiving.
     */
    @Test
    public void testWorkflow() {

        final DummyReceiver receiver = new DummyReceiver();
        this.eventScope.register(DummyEventImpl.class, receiver);

        this.eventScope.dispatch(new DummyEventImpl());
        Assert.assertEquals(1, receiver.received);
    }

    /**
     * This is an absurd test that perfectly demonstrates the absolute flexibility of ticino. In fact, this is absolute
     * pojo; you dont have to create event or payload classes if there already is something like an already programmed
     * event or payload class around, like in wicket, or JMS.
     * <p />
     * <strong>CAUTION!</strong> Using very basic types like Integers really only is advisable for demonstration
     * purposes. For instance, the problem comes with registration where *all* methods receiving a primitive int or an
     * Integer automatically become possible receiver methods. You dont want that.
     */
    @Test
    public void testAbsurd() {

        // create a container to put the payload in.
        final List<Integer> payload = new LinkedList<Integer>();

        // register an anonymous adapter as receiver, that puts the received
        // "event" (i.e. the Integer) into the list above.
        this.eventScope.register(Integer.class, new Object() {
            @SuppressWarnings("unused")
            public void receive(final Integer i) {
                // this is the receiver method.
                payload.add(i);
            }
        });

        // trigger receiver twice
        this.eventScope.dispatch(4);
        this.eventScope.dispatch(42);

        // check that both integers have been put into the list (and hence rthe
        // receivers have been processed).
        Assert.assertEquals((Integer) 4, payload.get(0));
        Assert.assertEquals((Integer) 42, payload.get(1));
    }

    @Test
    public void testSameClass() {

        try {
            this.eventScope.register(String.class, new Object() {

                // receiver that has two methods that possibly could catch the
                // event
                @SuppressWarnings("unused")
                public void receive1(final String evt) {

                }

                @SuppressWarnings("unused")
                public void receive2(final String evt) {}
            });

            Assert.fail("illegal receiver accepted");

        }
        catch (final IllegalArgumentException iae) {
            // w^5

        }
    }

    @Test
    public void testClassInSameHierarchy() {

        try {
            this.eventScope.register(String.class, new Object() {

                // receiver that has two methods that possibly could catch the
                // event
                @SuppressWarnings("unused")
                public void receive1(final String evt) {

                }

                @SuppressWarnings("unused")
                public void receive2(final Object evt) {}
            });

            Assert.fail("illegal receiver accepted");

        }
        catch (final IllegalArgumentException iae) {
            // w^5

        }
    }

    @Test
    public void testReceiverThrowingException() {
        final Collection<String> c = new LinkedList<String>();
        try {

            // register first receiver, throwing an exception.
            this.eventScope.register(String.class, new Object() {
                @SuppressWarnings("unused")
                public void receive(final String message) throws IOException {
                    throw new IOException();
                }
            });
            // register second receiver, expecting work to do (but not reached)
            this.eventScope.register(String.class, new Object() {
                @SuppressWarnings("unused")
                public void receive(final String message) {
                    c.add("second");
                }
            });

            // firing dummy event
            this.eventScope.dispatch("Hello, receivers!");

            // exception not thrown
            Assert.fail();
        }
        catch (final Exception ise) {

            Assert.assertSame(DispatchException.class, ise.getClass());

            // assert that the second receiver has not been called
            Assert.assertEquals(0, c.size());
        }
    }

    @Test
    public void testRegister_with_pattern() {

        final List<String> receiver = new LinkedList<String>();

        this.eventScope.register(String.class, receiver, Pattern.compile("add$"));

        this.eventScope.dispatch("1234");

        Assert.assertEquals(1, receiver.size());

        Assert.assertEquals("1234", receiver.get(0));

    }

    @Test
    public void testSuperClassRegistration() {

        // register a receiver upon the interface
        final DummyReceiver interfaceReceiver = new DummyReceiver();
        this.eventScope.register(IDummyEvent.class, interfaceReceiver);

        // register a receiver upon the implementation
        final DummyReceiver implReceiver = new DummyReceiver();
        this.eventScope.register(DummyEventImpl.class, implReceiver);

        //
        // dispatch an impl event
        this.eventScope.dispatch(new DummyEventImpl());

        // assert impl calls
        // expected: both receivers received the event once
        Assert.assertEquals(1, implReceiver.received);
        Assert.assertEquals(1, interfaceReceiver.received);

        //
        // dispatch an abstract adaptor event
        this.eventScope.dispatch(new IDummyEvent() {});

        // expectation: only the interface receiver received the event.
        Assert.assertEquals(2, interfaceReceiver.received);
        Assert.assertEquals(1, implReceiver.received);

    }

    @Test
    public void testDoubleCallError() {

        final List hits = new LinkedList();
        final Object listener = new Object() {
            public void process(final List evt) {
                hits.add("hit");
            }
        };
        this.eventScope.register(List.class, listener);
        this.eventScope.register(LinkedList.class, listener);

        this.eventScope.dispatch(new LinkedList());
        Assert.assertEquals(1, hits.size());

    }

}
