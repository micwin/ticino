package net.micwin.examples.example1;

public class Receiver {
	public void doSomethingWithThisEvent(IncomingXmlEvent incomingXml) {
		System.out.println("incoming xml : " + incomingXml.getXml());
	}
}