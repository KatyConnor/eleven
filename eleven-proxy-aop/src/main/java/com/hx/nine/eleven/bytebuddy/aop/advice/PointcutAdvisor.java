package com.hx.nine.eleven.bytebuddy.aop.advice;

public interface PointcutAdvisor {

	/**
	 * 获取切点方法
	 * @return
	 */
	MethodMatcher getMethodMatcher();
}
