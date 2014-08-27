
package net.micwin.ticino.spring.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A Annotation that identifies an event scope to be injected
 * 
 * @author micwin
 * 
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Documented
@Inherited
public @interface Scope {

    /**
     * The base type of events to be dispatched over this event.
     * 
     * @return
     */
    Class eventType();
}
