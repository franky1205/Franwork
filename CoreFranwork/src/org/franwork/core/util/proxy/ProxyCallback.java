package org.franwork.core.util.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ProxyCallback class is used by cglib to create a proxy instance.
 * 
 * @author Frankie
 *
 */
public class ProxyCallback implements MethodInterceptor {

	private Logger _logger = LoggerFactory.getLogger(this.getClass());
	
	public static final Class<? extends InterceptorMethod> DEFAULT_INTERCPT_METHOD_CLASS = BasicInterceptorMethod.class;
	
	/**
	 * All the interceptor methods instance map.
	 */
	protected Map<String, InterceptorMethod> interceptorsMap;
	
	/**
	 * Thread local isProxying boolean value.
	 */
	protected ThreadLocal<Boolean> isProxying = new ThreadLocal<Boolean>();
	
	/**
	 * Constructor take InterceptorMethod array as parameter.
	 * @param interceptorMethods
	 */
	public ProxyCallback(InterceptorMethod... interceptorMethods) throws Throwable {
		this(Arrays.asList(interceptorMethods));
	}

	/**
	 * Constructor take InterceptorMethod collection as parameter.
	 * @param interceptorMethods
	 * @throws Throwable
	 */
	public ProxyCallback(Collection<InterceptorMethod> interceptorMethods) throws Throwable {
		this.initInterceptorsMap(interceptorMethods);
	}
	
	protected Map<String, InterceptorMethod> getInterceptorsMap() {
		return interceptorsMap;
	}

	protected void setInterceptorsMap(Map<String, InterceptorMethod> interceptorsMap) {
		this.interceptorsMap = interceptorsMap;
	}

	/**
	 * Get all the interceptor's key set.
	 * 
	 * @return
	 */
	public Set<String> getInterceptorKeys() {
		return interceptorsMap.keySet();
	}

	/**
	 * Get an interceptor by the given interceptor name.
	 * 
	 * @param interceptorKey
	 * @return
	 */
	public InterceptorMethod getInterceptor(String interceptorKey) {
		if (!this.interceptorsMap.containsKey(interceptorKey)) {
			return null;
		}
		return this.interceptorsMap.get(interceptorKey);
	}

	/**
	 * Init the ProxyCallback instance's interceptors map.
	 * 
	 * @param interceptorMethods
	 */
	protected void initInterceptorsMap(Collection<InterceptorMethod> interceptorMethods) throws Throwable {
		if (CollectionUtils.isEmpty(interceptorMethods)) {
			return ;
		}
		this.interceptorsMap = new LinkedHashMap<String, InterceptorMethod>();
		for (InterceptorMethod interceptorMethod : interceptorMethods) {
			try {
				if (interceptorMethod == null) {
					throw new ProxyInterceptorException("The given interceptorMethod object is null");
				}
				if (interceptorMethod.getInterceptor() == null) {
					throw new ProxyInterceptorException("The interceptor object is undefined");
				}
				this.interceptorsMap.put(interceptorMethod.getInterceptorName(), interceptorMethod);
			} catch (ProxyInterceptorException e) {
				_logger.info(e.getMessage());
				continue;
			} catch (Exception e) {
				_logger.error(e.getMessage(), e);
				throw e;
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object, 
	 * 		java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
	 */
	public Object intercept(Object proxyObj, Method proxyMethod, Object[] paramObjs, 
			MethodProxy methodProxy) throws Throwable {
		if (isProxying.get() != null && isProxying.get().booleanValue()) {
			return methodProxy.invokeSuper(proxyObj, paramObjs);
		}
		isProxying.set(Boolean.TRUE);
		if (MapUtils.isEmpty(this.interceptorsMap)) {
			return methodProxy.invokeSuper(proxyObj, paramObjs);
		}
		ProxyMethod methodAnno = proxyMethod.getAnnotation(ProxyMethod.class);
		List<InterceptorMethod> executedInterceptors = this.getExecutedInterceptors(methodAnno, proxyMethod);
		Object invokeResult = null;
		this.executeBeforeInterceptors(executedInterceptors.iterator(), proxyObj, proxyMethod, paramObjs);
		try {
			invokeResult = methodProxy.invokeSuper(proxyObj, paramObjs);
		} catch (Throwable ex) {
			this.executeExceptionInterceptors(executedInterceptors.iterator(), proxyObj, proxyMethod, paramObjs, ex);
		}
		this.executeAfterInterceptors(new ReverseIterator<InterceptorMethod>(executedInterceptors), 
				proxyObj, proxyMethod, paramObjs, invokeResult);
		isProxying.set(Boolean.FALSE);
		return invokeResult;
	}
	
	protected List<InterceptorMethod> getExecutedInterceptors(ProxyMethod methodAnno, Method method) {
		List<InterceptorMethod> executedInterceptors = new LinkedList<InterceptorMethod>();
		List<String> interceptorNames = new ArrayList<String>();
		if (methodAnno != null && !ArrayUtils.isEmpty(methodAnno.values())) {
			interceptorNames.addAll(Arrays.asList(methodAnno.values()));
		}
		for (String interceptorName : interceptorNames) {
			if (this.interceptorsMap.containsKey(interceptorName)) {
				executedInterceptors.add(this.interceptorsMap.get(interceptorName));
			}
		}
		for (InterceptorMethod interceptor : this.interceptorsMap.values()) {
			if (executedInterceptors.contains(interceptor) || !interceptor.isExecutedInterceptor(methodAnno, method)) {
				continue;
			}
			executedInterceptors.add(interceptor);
		}
		return executedInterceptors;
	}
	
	protected void executeBeforeInterceptors(Iterator<InterceptorMethod> interceptorList, 
			Object proxyObj, Method proxyMethod, Object[] paramObjs) {
		if (interceptorList == null) {
			return ;
		}
		while (interceptorList.hasNext()) {
			try {
				interceptorList.next().executeBeforeMethods(proxyObj, proxyMethod, paramObjs);
			} catch (Exception e) {
				_logger.error(e.getMessage(), e);
			}
		}
	}
	
	protected void executeAfterInterceptors(Iterator<InterceptorMethod> interceptorList, 
			Object proxyObj, Method proxyMethod, Object[] paramObjs, Object afterReturning) {
		if (interceptorList == null) {
			return ;
		}
		while (interceptorList.hasNext()) {
			try {
				interceptorList.next().executeAfterMethods(proxyObj, proxyMethod, paramObjs, afterReturning);
			} catch (Exception e) {
				_logger.error(e.getMessage(), e);
			}
		}
	}
	
	protected void executeExceptionInterceptors(Iterator<InterceptorMethod> interceptorList, 
			Object proxyObj, Method proxyMethod, Object[] paramObjs, Throwable ex) {
		if (interceptorList == null) {
			return ;
		}
		while (interceptorList.hasNext()) {
			try {
				interceptorList.next().executeExceptionMethods(proxyObj, proxyMethod, paramObjs, ex);
			} catch (Exception e) {
				_logger.error(e.getMessage(), e);
			}
		}
	}
	
	private static class ReverseIterator<T> implements Iterator<T> {

	    private final List<T> list;
	    private int position;

	    public ReverseIterator(List<T> list) {
	        this.list = list;
	        this.position = list.size() - 1;
	    }

	    public boolean hasNext() {
	        return position >= 0;
	    }

	    public T next() {
	        return list.get(position--);
	    }

	    public void remove() {
	        throw new UnsupportedOperationException();
	    }

	}
}
