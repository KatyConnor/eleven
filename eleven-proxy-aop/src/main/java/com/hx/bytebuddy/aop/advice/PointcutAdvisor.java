package com.hx.nine.eleven.bytebuddy.aop.advice;

import com.hx.nine.eleven.bytebuddy.aop.interceptor.jdk.MethodInvocationInterceptor;

public interface PointcutAdvisor {

	/**
	 * 获取拦截
	 * @return
	 */
	MethodInvocationInterceptor getAdvice();

	/**
	 * 获取切点方法
	 * @return
	 */
	MethodMatcher getMethodMatcher();
}
