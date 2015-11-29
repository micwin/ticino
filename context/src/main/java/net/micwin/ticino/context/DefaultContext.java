
package net.micwin.ticino.context ;

import java.util.* ;
import java.util.function.Predicate ;

/**
 * Default implementation for a IReadableModifyableContext that can hold an
 * infinite number of elements in memory and can validate elements..
 *
 * @param <ElementType>
 */
public class DefaultContext<ElementType> implements IReadableModifyableContext<ElementType> , IValidatable<ElementType> {

    LinkedList<ElementType>        fElements ;
    private Predicate<ElementType> fPutValidator ;

    /**
     * Create a default context without elementsd and no validator.
     */
    public DefaultContext () {

        this (null , new LinkedList<> ()) ;
    }

    /**
     * Creates a new DefaultContext and puts given elements nto it.
     * 
     * @param pInitialElements
     *            Initial elements.
     */
    @SafeVarargs
    public DefaultContext (final ElementType ... pInitialElements) {

        this (null , Arrays.asList (pInitialElements)) ;
    }

    /**
     * Create a DefaultContext with given validator and puts givene elements in
     * it. Does validate elements.
     * 
     * @param pPutValidator
     *            A validator that validates elements that are put into the
     *            context via methods {@link #put(Object)} and
     *            {@link #putAll(IReadableContext)}.
     * @param pInitialElements
     *            Initial elements.
     */
    @SafeVarargs
    public DefaultContext (final Predicate<ElementType> pPutValidator, final ElementType ... pInitialElements) {

        this (pPutValidator , Arrays.asList (pInitialElements)) ;
    }

    /**
     * Creates a DefaultContext without validator and puts given elements into
     * it.
     * 
     * @param pInitialElements
     */
    public DefaultContext (final Collection<ElementType> pInitialElements) {

        this (null , pInitialElements) ;
    }

    /**
     * Create a DefaultContext with given validator and puts givene elements in
     * it. Does validate elements.
     * 
     * @param pPutValidator
     *            A validator that validates elements that are put into the
     *            context via methods {@link #put(Object)} and
     *            {@link #putAll(IReadableContext)}.
     * @param pInitialElements
     */
    public DefaultContext (final Predicate<ElementType> pPutValidator, final Iterable<ElementType> pInitialElements) {

        fElements = new LinkedList<> () ;
        fPutValidator = pPutValidator ;

        for (final ElementType lInitialElement : pInitialElements) {
            put (lInitialElement) ;
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
    public DefaultContext<ElementType> lookup (final Predicate<ElementType> pPredicate) {

        return lookup (pPredicate , Integer.MAX_VALUE , new DefaultContext<ElementType> ()) ;
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
    public <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (
            final Predicate<ElementType> pPredicate , final TargetContextType pTarget) {

        return lookup (pPredicate , Integer.MAX_VALUE , pTarget) ;
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
    public DefaultContext<ElementType> lookup (final Predicate<ElementType> pPredicate , final int pMaxCount) {

        return lookup (pPredicate , pMaxCount , new DefaultContext<> ()) ;
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
    public <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (
            final Predicate<ElementType> pPredicate , final int pMaxCount , final TargetContextType pTarget) {

        if (pMaxCount > 0) {
            for (final ElementType lElement : fElements) {
                if (pPredicate.test (lElement)) {
                    pTarget.put (lElement) ;
                }
                if (pTarget.getCurrentElementCount () >= pMaxCount) {
                    break ;
                }
            }

        }
        return pTarget ;
    }

    @Override
    public int getMaxStructureSize () {

        return Integer.MAX_VALUE ;
    }

    @Override
    public int getMaxTheoreticalSize () {

        return Integer.MAX_VALUE ;
    }

    @Override
    public int getCurrentElementCount () {

        return fElements.size () ;
    }

    @Override
    public DefaultContext<ElementType> put (final ElementType pElement) throws IllegalArgumentException {

        if (fPutValidator != null && !fPutValidator.test (pElement)) {
            throw new IllegalArgumentException ("argument '" + pElement + "' invalid") ;
        }
        fElements.add (pElement) ;
        return this ;
    }

    @Override
    public DefaultContext<ElementType> putAll (final IReadableContext<ElementType> pElements) {

        for (final ElementType lElement : pElements) {
            put (lElement) ;
        }
        return this ;
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
    public Iterator<ElementType> iterator () {

        return Collections.unmodifiableList (fElements).iterator () ;
    }

    /**
     * Sets a new put validator. Note that elements currently in the list are
     * not validated, only new one. So if you want to have a context with
     * partially valid elements, make sure to put them into the context
     * <i>before</i> setting a validator.
     * 
     * @param pPutValidator
     *            New put validator.
     */
    public void setValidator (final Predicate<ElementType> pPutValidator) {

        fPutValidator = pPutValidator ;

    }

    /**
     * Returns the put validator currently in place.
     * 
     * @return Optionally <code>null</code>.
     */
    @Override
    public Predicate<ElementType> getValidator () {

        return fPutValidator ;
    }

}
