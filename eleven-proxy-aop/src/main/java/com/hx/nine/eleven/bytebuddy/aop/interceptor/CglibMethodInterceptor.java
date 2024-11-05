package com.hx.nine.eleven.bytebuddy.aop.interceptor;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib拦截器实现
 * @author wml
 * @date 2023-04-06
 */
public abstract class CglibMethodInterceptor implements MethodInterceptor {

	@Override
	public Object intercept(Object obj, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
		doInterceptBefore(obj,method,objects,methodProxy);
		Object result = method.invoke(obj,objects);
		doInterceptAfter(obj,method,objects,methodProxy);
		return result;
	}

	public abstract Object doInterceptBefore(Object o, Method method, Object[] objects, MethodProxy methodProxy);

	public abstract Object doInterceptAfter(Object o, Method method, Object[] objects, MethodProxy methodProxy);
}
