package net.micwin.examples.example1;

import net.micwin.ticino.Ticino;

public class XmlCaster {
	public void yep(String newXml) {
		Ticino.dispatch(new IncomingXmlEvent(newXml));
	}
}
