package org.franwork.core.util.proxy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Frankie
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyMethod {
	
	/**
	 * The name of proxy method. If none is given the default name 
	 * will be set to the proxy method function name.
	 * 
	 * @return
	 */
	public String name() default StringUtils.EMPTY;
	
	/**
	 * All the mapping executed proxy interceptor names.
	 * 
	 * @return
	 */
	public String[] values() default {};
}
