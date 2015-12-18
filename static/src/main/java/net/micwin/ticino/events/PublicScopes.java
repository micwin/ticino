
package net.micwin.ticino.events;

import java.util.*;

/**
 * A container for event scopes that should be accessible throughout the vm
 * without the need to have a reference.
 * 
 * @author micwin
 */

public class PublicScopes {

    private static final Map<Class<?> , EventScope<?>> sScopes = new HashMap<Class<?> , EventScope<?>> ();

    /**
     * Remove all registered scopes. Not a public method with good reason.
     */
    static void clear () {

        sScopes.clear ();

    }

    /**
     * Registers a new public {@link EventScope} for its base type. This is an
     * expensive
     * operation, so make sure to only call as seldom as possible.
     * 
     * @param pScope
     *            a event scope.
     */
    public static <T> EventScope<T> register (final EventScope<T> pScope) {

        if (pScope.getBaseClass () == null) {
            throw new IllegalArgumentException ("with public event scopes, base types are mandatory");
        }

        if (null != get (pScope.getBaseClass ())) {
            throw new IllegalArgumentException ("already registered to type " + pScope.getBaseClass ());
        }

        sScopes.put (pScope.getBaseClass () , pScope);

        return pScope;

    }

    /**
     * Returns the event scope bound to that event type. This is an expensive
     * operation, so make sure to only call as seldom as possible.
     * 
     * @param pEventType
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> EventScope<T> get (final Class<T> pEventType) {

        if (pEventType == Object.class) {
            return null;
        }

        EventScope<T> lResult = (EventScope<T>) sScopes.get (pEventType);

        if (lResult != null) {
            return lResult;
        }

        for (final Class<?> lInterface : pEventType.getInterfaces ()) {

            lResult = (EventScope<T>) sScopes.get (pEventType);

            if (lResult != null) {
                return lResult;
            }

        }

        // still here - dive into super class

        return (EventScope<T>) get (pEventType.getSuperclass ());
    }

}
