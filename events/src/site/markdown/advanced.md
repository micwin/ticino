# event inheritance

Say we have a general event containing logic and data common to all of your events, ...
    
	class GeneralEvent {
		// some general stuff
	}

... an event containing data for a specific use case and extending GeneralEvent, ...

	class SpecificEvent extends GeneralEvent {
		// some use case specific data
	}

... a receiver triggered by the general event, ...
    
	class Receiver {
	    public void receive(GeneralEvent event) {
	        System.out.println("got event of " + event.getClass());
	    }
	}

... a event scope definition
   
	EventScope<GeneralEvent> eventScope = new EventScope <GeneralEvent> (GeneralEvent.class) ; 
   
... some main method bringing event and receiver together (normally you would do that in a spring context) ...
    
	public static void main(String[] args) {
	    eventScope.register(GeneralEvent.class, new Receiver());
	}

... then you could dispatch a specific event like that ...
    

	// fire specific event
	Specific event evt = new SpecificEvent();
	eventScope.dispatch(evt);


... and finally get an output like that :

	got event of class net.micwin.examples.inheritance.Main$SpecificEvent   