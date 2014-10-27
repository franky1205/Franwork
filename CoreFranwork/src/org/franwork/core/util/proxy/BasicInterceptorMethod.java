package org.franwork.core.util.proxy;

import java.lang.reflect.Method;

/**
 * 
 * @author Frankie
 *
 */
public class BasicInterceptorMethod extends InterceptorMethod {

	public BasicInterceptorMethod() {
		super();
	}

	public BasicInterceptorMethod(Object interceptor) throws ProxyInterceptorException {
		this.setExtendedInfo(interceptor);
	}
	
	@Override
	public Boolean isExecutedInterceptor(ProxyMethod methodAnno, Method method) {
		return (super.executed == null ? Boolean.FALSE : super.executed);
	}
	
	@Override
	public String toString() {
		return super.toString() + " -> " + "BasicInterceptorMethod";
	}
}
