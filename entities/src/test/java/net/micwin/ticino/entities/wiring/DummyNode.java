package net.micwin.ticino.entities.wiring;

/**
 * A dummy class working as a entity for unit testing..
 * 
 * @author micwin
 *
 */
public class DummyNode {

	public String fName;
	public int fIntegerValue;
	public double fDoubleValue;

	public DummyNode() {
	}

	public DummyNode(String pName, int pIntegervalue, double pDoubleValue) {
		this.fName = pName;
		this.fIntegerValue = pIntegervalue;
		this.fDoubleValue = pDoubleValue;
	}

}
