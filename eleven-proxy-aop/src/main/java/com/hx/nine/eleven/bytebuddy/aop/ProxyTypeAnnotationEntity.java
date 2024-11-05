package com.hx.nine.eleven.bytebuddy.aop;

import com.hx.nine.eleven.bytebuddy.aop.enums.AopProxyTypeEnums;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;

import java.lang.annotation.Annotation;

public class ProxyTypeAnnotationEntity {

	/**
	 * 代理模式
	 */
	private AopProxyTypeEnums proxyType;

	/**
	 * 切点注解
	 */
	private Class<? extends Annotation> annotation;

	/**
	 * 切点拦截器实现
	 */
	private MethodInterceptor methodInterceptor;

	public AopProxyTypeEnums getProxyType() {
		return proxyType;
	}

	public void setProxyType(AopProxyTypeEnums proxyType) {
		this.proxyType = proxyType;
	}

	public Class<? extends Annotation> getAnnotation() {
		return annotation;
	}

	public void setAnnotation(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	public MethodInterceptor getMethodInterceptor() {
		return methodInterceptor;
	}

	public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
		this.methodInterceptor = methodInterceptor;
	}
}
