# the basics

to use events, we need at least

## mediator

the class _net.micwin.ticino.events.EventScope_ serves as mediator between event sender and receiver and transports events from one code snippet to another.

	EventScope <IFlightData> fFlightDataScope = new EventScope <IFlightData> (IFlightData.class) ; 

## event class

all data of a specific event is encapsulated in a dedicated java class:

	public class IncomingUpdateEvent implements IFlightData {
		
		private final String fFlightDataXml
		private Object fResult ; 
		
		public IncomingUpdateEvent (String pFlightDataXml) {
			fFlightDataXml = pFlightDataXml ; 
		}
		
		public Object getResult() {
			return fResult ; 
		}
		
		public void setResult (Object pResult) {
			if (fResult != null) {
				throw new IllegalArgumentException ("result value already set") ;
			}
			fResult = pResult ; 
		}
	}
	
* a descriptive name (_IncomingUpdateEvent_) tells what happened in domain language
* constructor parameters and immutable/final fields (_pFlightDataXml, fFlightDataXml_) serve as context information and additional event data.
* mutable fields (_fResult_) serve as results holder - information that flows from receiver back to dispatcher.

## dispatcher

the dispatcher is the source of a specific event.

	public class FlightDataService {

		public void doSomething(String pFlightDataXml) {

			// create event		
			IncomingUpdateEvent lUpdateEvent = new IncomingUpdateEvent (pFlightDataXml) ;
			
			// dispatch 
			fFlightDataScope.dispatch(lUpdateEvent) ; 
		}
	}
	
If you prefer chaining, you might prefer this solution : 

			...
			// create and dispatch event 
			IncomingUpdateEvent lEvent = fFlightDataScope.dispatch(new IncomingUpdateEvent (pFlightDataXml)) ;
			... 

## receiver

the receiver is the place where events will land. there can be multiple receivers listening for the same event.

A receiver has to provide a public method with a single parameter of the event type. 

	public class ProcessDao {
	
		public void add (IncomingUpdateEvent pEvent) {
			fEntityManager.store (pEvent) ; 
		}
	}
	
As you see, _ticino events_ fits perfectly to separate layers from each other - here:  to separate web service layer from persistence layer without mangling dependencies or passing instances directly. 

## configurator

The configurator is the place that initializes and passes required instances to each other. For example, you could wire all the stuff in a main method

	public class Launcher {
	
		public static void main (String [] pArgs) {

			// declare scope
			EventScope <IFlightData> fFlightDataScope = new EventScope <IFlightData> (IFlightData.class) ;
			
			// create receiver
			ProcessDao lProcessDao = ...
			
			// register receiver
			fFlightDataScope.register (IncomingUpdateEvent.class, lProcessDao) ;
			
			// register sort of event logger with a name pattern matcher
			List <IFlightData> lEventLogger = new LinkedList <IFlightData> () ; 
			fFlightDataScope.register (IFlightData.class, lEventLogger , Pattern.compile("add")) ;
			
			// create service interface
			FlightDataService lFlightDataService = new FlightDataService (fFlightDataScope) ;
					
			// ... do something meaningful
			lFlightDataService.doSomething ("this is not an xml string") ; 
			
			// show all events that have been properly processed.
			System.out.println ("processed events:") ; 
			for (IFlightData lEvent : lEventLogger) {
				System.out.println ("received event : " + lEvent) ;
			}
		}
	}
	
## wiring with spring
	
When using spring, you need some appropriate bean definitions in application context ...

	<!-- declare flight data scope bean -->
	<bean name= "flightDataScope" class="net.micwin.ticino.events.EventScope">
		<constructor-arg value="flight.data.IFlightData" />
	</bean>
	
	<!-- declare receiver -->
	<bean name="processDao" class="flight.data.ProcessDao" />
	<bean name="eventLogger" class="java.util.LinkedList" />

You also have to declare a helper to register receivers to the appropriate scopes:

	<bean class="flight.data.spring.Configurator">
		<constructor-arg ref="flightDataScope" />
		<constructor-arg ref="processDao" />
		<constructor-arg ref="eventLogger" />
	</bean>
	
The class:

	public class Configurator implements InitializingBean {
	
		private ProcessDao fProcessDao ; 
		EventScope <IFlightData> fFlightDataScope ;
		List<IFlightData> fEventLogger ; 
		
		public Configurator (EventScope <IFlightData> pFlightDataScope , ProcessDao pProcessDao, List<IFlightData> pEventLogger) {
			fProcessDao = pProcessDao ; 
			fFlightDataScope = pFlightDataScope ;
			fEventLogger = pEventLogger ; 
		}
		
		// this, ironically, is a spring event that perfectly fits in this purpose
		@Override
		public void afterPropertiesSet() {
		
			// wire dao
			fFlightDataScope.register (IncomingUpdateEvent.class, fProcessDao) ;
			
			// wire event logger
			fFlightDataScope.register (IFlightData.class, fEventLogger , Pattern.compile("add")) ;
		}
	}
	
Yes, sooner or later there will be a more intuitive spring support.