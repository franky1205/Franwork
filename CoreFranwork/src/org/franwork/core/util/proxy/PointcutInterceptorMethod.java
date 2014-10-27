package org.franwork.core.util.proxy;

import java.lang.reflect.Method;

/**
 * AspectJ Pointcut expression supported interceptor executing rule.
 * 
 * This PointcutInterceptorMethod supported pointcut designators involved,
 * 
 * within
 * 
 * execution
 * 
 * this
 * 
 * target
 * 
 * args
 * 
 * @author Frankie
 *
 */
public class PointcutInterceptorMethod extends ExtendedInterceptorMethod {
	
	/**
	 * The AspectJ ponintcut expression array.
	 */
	protected String[] pointcuts;

	public PointcutInterceptorMethod(InterceptorMethod interceptorMethod) throws ProxyInterceptorException {
		super(interceptorMethod);
	}

	public String[] getPointcuts() {
		return pointcuts;
	}

	public void setPointcuts(String[] pointcuts) {
		this.pointcuts = pointcuts;
	}

	@Override
	public void setExtendedInfo(Object interceptor) throws ProxyInterceptorException {
		ProxyInterceptor interceptorAnno = interceptor.getClass().getAnnotation(ProxyInterceptor.class);
		this.pointcuts = interceptorAnno.pointcuts();
	}

	@Override
	public Boolean isExecutedInterceptor(ProxyMethod methodAnno, Method method) {
		if (!this.interceptorMethod.isExecutedInterceptor(methodAnno, method)) {
			return Boolean.FALSE;
		}
		return null;
	}

}
