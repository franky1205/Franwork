package org.franwork.core.util.proxy;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Interceptor executing rule is defined by proxy method's name pattern regular expression.
 * 
 * No matter whether the executed method is annotated with ProxyMethod or not.
 * 
 * @author Frankie
 *
 */
public class NamePatternInterceptorMethod extends ExtendedInterceptorMethod {

	/**
	 * The regular expression name patterns.
	 */
	protected String[] namePatterns;
	
	public NamePatternInterceptorMethod(InterceptorMethod interceptorMethod) throws ProxyInterceptorException {
		super(interceptorMethod);
	}
	
	public String[] getNamePatterns() {
		return namePatterns;
	}
	
	public void setNamePatterns(String[] namePatterns) {
		this.namePatterns = namePatterns;
	}
	
	@Override
	public void setExtendedInfo(Object interceptor) throws ProxyInterceptorException {
		ProxyInterceptor interceptorAnno = interceptor.getClass().getAnnotation(ProxyInterceptor.class);
		this.namePatterns = interceptorAnno.namePatterns();
	}

	@Override
	public Boolean isExecutedInterceptor(ProxyMethod methodAnno, Method method) {
		if (!this.interceptorMethod.isExecutedInterceptor(methodAnno, method)) {
			return Boolean.FALSE;
		}
		if (ArrayUtils.isEmpty(this.namePatterns)) {
			return Boolean.FALSE;
		}
		String annoName = (methodAnno == null ? StringUtils.EMPTY : methodAnno.name());
		String methodName = (StringUtils.isBlank(annoName) ? method.getName() : annoName);
		for (String namePattern : this.namePatterns) {
			if (Pattern.matches(namePattern, methodName)) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public String toString() {
		return this.interceptorMethod.toString() + " -> NamePatternInterceptorMethod [namePatterns=" + Arrays.toString(namePatterns) + "]";
	}
}
