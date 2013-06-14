package net.micwin.examples.example1;

import net.micwin.ticino.Ticino;

public class Main {
	public static void main(String[] args) {
		Receiver rec = new Receiver();
		Ticino.register(IncomingXmlEvent.class, rec);
		// yes, there *is* spring support
	}

}
