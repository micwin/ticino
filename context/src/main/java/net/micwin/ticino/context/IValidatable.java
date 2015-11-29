package net.micwin.ticino.context;

import java.util.function.Predicate;

/**
 * An item that is sort of a value and hence can be validated.
 *
 */
public interface IValidatable<ElementType> {

	/**
	 * Returns the current validator. may return null if no validator has been
	 * set.
	 *
	 * @return
	 */
	Predicate<ElementType> getValidator();

}
