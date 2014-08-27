
package net.micwin.ticino.spring;


import java.util.LinkedList;
import java.util.List;

import net.micwin.ticino.EventScope;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContextScanningConfigurationTest {

    private static final String STRING_EVENT = "Hello, world";

    private static final List<String> LIST_EVENT = new LinkedList<String>();

    SpringContextScanningConfiguration springContextScanningConfiguration;
    private ApplicationContext applicationContext;

    EventScope<String> stringScope;
    EventScope<Integer> intScope;
    EventScope<List<String>> stringListScope;

    DummyBean dummyBean;

    @Before
    public void before() {

        this.applicationContext = new ClassPathXmlApplicationContext("SpringContextScanningConfigurationTestCtx.xml");

        this.springContextScanningConfiguration = this.applicationContext
                .getBean(SpringContextScanningConfiguration.class);

        this.stringScope = (EventScope<String>) this.springContextScanningConfiguration.scopes.get(String.class);
        this.intScope = (EventScope<Integer>) this.springContextScanningConfiguration.scopes.get(Integer.class);
        this.stringListScope = (EventScope<List<String>>) this.springContextScanningConfiguration.scopes
                .get(List.class);

        this.dummyBean = this.applicationContext.getBean(DummyBean.class);
    }

    @Test
    public void testBefore() {
        Assert.assertNotNull(this.stringScope);
        Assert.assertNotNull(this.intScope);
        Assert.assertNotNull(this.stringListScope);
    }

    @Test
    public void testOverrideIgnoresHandler() {
        this.stringListScope.dispatch(SpringContextScanningConfigurationTest.LIST_EVENT);

        // root bean type must not have been reached (due to overriding by SuperBean)
        Assert.assertNull(this.dummyBean.lastRootEvent);

    }

    @Test
    public void testInheritedHandler() {
        this.stringListScope.dispatch(SpringContextScanningConfigurationTest.LIST_EVENT);

        // event reaches handler in super class
        Assert.assertEquals(SpringContextScanningConfigurationTest.LIST_EVENT, this.dummyBean.lastHandledList);
    }

    @Test
    public void testMultiHandler() {
        this.stringScope.dispatch(SpringContextScanningConfigurationTest.STRING_EVENT);

        // event reaches handler in bean class
        Assert.assertEquals(SpringContextScanningConfigurationTest.STRING_EVENT, this.dummyBean.lastStringEvent);

        // event reaches handler in super class
        Assert.assertEquals(SpringContextScanningConfigurationTest.STRING_EVENT,
                this.dummyBean.lastHandledStringFromSuper);
    }

}
