package net.micwin.examples.theWeirdOne;

import net.micwin.ticino.Ticino;

/**
 * Example that does all in two lines
 * 
 * @author MicWin
 * 
 */
public class Main {

	public static void main(String[] args) {

		Ticino.register(String.class, new Object() { public void yaho(String s) { System.out.println(s); } });
		Ticino.dispatch("hello, world!");

	}

}
