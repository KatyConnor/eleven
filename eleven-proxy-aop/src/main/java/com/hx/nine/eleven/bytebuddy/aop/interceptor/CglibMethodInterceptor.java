package com.hx.nine.eleven.bytebuddy.aop.interceptor;

import com.hx.nine.eleven.bytebuddy.aop.invoke.MethodInvocation;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * cglib拦截器实现
 * @author wml
 * @date 2023-04-06
 */
public class CglibMethodInterceptor implements MethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(CglibMethodInterceptor.class);

	private final com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor methodInterceptor;

	public CglibMethodInterceptor(com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor methodInterceptor){
		this.methodInterceptor = methodInterceptor;
	}


	@Override
	public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("---------进入 [{}:{}]方法开始执行逻辑---------",obj.getClass().getName(),method.getName());
		}
		MethodInvocation invocation = new MethodInvocation(methodProxy,objects,null,obj);
		invocation.setMethod(method);
		Object result = this.methodInterceptor.intercept(invocation);
		if (LOGGER.isDebugEnabled()){
			LOGGER.debug("---------方法 [{}:{}] 执行完毕---------",obj.getClass().getName(),method.getName());
		}
		return result;
	}
}
