
package net.micwin.ticino.spring;


import java.util.List;

import net.micwin.ticino.EventScope;
import net.micwin.ticino.spring.annotations.Handler;
import net.micwin.ticino.spring.annotations.Scope;

public class SuperBean extends RootBean {

    public String lastHandledStringFromSuper = null;

    @Scope(eventType = String.class)
    EventScope<String> superScope;

    public List<String> lastHandledList;

    @Handler
    public void handleStrings(final String eventHaha) {
        this.lastHandledStringFromSuper = eventHaha;
    }

    @Handler
    @Override
    public void handleList(final List<String> event) {
        this.lastHandledList = event;

    }

}
