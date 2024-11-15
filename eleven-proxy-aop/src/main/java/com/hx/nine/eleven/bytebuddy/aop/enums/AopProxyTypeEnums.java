package com.hx.nine.eleven.bytebuddy.aop.enums;
import com.hx.nine.eleven.bytebuddy.aop.creator.ObjectProxyCreator;
import com.hx.nine.eleven.bytebuddy.aop.creator.ByteBuddyCreator;
import com.hx.nine.eleven.bytebuddy.aop.creator.CglibProxyCreator;
import com.hx.nine.eleven.bytebuddy.aop.creator.JdkProxyCreator;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.advice.AdvisorMethodInterceptor;

import java.util.Arrays;

public enum AopProxyTypeEnums {

	BYTEBUDDY_INTERCEPTOR_PROXY("BYTEBUDDY_PROXY",ByteBuddyCreator.class,
			"createInterceptorProxy", MethodInterceptor.class),
	BYTEBUDDY_ADVICE_PROXY("BYTEBUDDY_ADVICE_PROXY",ByteBuddyCreator.class,
			"createAdviceInterceptorProxy", AdvisorMethodInterceptor.class),
	JDK_INTERCEPTOR_PROXY("JDK_PROXY", JdkProxyCreator.class,
			"createInterceptorProxy", MethodInterceptor.class),
	CGLIB_INTERCEPTOR_PROXY("CGLIB_PROXY", CglibProxyCreator.class,
			"createInterceptorProxy", MethodInterceptor.class);

	private String code;
	private Class<? extends ObjectProxyCreator> proxyClass;
	private String method;
	private Class<?> interceptor;

	AopProxyTypeEnums(String code,Class<? extends ObjectProxyCreator> proxyClass,String method,Class<?> interceptor) {
		this.code = code;
		this.proxyClass = proxyClass;
		this.method = method;
		this.interceptor = interceptor;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Class<? extends ObjectProxyCreator> getProxyClass() {
		return proxyClass;
	}

	public void setProxyClass(Class<? extends ObjectProxyCreator> proxyClass) {
		this.proxyClass = proxyClass;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Class<?> getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(Class<?> interceptor) {
		this.interceptor = interceptor;
	}

	public static AopProxyTypeEnums getByCode(String code){
		return Arrays.stream(AopProxyTypeEnums.values()).filter(e -> e.getCode().equals(code)).findFirst().orElseGet(null);
	}
}
