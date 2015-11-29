package net.micwin.ticino.context;

/**
 * A context that can both be read and modified.
 *
 *
 * @param <ElementType>
 */
public interface IReadableModifyableContext<ElementType>
		extends IReadableContext<ElementType>, IModifyableContext<ElementType> {

}
