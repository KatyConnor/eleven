package hx.nine.eleven.bytebuddy.aop.interceptor;

import hx.nine.eleven.bytebuddy.aop.invoke.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK代理拦截,切点执行器
 * @author wml
 * @date 2023-04-12
 */
public class JDKMethodInvocationHandler implements InvocationHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(JDKMethodInvocationHandler.class);

	private final MethodInterceptor methodInterceptor;

	private Object target;

	public JDKMethodInvocationHandler(MethodInterceptor methodInterceptor, Object target){
		this.methodInterceptor = methodInterceptor;
		this.target = target;
	}

	/**
	 * 方法拦截接口
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable{
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("---------进入 [{}:{}]方法开始执行逻辑---------",proxy.getClass().getSuperclass().getName(),method.getName());
		}
		MethodInvocation invocation = new MethodInvocation(method,args,proxy,this.target);
		Object result = this.methodInterceptor.intercept(invocation);
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("---------方法 [{}:{}] 执行完毕---------",proxy.getClass().getSuperclass().getName(),method.getName());
		}
		return result;
	}
}
