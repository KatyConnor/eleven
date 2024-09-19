package com.hx.nine.eleven.bytebuddy.aop.advice;

import java.lang.annotation.Annotation;

/**
 * AOP切面切点匹配
 * @author wml
 * @date 2023-04-02
 */
public interface MethodMatcher {

	Annotation matches(Class<?> targetClass);
}
