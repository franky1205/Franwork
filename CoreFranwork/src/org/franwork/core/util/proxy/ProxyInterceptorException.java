package org.franwork.core.util.proxy;

/**
 * 
 * @author Frankie
 *
 */
public class ProxyInterceptorException extends Exception {

	/**
	 * Generated SerialUID
	 */
	private static final long serialVersionUID = 6217063446989264334L;
	
	private static final String DEFAULT_MESSAGE = "ProxyInterceptorException happened with type";
	
	public static final String INTERCEPTOR_UNDEFINED = "The given instance type is not annotated with ProxyInterceptor";
	
	public ProxyInterceptorException(String message) {
		super(message);
	}
	
	public ProxyInterceptorException(Class<?> interceptorType) {
		this(DEFAULT_MESSAGE, interceptorType);
	}

	public ProxyInterceptorException(String message, Class<?> interceptorType) {
		super(message + " : " + interceptorType.getName());
	}
}
