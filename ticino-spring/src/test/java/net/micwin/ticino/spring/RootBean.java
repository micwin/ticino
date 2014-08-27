
package net.micwin.ticino.spring;


import java.util.List;

import net.micwin.ticino.spring.annotations.Handler;

public class RootBean {

    public List<String> lastRootEvent;

    @Handler
    public void handleList(final List<String> pEvent) {
        this.lastRootEvent = pEvent;
    }
}
