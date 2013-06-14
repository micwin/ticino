package net.micwin.examples.inheritance;

import net.micwin.ticino.Ticino;

public class Main {

	static class GeneralEvent {
	}

	static class SpecificEvent extends GeneralEvent {
	}

	static class Receiver {
		public void receive(GeneralEvent event) {
			System.out.println("got event of " + event.getClass());
		}
	}

	public static void main(String[] args) {
		// register receiver to super class
		Ticino.register(GeneralEvent.class, new Receiver());

		// fire specific event
		Ticino.dispatch(new SpecificEvent());
		
		// you should get an output like
		// "got event of class net.micwin.examples.inheritance.Main$SpecificEvent"
	}

}
