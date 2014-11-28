
package net.micwin.ticino ;

import static org.junit.Assert.* ;

import java.io.IOException ;
import java.util.* ;
import java.util.regex.Pattern ;

import org.junit.Test ;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class EventScopeTest {

    /**
     * An interface for a dummy event
     *
     * @author MicWin
     */
    interface IDummyEvent {

    }

    /**
     * A mock dummy event.
     *
     * @author MicWin
     */
    class DummyEventImpl implements IDummyEvent {

    }

    /**
     * A dummy receiver that receives the dummy event.
     */
    class DummyReceiver {

        private int received ;

        /**
         * Dont do this (using Object as event type) in real life since the
         * method then might get shadowed by other
         * methods not associated with event handling.
         *
         * @param evt
         */
        public void receive (final Object evt) {

            this.received++ ;
        }
    }

    @Test
    public void testInit () {

        final EventScope<IDummyEvent> eventScope = new EventScope<EventScopeTest.IDummyEvent> (IDummyEvent.class) ;
        assertEquals (IDummyEvent.class , eventScope.getBaseClass ()) ;
        assertEquals (0 , eventScope.receiverMap.size ()) ;
    }

    /**
     * This is a simple test with a mock event and mock receiver. It
     * demonstrates the default event work flow:
     * registering, dsipatching, receiving.
     */
    @Test
    public void testWorkflow () {

        final EventScope<IDummyEvent> eventScope = new EventScope<EventScopeTest.IDummyEvent> (IDummyEvent.class) ;
        final DummyReceiver receiver = new DummyReceiver () ;
        eventScope.register (DummyEventImpl.class , receiver) ;

        eventScope.dispatch (new DummyEventImpl ()) ;
        assertEquals (1 , receiver.received) ;
    }

    /**
     * This is an absurd test that perfectly demonstrates the absolute
     * flexibility of ticino. In fact, this is absolute
     * pojo; you dont have to create event or payload classes if there already
     * is something like an already programmed
     * event or payload class around, like in wicket, or JMS.
     * <p />
     * <strong>CAUTION!</strong> Using very basic types like Integers really
     * only is advisable for demonstration purposes. For instance, the problem
     * comes with registration where *all* methods receiving a primitive int or
     * an Integer automatically become possible receiver methods. You dont want
     * that.
     */
    @Test
    public void testAbsurd () {

        // create a container to put the payload in.
        final List<Integer> payload = new LinkedList<Integer> () ;

        // register an anonymous adapter as receiver, that puts the received
        // "event" (i.e. the Integer) into the list above.
        final EventScope<Integer> eventScope = new EventScope<Integer> (Integer.class) ;
        eventScope.register (Integer.class , new Object () {

            @SuppressWarnings("unused")
            public void receive (final Integer i) {

                // this is the receiver method.
                payload.add (i) ;
            }
        }) ;

        // trigger receiver twice
        eventScope.dispatch (4) ;
        eventScope.dispatch (42) ;

        // check that both integers have been put into the list (and hence rthe
        // receivers have been processed).
        assertEquals ((Integer) 4 , payload.get (0)) ;
        assertEquals ((Integer) 42 , payload.get (1)) ;
    }

    @Test
    public void testSameClass () {

        final EventScope<String> eventScope = new EventScope<String> (String.class) ;

        try {
            eventScope.register (String.class , new Object () {

                // receiver that has two methods that possibly could catch the
                // event
                @SuppressWarnings("unused")
                public void receive1 (final String evt) {

                }

                @SuppressWarnings("unused")
                public void receive2 (final String evt) {

                }
            }) ;

            fail ("illegal receiver accepted") ;

        } catch (final IllegalArgumentException iae) {
            // w^5

        }
    }

    @Test
    public void testClassInSameHierarchy () {

        final EventScope<String> eventScope = new EventScope<String> (String.class) ;

        try {
            eventScope.register (String.class , new Object () {

                // receiver that has two methods that possibly could catch the
                // event
                @SuppressWarnings("unused")
                public void receive1 (final String evt) {

                }

                @SuppressWarnings("unused")
                public void receive2 (final Object evt) {

                }
            }) ;

            fail ("illegal receiver accepted") ;

        } catch (final IllegalArgumentException iae) {
            // w^5

        }
    }

    @Test
    public void testReceiverThrowingException () {

        final EventScope<String> eventScope = new EventScope<String> (String.class) ;
        final Collection<String> c = new LinkedList<String> () ;
        try {

            // register first receiver, throwing an exception.
            eventScope.register (String.class , new Object () {

                @SuppressWarnings("unused")
                public void receive (final String message) throws IOException {

                    throw new IOException () ;
                }
            }) ;
            // register second receiver, expecting work to do (but not reached)
            eventScope.register (String.class , new Object () {

                @SuppressWarnings("unused")
                public void receive (final String message) {

                    c.add ("second") ;
                }
            }) ;

            // firing dummy event
            eventScope.dispatch ("Hello, receivers!") ;

            // exception not thrown
            fail () ;
        } catch (final Exception ise) {

            assertSame (DispatchException.class , ise.getClass ()) ;

            // assert that the second receiver has not been called
            assertEquals (0 , c.size ()) ;
        }
    }

    @Test
    public void testRegister_with_pattern () {

        final EventScope<String> eventScope = new EventScope<String> (String.class) ;
        final List<String> receiver = new LinkedList<String> () ;

        eventScope.register (String.class , receiver , Pattern.compile ("add$")) ;

        eventScope.dispatch ("1234") ;

        assertEquals (1 , receiver.size ()) ;

        assertEquals ("1234" , receiver.get (0)) ;

    }

    @Test
    public void testSuperClassRegistration () {

        // register a receiver upon the interface
        final EventScope<IDummyEvent> eventScope = new EventScope<EventScopeTest.IDummyEvent> (IDummyEvent.class) ;
        final DummyReceiver interfaceReceiver = new DummyReceiver () ;
        eventScope.register (IDummyEvent.class , interfaceReceiver) ;

        // register a receiver upon the implementation
        final DummyReceiver implReceiver = new DummyReceiver () ;
        eventScope.register (DummyEventImpl.class , implReceiver) ;

        //
        // dispatch an impl event
        eventScope.dispatch (new DummyEventImpl ()) ;

        // assert impl calls
        // expected: both receivers received the event once
        assertEquals (1 , implReceiver.received) ;
        assertEquals (1 , interfaceReceiver.received) ;

        //
        // dispatch an abstract adaptor event
        eventScope.dispatch (new IDummyEvent () {
        }) ;

        // expectation: only the interface receiver received the event.
        assertEquals (2 , interfaceReceiver.received) ;
        assertEquals (1 , implReceiver.received) ;

    }

    @Test
    public void testDoubleCallError () {

        final EventScope<List> eventScope = new EventScope<List> (List.class) ;
        final List hits = new LinkedList () ;
        final Object listener = new Object () {

            public void process (final List evt) {

                hits.add ("hit") ;
            }
        } ;
        eventScope.register (List.class , listener) ;
        eventScope.register (LinkedList.class , listener) ;

        eventScope.dispatch (new LinkedList ()) ;
        assertEquals (1 , hits.size ()) ;

    }

    @Test
    public void testUnregister_simple () {

        final EventScope<String> eventScope = new EventScope<String> (String.class) ;
        final List<String> touches = new LinkedList<String> () ;

        final Object receiver = new Object () {

            public void process (final String event) {

                touches.add (event) ;
            }
        } ;

        // first, check wether it's receiving
        eventScope.register (String.class , receiver) ;
        eventScope.dispatch ("A") ;
        assertTrue (touches.contains ("A")) ;

        touches.clear () ;

        // unregister
        eventScope.unregisterListener (receiver) ;
        eventScope.dispatch ("B") ;

        assertEquals (0 , touches.size ()) ;

    }

    @Test
    public void testUnregister_three () {

        final EventScope<String> eventScope = new EventScope<String> (String.class) ;
        final List<String> touches = new LinkedList<String> () ;

        final Object receiver1 = new Object () {

            public void process (final String event) {

                touches.add ("A") ;
            }
        } ;
        eventScope.register (String.class , receiver1) ;

        final Object receiver2 = new Object () {

            public void process (final String event) {

                touches.add ("B") ;
            }
        } ;
        eventScope.register (String.class , receiver2) ;

        final Object receiver3 = new Object () {

            public void process (final String event) {

                touches.add ("C") ;
            }
        } ;
        eventScope.register (String.class , receiver3) ;

        eventScope.unregisterListener (receiver2) ;

        eventScope.dispatch ("E") ;
        assertTrue (touches.contains ("A")) ;
        assertTrue (touches.contains ("C")) ;

        assertEquals (2 , touches.size ()) ;

    }

    @Test
    public void testUnregister_hierarchy () {

        final EventScope<Number> eventScope = new EventScope<Number> (Number.class) ;
        final List<String> touches = new LinkedList<String> () ;

        final Object receiver1 = new Object () {

            public void process (final Number event) {

                touches.add ("A") ;
            }
        } ;
        eventScope.register (Integer.class , receiver1) ;

        final Object receiver2 = new Object () {

            public void process (final Number event) {

                touches.add ("B") ;
            }
        } ;
        eventScope.register (Integer.class , receiver2) ;
        eventScope.register (Number.class , receiver2) ;

        final Object receiver3 = new Object () {

            public void process (final Integer event) {

                touches.add ("C") ;
            }
        } ;
        eventScope.register (Integer.class , receiver3) ;

        eventScope.unregisterListener (receiver2) ;

        eventScope.dispatch (new Integer (4)) ;

        // assert
        assertTrue (touches.contains ("A")) ;
        assertTrue (touches.contains ("C")) ;

        assertEquals (2 , touches.size ()) ;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeCheck () {

        final EventScope evtScope = new EventScope (Number.class) ;

        evtScope.dispatch ("Hello, World!") ;
        fail ("No exception thrown!") ;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull () {

        final EventScope<Object> scope = new EventScope<Object> () ;

        scope.dispatch ((Object) null) ;
    }

}
