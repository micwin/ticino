# Welcome to the ticino events module!

ticino events is a lightweight java library that adresses the problem of 
decoupling dependent modules from each other. It introduces an 
intermediary _EventScope_, a _DispatchException_ and an optional _IPostprocessor_.


## basics

each distinct event is represented by a corresponding class:

	// a marker interface as some sort of base event
	public interface IEvent {
	}

	// an event signaling that we connected to a remote entity
	public class Connected implements IEvent {
	}
	
	// an event signaling we disconnected again
	public class Disconnected implements IEvent {
	}
	
	// we're going down
	public ShutDown implements IEvent {
	}
	
The receiver then looks as follows:

	public class ReceiverHandler {
	
		public ReceiverHandler (EventScope <IEvent> pEventScope) {
		
			// register to what we want to receive
			pEventScope.register (Connected.class, this); 
			pEventScope.register (Disconnected.class,this) ; 
			pEventScope.register(ShutDown.class, this) ; 
			
			// of course you donnot have to register to all events, just these
			// you can and/or want to handle
		}

		public void yeeha (Connected pEvent) {
			System.out.println ("We are connected arent we?" ) ;
		}
		
		public void handle (Disconnected pEvent) {
			System.out.println ("Not connected any more" ) ;
		}
		
		public void down (ShutDown pEvent) {
			System.out.println ("This is ending soon" ) ;
		}
	}

### method names?
ticino does not care about method names, only about parameter types, 
which correspond with distinct event types.

The method has to be accessible, that is public, non- abstract, 
non-virtual. And there only has to be exactly _one_ method with accepts each 
distinct event types. Otherwise ticino events does not know where to put 
the event to and cancels registration with an _IllegalArgumentException_.

More about the method name: you can choose a method name that is approppriate 
for this class or the module this class is in. For instance, you could amplify 
the fact that here happens event handling by choosing method naming like 
_doEvent(...)_ or _process(...)_; if the method is part of a final class, you simply
 could use one of the methods already defined. In fact, there _is_ a way of 
 delegating events to a _LinkedList_, a _TreeSet_ or even a _StringBuffer_.
 
 
## the dispatcher
a dispatcher has to have a reference to  event scope and  ability to create an event instance:

	private class Dispatcher {
	
		@AutoWired
		EventScope <IEvent> fEventScope ; 
	
		public Dispatcher (EventScope<IEvent> pEventScope) {
			fEventScope = pEventScope ; 
		}
		
		public void connect() {
		
			... connecting ...
			
			fEventScope.dispatch (new Connected() ) ; 
			
			// no dependencies or direct handle to the receiver ;)
		}
		
		public void disconnect() {
		
			... disconnecting ...
			
			fEventScope.dispatch (new Disconnected() ) ; 
			// again, no dependencies or direct handle to the receiver ;)
		}
		
		
		public void shutdown() {
		
			// first disconnect
			disconnect () ;
			
			// then shut down
			
			fEventScope.dispatch (new ShutDown() ) ; 
		}
		
	}

## return values

You surely could implement a kind of return value and use that as a sort of processing trigger:

	public class PingEvent {
		boolean fProcessed
		
		public void markProcessed() {
			fProcessed = true ; 
		}
		
		public boolean isProcessed () {
			return fProcessed ; 
		}
	}

A receiver ...

	public class Receiver {
		...		
		public void process (Ping pEvent) {
			if (!pEvent.isProcessed() {
				// do some magic
				pEvent.markProcessed() ; 
			}
		}
	}
	
A dispatcher and some output ...

	public void doSomething() {

		System.out.prinln ("Ping : fPingScope.dispatch(new Ping()).isProcessed()) ; 
	
	}
		
_EventScope.dispatch_ returns the event itself so, if you see above, chaining is quite handy when processing return values.
	
## some notes
	
- the check wether a receiver has the appropriate handler method is done at runtime, when EventScope.register is called. 
A very elaborate, clear and loud exception is thrown when a problem occurrs. 

- ticino events uses soft references. If listeners get garbage collected, 
ticino events also will remove them, so make sure your listeners will not 
vanish because of sole purpose listenijg to ticino. Use handler types instead 
and store them in the class that instantiates them.

- a good place  to instantiate event scopes is a main method, or (as implied above) a spring context.

- you could also dispatch asynchronously. Please see documentation of 
EventScope.dispatchAsynchronously() for that.

- in above example, you also could register to IEvent; in that case, you 
would get all dispatched events implementing IEvent and have to do the type check by yourself, in the handler.

- If you register both to the interface and the explicit classes, you will get
notified twice for each explicit class. if you register multiple times to the 
same event, you will get notified multiple times.

- of course you could add some state information to the event classes. 
In fact, this is the intended use - but beware to not store sensitive or big scale data, 
and keep in mind that event classes are somewhat volatile.

- every exception thrown by a receiver is wrapped in a DispatchException, which
is a RuntimeException. Hence, since you do have to mess around with runtime 
exceptions, you are not limited in selecting the throws clause of the receiver
method. If your receiver method does not want to mess around with an IOException, 
just add a throws IOException to the method header of receiving method and let the dispatcher handle that.

- ticino events does not limit the design of event classes nor analyze or call any methods
nor proxy or wrap them in whatsoever. If you already have a method or event class from 
another events framework, it is abolutely perfect (from a ticino events 
view) to use them with ticino.

- donot put any algorithms or logic into the event classes  - these should be sole value 
or holders of temporary states.

- Again - although ticino events does not limit that, make sure to not incorporate 
sensitive data in your events or, if you have to, make sure no one not intended 
gets a grip on the corresponding event scope.

- you surely could use primitives as events as well. using strings surely 
is a good choice if the decision which string means what event should be done 
by the receiver itself - or if you simply use ticino as a means of simple intra-vm data 
transport multiplexer.

- be careful when dispatching an event while handling another one. Could 
result in a deadlock. When you have to, consider using _EventScope.dispatchAsynchronously_ when dispatching inside the call stack of an event handler.