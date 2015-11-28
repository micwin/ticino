# Welcome to the ticino events module!

the ticino events module is java library that adresses the problem of 
decoupling dependent modules from each other. It introduces an 
intermediary _EventScope_.

## former times

Normally, you would code somethign like this : 
	
	import receiver.package.ReceiverClass
	import dispatcher.package.DispatcherClass
	
	...
	
	ClassName receiver = ...
	
	... 
	
	ClassName dispatcher = ...
	
	...
	
	receiver.notify (param1, param2, param3) ; 

Short and small, isn't it? But there are a couple of problems with that:

- if signature of the notify method changes (adding/removing parameters, or 
renaming method), you have to adjust both dispatching and receiving code.
- the dispatcher has to have an instance of the receiver and can do with that 
whatever it likes - calling other methods that alter state, or attach interceptors, for instance.
- if the event has to be delegated to another receiver, the responsibility is unclear. 
- The responsibility in case of an error is also unclear
- dispatcher has to follow naming aproach given by the notify method, no 
  matter what naming aproach is common in the context of the dispatcher.
  
hence, although this aproach looks like good practice, it in fact breaks 
encapsulation on an abstract level. 

## the ticino events approach

in ticino events, each distinct event is represented by a corresponding class:

	// for instance, we could use a marker interface as some sort of base
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

	public class Receiver {
	
		public Receiver (EventScope <IEvent> pEventScope) {
		
			// register to what we want to receive
			pEventScope.register (Connected.class, this); 
			pEventScope.register (Disconnected.class,this) ; 
			pEventScope.register(ShutDown.class) ; 
			
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
	
ticino does not care about method names, only about parameter types, 
which correspond with distinct event types.

THe method, though, has to be accessible, that is public and non- abstract, 
non-virtual. And there only has to be exactly _one_ method with accepts each 
distinct event types. Otherwise ticino events does not know where to put 
the event to.

More about the method name: you can choose a method name that is approppriate 
for this class or the module this class is in. For instance, you could amplify 
the fact that here happens event handling by choosing method naming like 
_doEvent()_ or _process()_; if the method is part of a final class, you simply
 could use one of the methods already defined. In fact, there _is_ a way of 
 delegating events to a _LinkedList_, a _TreeSet_ or even a _StringBuffer_ but 
 I won't ya - go find it by yourself ;)
 
Back to the basics;  a dispatcher has to have a reference to the event scope and the ability to create an event instance:

	private class Dispatcher {
	
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
	
A dispatcher ...

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

- a good place  to instantiate event scopes is a main method, or a spring context.

- you could also dispatch asynchronously. Please see documentation of 
EventScope.dispatchAsynchronously() for that.

- in above example, you also could register to IEvent; in that case, you 
would get all dispatched events implementing IEvent.
If you register both to the interface and the explicit classes, you will get
notified twice for each explicit class. if you register multiple times to the 
same event, you will get notified multiple times.

- of course you could add some state information to the event classes. 
In fact, this is the intended use!

- every exception thrown by a receiver is wrapped in a DispatchException, which
is a RuntimeException. Hence, since you do have to mess around with runtime 
exceptions, you are not limited in selecting the throws clause of the receiver
method. If your eceiver method does not want to mess around witn an IOException, 
just add a throws IOException to the method header and let the dispatcher handle that.

- ticino events does not limit the design of event classesnor analyze or call any methods
nor proxy or wrap them in whatsoever. If you already have a method or event class from 
another events framework, it is abolutely perfect (from a ticino events 
view) to use them with ticino.

- donot put any algorithms or logic into the event classes. these should be sole value 
or state holders.

- Although ticino events does not limit that, make sure to not incorporate 
sensitive data in your events or, if you have to, make sure no one not intended 
gets a grip on the corresponding event scope.

- you surely could use primitives as events as well. using strings surely 
is a good choice if the decision which string means what event should be done 
by the receiver itself - or if you simply use ticino as a means of simple intra-vm data 
transport multiplexer.

- be careful when dispatching an event while handling another one. Could 
result in a deadlock. When you have to, consider using _EventScope.dispatchAsynchronously_ when dispatching inside
a event handler.