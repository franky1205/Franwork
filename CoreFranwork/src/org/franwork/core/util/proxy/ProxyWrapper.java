package org.franwork.core.util.proxy;

public interface ProxyWrapper<T> {

	public void setOriginProxyObj(T originProxyObj);
	
	public T getOriginProxyObj();
}
