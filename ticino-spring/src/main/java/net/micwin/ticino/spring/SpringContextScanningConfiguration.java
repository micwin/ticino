
package net.micwin.ticino.spring;


import java.lang.reflect.Method;
import java.util.HashMap;

import net.micwin.ticino.EventScope;
import net.micwin.ticino.spring.annotations.Handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * A configurator that scans the spring context for ticino event scopes and handlers and wires them seamlessly together.
 * Since ticino is highly declarative elsewhere, we need some sort of hint to find out about the relevance of a class in
 * respect of ticino. There are the following ways of marking a class as a ticino class:
 * <ul>
 * <li>implement interface {@link TicinoAware} (which contains no methods)</li>
 * <li>use annotation {@link net.micwin.ticino.spring.annotations.TicinoAware}
 * </ul>
 * 
 * @author MicWin
 * 
 */
public class SpringContextScanningConfiguration implements ApplicationContextAware, BeanPostProcessor {

    private final boolean strict = false;

    private ApplicationContext applicationContext;
    final HashMap<Class<? extends Object>, EventScope<? extends Object>> scopes = new HashMap<Class<? extends Object>, EventScope<? extends Object>>();

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(final Object bean, final String name) throws BeansException {
        if (this.checkProcessSpiClass(bean, name)) {
            return bean;
        }
        if (!this.hasTicinoAnnotation(bean.getClass())) {
            return bean;
        }

        return this.postProcessTicinoAwareBean(bean);

    }

    private Object postProcessTicinoAwareBean(final Object pBean) {
        return pBean;
    }

    @Override
    public Object postProcessBeforeInitialization(final Object bean, final String name) throws BeansException {
        return bean;
    }

    private boolean checkProcessSpiClass(final Object bean, final String name) {

        try {

            @SuppressWarnings("unchecked")
            final EventScope<? extends Object> scope = (EventScope<Object>) bean;
            final EventScope<? extends Object> present = this.scopes.get(scope.getBaseClass());

            if (present == null) {
                this.scopes.put(scope.getBaseClass(), scope);
            }

            return true;

        }
        catch (final ClassCastException cce) {
            // no spi so ignore
            return false;
        }

    }

    /**
     * Checks wether or not a bean is ticino aware.
     * 
     * @param classToTest
     * @return
     */
    @SuppressWarnings("unchecked")
    boolean hasTicinoAnnotation(@SuppressWarnings("rawtypes") final Class classToTest) {

        if (classToTest == Object.class) {
            return false;
        }

        // look for event handler annotations
        for (final Method method : classToTest.getDeclaredMethods()) {
            final Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes == null || parameterTypes.length > 1) {
                return false;
            }

            final Handler annotation = method.getAnnotation(Handler.class);
            if (annotation != null) {
                return true;
            }
        }

        // check 4 : scan super
        return this.hasTicinoAnnotation(classToTest.getSuperclass());
    }
}
