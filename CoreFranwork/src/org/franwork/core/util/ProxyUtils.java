package org.franwork.core.util;

import java.util.LinkedList;
import java.util.List;

import org.franwork.core.util.proxy.BasicInterceptorMethod;
import org.franwork.core.util.proxy.InterceptorMethod;
import org.franwork.core.util.proxy.NamePatternInterceptorMethod;
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
}
