package com.hx.nine.eleven.bytebuddy.aop.interceptor.advice;

import java.lang.reflect.Method;

/**
 * bytebuddy代理； advice 拦截器
 * @author wml
 * @date 2023-04-12
 */
public interface AdviceInterceptor {

	/**
	 * 标方法执行之前
	 *
	 * @param target    被代理的目标对象
	 * @param proxy     目标对象生成的代理对象
	 * @param method    被代理的目标对象当前执行方法
	 * @param arguments 当前执行方法参数
	 */
	void onMethodEnter(Object target, Object proxy, Method method, Object[] arguments);

	/**
	 * 方法执行之后
	 *
	 * @param target    被代理的目标对象
	 * @param proxy     目标对象生成的代理对象
	 * @param method    被代理的目标对象当前执行方法
	 * @param arguments 当前执行方法参数
	 * @param ret       当前执行方法返回对象
	 */
	void onMethodExit(Object target, Object proxy, Method method, Object[] arguments, Object ret);

	/**
	 * 方法执行抛出异常
	 *
	 * @param target    被代理的目标对象
	 * @param proxy     目标对象生成的代理对象
	 * @param method    被代理的目标对象当前执行方法
	 * @param arguments 当前执行方法参数
	 * @param ret       当前执行方法返回对象
	 * @param throwable 当前方法执行抛出异常
	 */
	void onMethodException(Object target, Object proxy, Method method, Object[] arguments, Object ret, Throwable throwable);
}
