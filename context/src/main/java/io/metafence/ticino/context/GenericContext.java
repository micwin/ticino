
package io.metafence.ticino.context;

import java.util.*;
import java.util.function.Predicate;

/**
 * Default implementation for a IReadableModifyableContext that can hold an
 * infinite number of elements in memory and can validate elements..
 *
 * @param <ElementType>
 */
public class GenericContext<ElementType> implements IReadableModifyableContext<ElementType> {

	LinkedList<ElementType> fElements;

	/**
	 * Create a default context without elements and no validator.
	 */
	public GenericContext() {

		fElements = new LinkedList<ElementType>();
	}

	/**
	 * Create a DefaultContext and puts given elements in it.
	 * 
	 * @param pInitialElements
	 *            Initial elements.
	 */
	@SafeVarargs
	public GenericContext(final ElementType... pInitialElements) {

		fElements = new LinkedList<>();

		for (final ElementType lElement : pInitialElements) {
			fElements.add(lElement);
		}
	}

	/**
	 * Create a DefaultContext with given validator and puts given elements in
	 * it.
	 * 
	 * @param pInitialElements
	 */
	public GenericContext(final Iterable<ElementType> pInitialElements) {

		fElements = new LinkedList<>();

		for (final ElementType lElement : pInitialElements) {
			fElements.add(lElement);
		}

	}

	/**
	 * Looks up elements that match given predicate and returns them in a newly
	 * created DefaultContext.
	 * 
	 * @param pPredicate
	 *            The criteria the elements must match to be found.
	 */
	@Override
	public GenericContext<ElementType> lookup(final Predicate<ElementType> pPredicate) {

		return lookup(pPredicate, Integer.MAX_VALUE, new GenericContext<ElementType>());
	}

	/**
	 * Looks up elements that match given predicate and returns them in given
	 * {@link IModifyableContext}.
	 * 
	 * @param pPredicate
	 *            The criteria the elements must match to be found.
	 * @param pTarget
	 *            Where the found elements were put into.
	 */

	@Override
	public <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup(
			final Predicate<ElementType> pPredicate, final TargetContextType pTarget) {

		return lookup(pPredicate, Integer.MAX_VALUE, pTarget);
	}

	/**
	 * Looks up elements that match given predicate and returns them in a newly
	 * created DefaultContext.
	 * 
	 * @param pPredicate
	 *            The criteria the elements must match to be found.
	 * @param pMaxCount
	 *            If lookup would return more elements, this limits the max
	 *            number of elements looked up.
	 */
	@Override
	public GenericContext<ElementType> lookup(final Predicate<ElementType> pPredicate, final int pMaxCount) {

		return lookup(pPredicate, pMaxCount, new GenericContext<>());
	}

	/**
	 * Looks up elements that match given predicate and returns them in given
	 * {@link IModifyableContext}.
	 * 
	 * @param pPredicate
	 *            The criteria the elements must match to be found.
	 * @param pMaxCount
	 *            If lookup would return more elements, this limits the max
	 *            number of elements looked up.
	 * @param pTarget
	 *            Where the found elements were put into.
	 */
	@Override
	public <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup(
			final Predicate<ElementType> pPredicate, final int pMaxCount, final TargetContextType pTarget) {

		if (pMaxCount > 0) {
			for (final ElementType lElement : fElements) {
				if (pPredicate.test(lElement)) {
					pTarget.put(lElement);
				}
				if (pTarget.getCurrentElementCount() >= pMaxCount) {
					break;
				}
			}

		}
		return pTarget;
	}

	@Override
	public int getMaxStructureSize() {

		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxTheoreticalSize() {

		return Integer.MAX_VALUE;
	}

	@Override
	public int getCurrentElementCount() {

		return fElements.size();
	}

	@Override
	public GenericContext<ElementType> put(final ElementType pElement) {

		fElements.add(pElement);
		return this;
	}

	@Override
	public GenericContext<ElementType> putAll(final IReadableContext<ElementType> pElements) {

		for (final ElementType lElement : pElements) {
			put(lElement);
		}
		return this;
	}

	/**
	 * Creates an iterator over the current set of elements. Does not adjust if
	 * the contents of Context changes, nor does throw an Exception. See
	 * {@link Iterator} and {@link Iterable} for more information.
	 * 
	 * @see Iterator
	 * @see Iterable
	 */
	@Override
	public Iterator<ElementType> iterator() {

		return Collections.unmodifiableList(fElements).iterator();
	}

}
