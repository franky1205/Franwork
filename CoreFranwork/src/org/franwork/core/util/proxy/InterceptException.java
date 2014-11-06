package org.franwork.core.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Proxy interceptor's exception throw method annotation.
 * While the executed target method throw exception the ProxyInterceptor
 * which method annotated with this InterceptException annotation will be executed.
 * 
 * The method which annotated with this annotation must have four parameter
 * types of Object.class, Method.class, Object[].class, Throwable.class.
 * For example : Method handleException is annotated with InterceptBefore.
 * 
 * public void handleException(Object obj, Method method, Object[] params, Throwable throwable)
 * 
 * @author Frankie
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterceptException {

}
