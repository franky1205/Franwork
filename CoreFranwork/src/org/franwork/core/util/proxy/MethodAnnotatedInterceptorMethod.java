package org.franwork.core.util.proxy;

import java.lang.reflect.Method;

public class MethodAnnotatedInterceptorMethod extends ExtendedInterceptorMethod {

	public MethodAnnotatedInterceptorMethod(InterceptorMethod interceptorMethod) throws ProxyInterceptorException {
		super(interceptorMethod);
	}

	@Override
	public void setExtendedInfo(Object interceptor) throws ProxyInterceptorException {
		// TODO Do nothing
	}

	@Override
	public Boolean isExecutedInterceptor(ProxyMethod methodAnno, Method method) {
		if (!this.interceptorMethod.isExecutedInterceptor(methodAnno, method)) {
			return Boolean.FALSE;
		}
		if (methodAnno == null) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
