package org.franwork.core.util.proxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.franwork.core.util.BeanUtils;

/**
 * Default InterceptorMethod implementation with all required fields prepared and ready.
 * 
 * @author Frankie
 *
 */
public abstract class InterceptorMethod {
	
	/**
	 * 
	 */
	public static final Class<?>[] interceptBeforeMethodTypes = new Class<?>[]{Object.class, Method.class, Object[].class};
	
	/**
	 * 
	 */
	public static final Class<?>[] interceptAfterMethodTypes = new Class<?>[]{Object.class, Method.class, Object[].class, Object.class};
	
	/**
	 * 
	 */
	public static final Class<?>[] interceptExceptionMethodTypes = new Class<?>[]{Object.class, Method.class, Object[].class, Throwable.class};
	
	protected String interceptorName;

	protected Object interceptor;
	
	/**
	 * This executed boolean value determines whether or not to execute when the invoked
	 * function method is annotated with ProxyMethod annotation.
	 */
	protected Boolean executed;
	
	/**
	 * Interceptor before method object list.
	 */
	protected List<Method> beforeMethods;
	
	/**
	 * Interceptor after method object list.
	 */
	protected List<Method> afterMethods;
	
	/**
	 * Interceptor exception method object list.
	 */
	protected List<Method> exceptionMethods;
	
	/**
	 * Default Constructor
	 */
	public InterceptorMethod() {
		super();
	}

	/**
	 * Constructor with interceptor as the initializing parameter.
	 * @param interceptor The interceptor instance.
	 * @throws ProxyInterceptorException
	 */
	public InterceptorMethod(Object interceptor) throws ProxyInterceptorException {
		this.setExtendedInfo(interceptor);
	}
	
	public String getInterceptorName() {
		return interceptorName;
	}
	
	public void setExtendedInfo(Object interceptor) throws ProxyInterceptorException {
		this.initInterceptor(interceptor);
	}
	
	public Object getInterceptor() {
		return interceptor;
	}

	public void setExecuted(Boolean executed) {
		this.executed = executed;
	}

	public Boolean getExecuted() {
		return executed;
	}
	
	public void setBeforeMethod(List<Method> beforeMethods) {
		this.beforeMethods = beforeMethods;
	}

	public List<Method> getBeforeMethods() {
		return beforeMethods;
	}
	
	public void setAfterMethod(List<Method> afterMethods) {
		this.afterMethods = afterMethods;
	}

	public List<Method> getAfterMethods() {
		return afterMethods;
	}

	public List<Method> getExceptionMethods() {
		return exceptionMethods;
	}

	public void setExceptionMethods(List<Method> exceptionMethods) {
		this.exceptionMethods = exceptionMethods;
	}

	protected final void initInterceptor(Object interceptor) throws ProxyInterceptorException {
		if (interceptor == null) {
			throw new ProxyInterceptorException("The given interceptor obj is null");
		}
		ProxyInterceptor interceptorAnno = interceptor.getClass().getAnnotation(ProxyInterceptor.class);
		if (interceptorAnno == null) {
			throw new ProxyInterceptorException(ProxyInterceptorException.INTERCEPTOR_UNDEFINED, interceptor.getClass());
		}
		this.setInterceptorName(interceptor, interceptorAnno);
		this.beforeMethods = BeanUtils.getAnnotatedMethods(interceptor, InterceptBefore.class, interceptBeforeMethodTypes);
		this.afterMethods = BeanUtils.getAnnotatedMethods(interceptor, InterceptAfter.class, interceptAfterMethodTypes);
		this.exceptionMethods = BeanUtils.getAnnotatedMethods(interceptor, InterceptException.class, interceptExceptionMethodTypes);
		this.executed = interceptorAnno.executed();
		this.interceptor = interceptor;
	}
	
	protected void setInterceptorName(Object interceptor, ProxyInterceptor interceptorAnno) {
		if (StringUtils.isNotBlank(interceptorAnno.value())) {
			this.interceptorName = interceptorAnno.value();
			return ;
		}
		this.interceptorName = interceptor.getClass().getSimpleName().substring(0, 1).toLowerCase() +
				interceptor.getClass().getSimpleName().substring(1);
	}
	
	public void executeBeforeMethods(Object proxyObj, Method proxyMethod, Object[] paramObjs) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (CollectionUtils.isEmpty(this.beforeMethods)) {
			return;
		}
		for (Method beforeMethod : this.beforeMethods) {
			beforeMethod.invoke(this.interceptor, proxyObj, proxyMethod, paramObjs);
		}
	}
	
	public void executeAfterMethods(Object proxyObj, Method proxyMethod, Object[] paramObjs, Object afterReturning) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (CollectionUtils.isEmpty(this.afterMethods)) {
			return;
		}
		for (Method afterMethod : this.afterMethods) {
			afterMethod.invoke(this.interceptor, proxyObj, proxyMethod, paramObjs, afterReturning);
		}
	}
	
	public void executeExceptionMethods(Object proxyObj, Method proxyMethod, Object[] paramObjs, Throwable ex) 
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		if (CollectionUtils.isEmpty(this.exceptionMethods)) {
			return ;
		}
		for (Method exceptionMethod : this.exceptionMethods) {
			exceptionMethod.invoke(this.interceptor, proxyObj, proxyMethod, paramObjs, ex);
		}
	}
	
	public abstract Boolean isExecutedInterceptor(ProxyMethod methodAnno, Method method);
	
	@Override
	public String toString() {
		return "InterceptorMethod [interceptorName=" + interceptorName + "]";
	}
}