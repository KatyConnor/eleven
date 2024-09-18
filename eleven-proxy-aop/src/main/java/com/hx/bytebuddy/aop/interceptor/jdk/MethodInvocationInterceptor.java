package com.hx.bytebuddy.aop.interceptor.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * JDK代理拦截,切点执行器
 * @author wml
 * @date 2023-04-12
 */
public interface MethodInvocationInterceptor  extends InvocationHandler {

	/**
	 * 方法拦截接口
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	@Override
	Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
