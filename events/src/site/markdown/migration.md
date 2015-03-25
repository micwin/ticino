# migration : general approach 

being reflection based instead of subclassing and annotating, there is a big migration path from or to ticino events.
Lets demonstrate the migration in case of wicket - which runs nearly out-of-the-box:
  
	public class WicketApplication extends WebApplication {
	
	  @Autowired
	  EventScope <MyPayload> eventScope = new EventScope<MyPayload>(MyPayload.class) ;
	
	  @Override
	  public void onEvent(IEvent<MyPayLoad> event) {
		// keep the wicket way functioning
		super.onEvent(event);
		
		// but also support strict typed ticino event handling
		eventScope.dispatch (event.getPayload()) ;
	  }
	}

   
Somewhere else in the code ...
   
	public class NoSink {
	
	  @Autowired
	  EventScope <MyPayload> eventScope = new EventScope<MyPayload>(MyPayload.class) ;
	   
	   public NoSink () {
	     eventScope.register (MyPayLoad.class , this) ; 
	   }
	   
	   public void blablah (MyPayLoad payload) {
	     // doSomething meaningful
	   }
	   
	}

Although there are methods to do so, you dont have to unregister - ticino does this for you if the receiver gets gc'ed.