package net.micwin.ticino.context;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A context for some elements.
 *
 */
public interface IContext<ElementType> {

	/**
	 * Returns the theoretical maximum the container can hold without
	 * restructuring. its a mixture of former size and capacity and adresses the
	 * problem that even an {@link ArrayList} can theoretically hold infinite
	 * elements but normally has to restructure (recreate inner array) multiple
	 * times until then. A {@link LinkedList} does not have to restructure while
	 * an array itself never restructures (only being replaced). Note that
	 * external issues like RAM memory size, storage place etc are not
	 * considered here while normal operations inherent to the contexts' type
	 * (creating new links in a linked list) are not considered restructuring.
	 *
	 * @return The number of elements this context can hold without
	 *         restructuring and without applying other tricks.
	 */
	int getMaxStructureSize();

	/**
	 * The Number of elements this container can hold when applying all kinds of
	 * tricks like resizing inner buffers, backbuffering, rehashing, relocating
	 * memory etc.
	 *
	 * @return
	 */
	int getMaxTheoreticalSize();

	/**
	 * The number of elements in this context.
	 *
	 * @return
	 */
	int getCurrentElementCount();

}
