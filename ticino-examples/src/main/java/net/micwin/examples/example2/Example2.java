package net.micwin.examples.example2;

import java.util.LinkedList;
import java.util.List;

import net.micwin.ticino.Ticino;

public class Example2 {

	public static void main(String[] args) {
		// first, wire a list with Ticino
		List logReceiver = new LinkedList () ; 
		Ticino.register (String.class, logReceiver) ;

		// somewhere else, fire something to log
		Ticino.dispatch ("Hello, world!") ; 
		 
		// print out the results
		System.out.println (logReceiver) ; 

	}

}
