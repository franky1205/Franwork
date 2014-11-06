package org.franwork.core.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Proxy interceptor's before executed method annotation. 
 * Before the real function executes, the interceptor's method which
 * annotated with this InterceptBefore annotation will be called first.
 *  
 * The method which annotated with this annotation must have three parameter
 * types of Object.class, Method.class, Object[].class.
 * For example : Method before is annotated with InterceptBefore.
 * 
 * public void before(Object obj, Method method, Object[] params)
 *  
 * @author Frankie
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptBefore {

}
