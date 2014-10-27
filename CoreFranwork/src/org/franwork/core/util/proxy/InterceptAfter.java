package org.franwork.core.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Proxy interceptor's after executed method annotation. 
 * After the real function has executed, the interceptor's method which
 * annotated with this InterceptAfter annotation will be called.
 *  
 * The method which annotated with this annotation must have four parameter
 * types of Object.class, Method.class, Object[].class, Object.class
 * For example : Method after is annotated with InterceptAfter.
 * 
 * public void after(Object obj, Method method, Object[] params, Object afterReturning)
 * 
 * @author Frankie
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptAfter {

}
