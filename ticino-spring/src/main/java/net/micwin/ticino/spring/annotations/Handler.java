
package net.micwin.ticino.spring.annotations;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a method as ticino event handler. Applicaple to public methods that have exactly one parameter. Owner of
 * method will be registered to a scope that handles such events.
 * 
 * @author micwin
 * 
 */

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
@Documented
@Inherited
public @interface Handler {

}
