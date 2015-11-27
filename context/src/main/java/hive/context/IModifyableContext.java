package hive.context;

public interface IModifyableContext<ElementType> extends IContext<ElementType> {

	/**
	 * Add element to context.
	 *
	 * @param pElement
	 * @return
	 */
	public IModifyableContext<ElementType> put(ElementType pElement);

	/**
	 * Add all elements of given context to this context.
	 *
	 * @param pElement
	 * @return
	 */
	public IModifyableContext<ElementType> putAll(IReadableContext<ElementType> pElements);

}
