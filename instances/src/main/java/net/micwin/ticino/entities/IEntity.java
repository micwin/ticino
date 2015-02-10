
package net.micwin.ticino.entities ;

import java.util.UUID ;

/**
 * A instance of an entity with risen complexity.
 * 
 * @author micwin
 */

public interface IEntity {

    /**
     * This is the ID of this physical instance, for internal management
     * purposes. Donnot use this as a reference
     * to the logical unit. Although this id is bound both to the
     * specific in memory and external representstion, it is not guaranteed to
     * stay the same when traversing these representation from one to another.
     * Since UUIDs come with a markable resource cost, implementors of this
     * interface
     * should generate this on demand, not anticipant, while customers should
     * use this method with caution and not without well defined reason.
     * Again, this ID is guaranteed to be unique in *one* representation, but is
     * not guaranteed to survive representation change.
     * 
     * @return
     */
    UUID getPhysicalId () ;

    /**
     * The state counter that reports how often something of this entity has
     * been changed.
     * <ul>
     * <li>0 : instantiated (default constructor called, at least one field has
     * values resuluting in unpredictable behavior)</li>
     * <li>1 : initialized (all fields have well defined values)</li>
     * <li>2: one field changed since initialization</li>
     * <li>3 : two fields changed since initialization</li>
     * <li>4 : ...</li>
     * </ul>
     * 
     * @return A value between 0 and {@link Integer#MAX_VALUE}
     */
    int getStateCounter () ;

}
