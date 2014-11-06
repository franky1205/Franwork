package org.franwork.core.util.proxy;

import java.lang.reflect.Method;

/**
 * 
 * @author Frankie
 *
 */
public class ExecuteAnywayInterceptorMethod extends ExtendedInterceptorMethod {

	/**
	 * This executeAnyway boolean value determines whether or not to execute on every method invocation.
	 */
	protected Boolean executeAnyway;
	
	public Boolean getExecuteAnyway() {
		return executeAnyway;
	}

	public void setExecuteAnyway(Boolean executeAnyway) {
		this.executeAnyway = executeAnyway;
	}
	
	public ExecuteAnywayInterceptorMethod(InterceptorMethod interceptorMethod) throws ProxyInterceptorException {
		super(interceptorMethod);
	}
	
	@Override
	public Boolean isExecutedInterceptor(ProxyMethod methodAnno, Method method) {
		if (!this.interceptorMethod.isExecutedInterceptor(methodAnno, method)) {
			return Boolean.FALSE;
		}
		if (this.executeAnyway != null && this.executeAnyway) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	@Override
	public String toString() {
		return super.toString() + " -> " + "DefaultInterceptorMethod";
	}

	@Override
	public void setExtendedInfo(Object interceptor) throws ProxyInterceptorException {
		ProxyInterceptor interceptorAnno = interceptor.getClass().getAnnotation(ProxyInterceptor.class);
		this.executeAnyway = interceptorAnno.executeAnyway();
	}
}
