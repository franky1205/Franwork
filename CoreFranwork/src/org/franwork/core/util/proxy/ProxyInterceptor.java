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
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ProxyInterceptor {

	/**
	 * This ProxyInterceptor's proxy name. Which is configured 
	 * in the ProxyMethod's value list. If this value is not given
	 * the class name with first alphabet in lower case will be used 
	 * as this ProxyInterceptor's interceptor name.
	 * 
	 * @return
	 */
	public String value() default StringUtils.EMPTY;
	
	/**
	 * Whether this interceptor is capable executed or not.
	 * 
	 * @return
	 */
	public boolean executed() default true;
	
	/**
	 * Whether this interceptor is executed no matter the proxy method is 
	 * designated explicitly or not.
	 * 
	 * @return
	 */
	public boolean executeAnyway() default false;
	
	/**
	 * Define the proxy interceptor's executing pointcut expression 
	 * when this ProxyInterceptor's executed is configured of true value.
	 * The proxy handler will determined whether this ProxyInterceptor should
	 * be executed or not. If pointcutExp values is not given the ProxyInterceptor
	 * will always execute once the executed boolean value is set to true.
	 * 
	 * @return
	 */
	public String[] pointcuts() default {};
	
	/**
	 * Define the proxy interceptor's executing ProxyMethod name pattern.
	 * The name patterns are regular expressions. If One of the giving 
	 * name pattern is matched with the proxy method's name, this ProxyInterceptor 
	 * annotates interceptor object will be executed.
	 * 
	 * If name patterns is given then the executed status will be set to true automatically.
	 * 
	 * @return
	 */
	public String[] namePatterns() default {};
}
