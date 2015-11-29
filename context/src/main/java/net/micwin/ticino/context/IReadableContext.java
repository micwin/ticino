
package net.micwin.ticino.context ;

import java.util.function.Predicate ;

public interface IReadableContext<ElementType> extends IContext<ElementType> , Iterable<ElementType> {

    /**
     * Returns a new context that contains all elements matching given
     * predicate.
     *
     * @param pPredicate
     * @return a Context holding elements that match predicate.
     */
    IModifyableContext<ElementType> lookup (Predicate<ElementType> pPredicate) ;

    /**
     * Add all elements matching given predicate into target.
     *
     * @param pPredicate
     *            The criteria with which the elements of context are selected.
     * @return pTarget The target list to put matches in.
     */
    <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (Predicate<ElementType> pPredicate ,
            TargetContextType pTarget) ;

    /**
     * Returns a new context that contains all elements matching given
     * predicate.
     *
     * @param pPredicate
     * @param pMaxCount
     *            maximum number of hits. If reached, search is ended
     *            in-between.
     * @return a Context holding elements that match predicate.
     */
    IModifyableContext<ElementType> lookup (Predicate<ElementType> pPredicate , int pMaxCount) ;

    /**
     * Add all elements matching given predicate into target.
     *
     * @param pPredicate
     *            The criteria with which the elements of context are selected.
     * @param pMaxCount
     *            maximum number of hits. If reached, search is ended
     *            in-between.
     * @return pTarget The target list to put matches in.
     */
    <TargetContextType extends IModifyableContext<ElementType>> TargetContextType lookup (Predicate<ElementType> pPredicate ,
            int pMaxCount , TargetContextType pTarget) ;

}
