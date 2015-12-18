
package net.micwin.ticino.events;

import static org.junit.Assert.assertSame ;

import org.junit.*;

public class PublicScopesTest {

    @Before
    public void before () {

        PublicScopes.clear ();
    }

    @Test
    public void shouldReturnBaseType () {

        final EventScope<Number> lScope = new EventScope<> (Number.class);
        PublicScopes.register (lScope);
        assertSame (lScope , PublicScopes.get (Number.class));
    }

    @Test
    public void shouldReturnSubTypes () {

        final EventScope<Number> lScope = new EventScope<> (Number.class);
        PublicScopes.register (lScope);
        assertSame (lScope , PublicScopes.get (Integer.class));
        assertSame (lScope , PublicScopes.get (Double.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCancelWeakEventTypes () {

        final EventScope<Number> lScope = new EventScope<> ();
        PublicScopes.register (lScope);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldCancelDoubleRegistration () {

        final EventScope<Number> lScope = new EventScope<> (Number.class);
        PublicScopes.register (lScope);
        PublicScopes.register (lScope);

    }

}
