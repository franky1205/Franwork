package org.franwork.core.util.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 
 * @author Frankie
 *
 */
public abstract class ExtendedInterceptorMethod extends InterceptorMethod {

	protected InterceptorMethod interceptorMethod;
	
	public ExtendedInterceptorMethod(InterceptorMethod interceptorMethod) throws ProxyInterceptorException {
		if (interceptorMethod == null) {
			throw new ProxyInterceptorException("The given interceptorMethod object is null");
		}
		if (interceptorMethod.getInterceptor() == null) {
			throw new ProxyInterceptorException("The interceptor object is undefined");
		}
		this.interceptorMethod = interceptorMethod;
		this.setExtendedInfo(this.interceptorMethod.getInterceptor());
	}
	
	public InterceptorMethod getInterceptorMethod() {
		return interceptorMethod;
	}
	
	@Override
	public String getInterceptorName() {
		return interceptorMethod.getInterceptorName();
	}

	@Override
	public Object getInterceptor() {
		return interceptorMethod.getInterceptor();
	}

	@Override
	public void setExecuted(Boolean executed) {
		interceptorMethod.setExecuted(executed);
	}

	@Override
	public Boolean getExecuted() {
		return interceptorMethod.getExecuted();
	}

	@Override
	public List<Method> getBeforeMethods() {
		return interceptorMethod.getBeforeMethods();
	}

	@Override
	public List<Method> getAfterMethods() {
		return interceptorMethod.getAfterMethods();
	}

	@Override
	protected void setInterceptorName(Object interceptor, ProxyInterceptor interceptorAnno) {
		interceptorMethod.setInterceptorName(interceptor, interceptorAnno);
	}

	@Override
	public void executeBeforeMethods(Object proxyObj, Method proxyMethod, Object[] paramObjs) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		interceptorMethod.executeBeforeMethods(proxyObj, proxyMethod, paramObjs);
	}

	@Override
	public void executeAfterMethods(Object proxyObj, Method proxyMethod, Object[] paramObjs, Object afterReturning)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		interceptorMethod.executeAfterMethods(proxyObj, proxyMethod, paramObjs, afterReturning);
	}

	@Override
	public void executeExceptionMethods(Object proxyObj, Method proxyMethod, Object[] paramObjs, Throwable ex) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		interceptorMethod.executeExceptionMethods(proxyObj, proxyMethod, paramObjs, ex);
	}

	/**
	 * Init the implemented InterceptorMethod 
	 */
	public abstract void setExtendedInfo(Object interceptor)  throws ProxyInterceptorException;
	
}
