
package net.micwin.ticino.context ;

import java.util.function.Predicate ;

public interface IReadableContext<ElementType> extends IContext<ElementType> , Iterable<ElementType> {

    /**
     * Iterates over elements and returns those who meet the
     * predicates' condition in a {@link IReadableContext}.
     *
     * @param pPredicate
     *            A functional (lambda) that defines if a specific element meets
     *            the condition of this lookup.
     * @return a Context holding elements that match predicate.
     */
    IReadableContext<ElementType> lookup (Predicate<ElementType> pPredicate) ;

    /**
     * Iterates over elements and puts those who meet the predicates' condition
     * in <code>pTarget</code>.
     *
     * @param pPredicate
     *            A functional (lambda) that defines if a specific element meets
     *            the condition of this lookup.
     * @param pTarget
     *            The context to put matches in.
     * @return pTarget
     */
    <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (Predicate<ElementType> pPredicate ,
            TargetContextType pTarget) ;

    /**
     * Iterates over at least <code>pMaxCount</code> elements and returns those
     * who
     * meet the predicates' condition in a {@link IReadableContext}. The method
     * will terminate if either the end of elements is reached or
     * <code>pMaxCount</code> elements have been collected.
     *
     * @param pPredicate
     *            A {@link Predicate} (lambda) that returns true if a specific
     *            element meets
     *            the criteria of this lookup.
     * @param pMaxCount
     *            maximum number of hits collected. If reached, search is ended
     *            in-between.
     * @return a Context holding elements that match predicate.
     */
    IReadableContext<ElementType> lookup (Predicate<ElementType> pPredicate , int pMaxCount) ;

    /**
     * Iterates over at least <code>pMaxCount</code> elements and puts those who
     * meet the predicates' condition into <code>pTarget</code>. The method
     * will terminate if either the end of elements is reached or
     * <code>pMaxCount</code> elements have been collected.
     *
     * @param pPredicate
     *            A functional (lambda) that defines if a specific element meets
     *            the condition of this lookup.
     * @param pMaxCount
     *            maximum number of hits collected. If reached, search is ended
     *            in-between.
     * @param pTarget
     *            The context to put matches in.
     * @return pTarget
     */
    <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (Predicate<ElementType> pPredicate ,
            int pMaxCount , TargetContextType pTarget) ;

}
