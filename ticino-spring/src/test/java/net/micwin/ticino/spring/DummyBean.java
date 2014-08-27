
package net.micwin.ticino.spring;


import java.util.List;

import net.micwin.ticino.EventScope;
import net.micwin.ticino.spring.annotations.Handler;
import net.micwin.ticino.spring.annotations.Scope;

public class DummyBean extends SuperBean {

    @Scope(eventType = List.class)
    EventScope<List<String>> listScope;

    public Integer lastScope1Event;
    public String lastStringEvent;

    @Handler
    public void handle(final Integer event) {
        this.lastScope1Event = event;
    }

    @Handler
    public void handle(final String event) {
        this.lastStringEvent = event;
    }

}
