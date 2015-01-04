package test.org.franwork.core;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.franwork.core.util.ProxyUtils;
import org.franwork.core.util.proxy.BasicInterceptorMethod;
import org.franwork.core.util.proxy.InterceptAfter;
import org.franwork.core.util.proxy.InterceptBefore;
import org.franwork.core.util.proxy.InterceptException;
import org.franwork.core.util.proxy.InterceptorMethod;
import org.franwork.core.util.proxy.NamePatternInterceptorMethod;
import org.franwork.core.util.proxy.ProxyInterceptor;
import org.franwork.core.util.proxy.ProxyMethod;
import org.junit.Test;

public class ProxyTest {
	
	@ProxyInterceptor(namePatterns = {"tast.*"})
	public static class Foo {
		
		@InterceptBefore
        public void doBefore(Object proxyObj, Method proxyMethod, Object[] paramObjs) {
        	System.out.println("doBefore todo... 1");
        	FooTest test = (FooTest) proxyObj;
//        	test.testProxyMethod("InterceptDoBefore1");
        }
        
        @InterceptAfter
        public void doAfter(Object proxyObj, Method proxyMethod, Object[] paramObjs, Object afterReturning) {
        	System.out.println("doAfter todo... 1" + afterReturning);
        	FooTest test = (FooTest) proxyObj;
//        	test.testProxyMethod("InterceptDoAfter1");
        }
        
        @InterceptException
        public void doException(Object proxyObj, Method proxyMethod, Object[] paramObjs, Throwable ex) {
        	System.out.println("WAWA Exception 1 : " + ex.getMessage());
        }
    }
	
	@ProxyInterceptor(namePatterns = {"tast.*"})
	public static class Foo2 {
		
		@InterceptBefore
        public void doBefore(Object proxyObj, Method proxyMethod, Object[] paramObjs) {
        	System.out.println("doBefore todo... 2");
        	FooTest test = (FooTest) proxyObj;
//        	test.testProxyMethod("InterceptDoBefore2");
        }
        
        @InterceptAfter
        public void doAfter(Object proxyObj, Method proxyMethod, Object[] paramObjs, Object afterReturning) {
        	System.out.println("doAfter todo... " + afterReturning);
        	FooTest test = (FooTest) proxyObj;
        	test.testProxyMethod("InterceptDoAfter2");
        }
        
        @InterceptException
        public void doException(Object proxyObj, Method proxyMethod, Object[] paramObjs, Throwable ex) {
        	System.out.println("WAWA Exception 2 : " + ex.getMessage());
        }
    }
	
	public static class FooTest {

        public String tast(int age) {
        	throw new RuntimeException("Hello Exception BABY");
//            System.out.println("test age :" + age);
//            return "tast age ***" + age;
        }
        
        @ProxyMethod
        public String testProxyMethod(String name) {
        	System.out.println("testProxyMethod todo... " + name);
        	System.out.println(tast(25));
            return "TestProxyMethod Returning";
        }
    }
 
    @Test
    public void testEnhance() throws Throwable {
    	try {
	    	InterceptorMethod inter = new NamePatternInterceptorMethod(new BasicInterceptorMethod(new Foo()));
	    	
	    	InterceptorMethod inter2 = new NamePatternInterceptorMethod(new BasicInterceptorMethod(new Foo2()));
    	
//    	Collection<InterceptorMethod> inters = ProxyUtil.createInterceptorMethods(new Foo());
//    	
//        Callback callback = new ProxyCallback(inters);
// 
//        FooTest created = (FooTest) Enhancer.create(FooTest.class, callback);
    	
    		FooTest created = ProxyUtils.createProxyInstance(FooTest.class, inter, inter2);
        
//        created.testProxyMethod("Frankie");
        
        	created.tast(5);
    	} catch (Exception ex) {
    		ex.printStackTrace();
    	}
    }
    
//    @Test
    public void testInterceptor() throws Exception {
//    	InterceptorMethods interceptorMethod = new InterceptorMethods(new Foo());
//    	System.out.println(interceptorMethod);
    	List<String> aaa = new LinkedList<String>();
    	aaa.add("aaa");
    	aaa.add("bbb");
    	aaa.add("ccc");
    	System.out.println(aaa);
    }
}
