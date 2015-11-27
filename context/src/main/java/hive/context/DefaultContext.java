
package hive.context ;

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

    public DefaultContext () {

        this (null , new LinkedList<> ()) ;
    }

    @SafeVarargs
    public DefaultContext (final ElementType ... pInitialElements) {

        this (null , Arrays.asList (pInitialElements)) ;
    }

    @SafeVarargs
    public DefaultContext (final Predicate<ElementType> pPutValidator, final ElementType ... pInitialElements) {

        this (pPutValidator , Arrays.asList (pInitialElements)) ;
    }

    public DefaultContext (final Collection<ElementType> pInitialElements) {

        this (null , pInitialElements) ;
    }

    public DefaultContext (final Predicate<ElementType> pPutValidator, final Iterable<ElementType> pInitialElements) {

        fElements = new LinkedList<> () ;
        fPutValidator = pPutValidator ;

        for (final ElementType lInitialElement : pInitialElements) {
            put (lInitialElement) ;
        }
    }

    @Override
    public IModifyableContext<ElementType> lookup (final Predicate<ElementType> pPredicate) {

        return lookup (pPredicate , Integer.MAX_VALUE , new DefaultContext<ElementType> ()) ;
    }

    @Override
    public <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (
            final Predicate<ElementType> pPredicate , final TargetContextType pTarget) {

        return lookup (pPredicate , Integer.MAX_VALUE , pTarget) ;
    }

    @Override
    public DefaultContext<ElementType> lookup (final Predicate<ElementType> pPredicate , final int pMaxCount) {

        return lookup (pPredicate , pMaxCount , new DefaultContext<> ()) ;
    }

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

    @Override
    public Iterator<ElementType> iterator () {

        return Collections.unmodifiableList (fElements).iterator () ;
    }

    public void setValidator (final Predicate<ElementType> pPutValidator) {

        fPutValidator = pPutValidator ;

    }

    @Override
    public Predicate<ElementType> getValidator () {

        return fPutValidator ;
    }

}
