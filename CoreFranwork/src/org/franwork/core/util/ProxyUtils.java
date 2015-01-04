package org.franwork.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.franwork.core.util.proxy.BasicInterceptorMethod;
import org.franwork.core.util.proxy.InterceptorMethod;
import org.franwork.core.util.proxy.NamePatternInterceptorMethod;
import org.franwork.core.util.proxy.ProxyCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProxyUtils {

	/**
	 * The private static org.slf4j.Logger object.
	 */
	private static Logger logger = LoggerFactory.getLogger(ProxyUtils.class);
	
	public static final String ORIGIN_INSTANCE_NAME = "originInstanceObj$";
	
	public static List<Class<? extends InterceptorMethod>> DEFAULT_EXTENDED_METHOD_TYPE = 
			new LinkedList<Class<? extends InterceptorMethod>>() {
		/**
		 * Generated SerialUID
		 */
		private static final long serialVersionUID = 2344248386232453409L;

		{
			this.add(NamePatternInterceptorMethod.class);
		}
	};
	
	public static Class<? extends InterceptorMethod> DEFAULT_BASIC_METHOD_TYPE = BasicInterceptorMethod.class;
	
	private ProxyUtils() {
		super();
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxyInstance(T originInstance, InterceptorMethod... interceptorMethods) throws Throwable{
		if (originInstance == null) {
			throw new IllegalArgumentException("The given origin instance object is NULL");
		}
		if (ArrayUtils.isEmpty(interceptorMethods)) {
			return originInstance;
		}
		T proxyInstance = (T) ProxyUtils.createProxyInstance(originInstance.getClass(), interceptorMethods);
		org.apache.commons.beanutils.BeanUtils.copyProperties(proxyInstance, originInstance);
		return proxyInstance;
	}
	
	public static <T> T createProxyInstance(Class<T> instanceType, InterceptorMethod... interceptorMethods) throws Throwable {
		if (instanceType == null) {
			throw new IllegalArgumentException("The given instance type class object is NULL");
		}
		if (ArrayUtils.isEmpty(interceptorMethods)) {
			return BeanUtils.newInstance(instanceType);
		}
		return ProxyUtils.createProxyInstance(instanceType, Arrays.asList(interceptorMethods));
	}
	
	public static <T> T createProxyInstance(Class<T> instanceType, Collection<InterceptorMethod> interceptorMethods) throws Throwable {
		if (instanceType == null) {
			throw new IllegalArgumentException("The given instance type class object is NULL");
		}
		if (CollectionUtils.isEmpty(interceptorMethods)) {
			return BeanUtils.newInstance(instanceType);
		}
		Object proxyInstance = Enhancer.create(instanceType, new ProxyCallback(interceptorMethods));
		logger.debug("ProxyInstance is created used specified interceptorMethods : " + interceptorMethods);
		return instanceType.cast(proxyInstance);
	}
}
