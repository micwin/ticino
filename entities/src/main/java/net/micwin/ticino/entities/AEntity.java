package net.micwin.ticino.entities;

import java.util.UUID;

/**
 * A helper class to simplify creation of ticino entities using inheritance.
 * 
 * @author micwin
 */
public abstract class AEntity implements IEntity {

	EntitySupport fSup = new EntitySupport();

	@Override
	public UUID getPhysicalId() {

		return fSup.getPhysicalId();
	}

	@Override
	public int getStateCounter() {

		return fSup.getStateCounter();
	}

	/**
	 * Raises the state counter by 1 and returns its new value.
	 * 
	 * @return new value after being risen.
	 */
	protected int raiseStateCounter() {

		return fSup.raiseStateCounter();
	}

	/**
	 * Raises the state counter by a given amopunt and return its new value.
	 * 
	 * @param pAmount
	 * @return
	 */
	protected int raiseStateCounter(final int pAmount) {

		return fSup.raiseStateCounter(pAmount);
	}

}
