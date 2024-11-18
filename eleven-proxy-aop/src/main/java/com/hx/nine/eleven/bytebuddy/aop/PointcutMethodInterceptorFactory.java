package com.hx.nine.eleven.bytebuddy.aop;

import com.hx.nine.eleven.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import com.hx.nine.eleven.bytebuddy.aop.enums.AopProxyTypeEnums;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.bean.ElevenBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 * @auth wml
 * @date 2024/11/15
 */
public class PointcutMethodInterceptorFactory {

	public static void addAnnotation(Class<? extends Annotation> annotation, MethodInterceptor methodInterceptor){
		AbstractPointcutAdvisor pointcutAdvisor = getAbstractPointcutAdvisor();
		pointcutAdvisor.addAnnotation(annotation,methodInterceptor);
	}

	public static void addAnnotation(Class<? extends Annotation> annotation, Class<? extends MethodInterceptor> interceptor){
		AbstractPointcutAdvisor pointcutAdvisor = getAbstractPointcutAdvisor();
		pointcutAdvisor.addAnnotation(annotation,interceptor);
	}

	public static void addAnnotation(Class<? extends Annotation> annotation, Class<? extends MethodInterceptor> interceptor,
					   AopProxyTypeEnums aopProxyTypeEnums){
		AbstractPointcutAdvisor pointcutAdvisor = getAbstractPointcutAdvisor();
		pointcutAdvisor.addAnnotation(annotation,interceptor,aopProxyTypeEnums);

	}

	public static void addAnnotation(Class<? extends Annotation> annotation, MethodInterceptor methodInterceptor,
					   AopProxyTypeEnums aopProxyTypeEnums){
		AbstractPointcutAdvisor pointcutAdvisor = getAbstractPointcutAdvisor();
		pointcutAdvisor.addAnnotation(annotation,methodInterceptor,aopProxyTypeEnums);

	}

	private static AbstractPointcutAdvisor getAbstractPointcutAdvisor(){
		AbstractPointcutAdvisor pointcutAdvisor = ElevenApplicationContextAware.getBean(AbstractPointcutAdvisor.class);
		if (!Optional.ofNullable(pointcutAdvisor).isPresent()){
			// 添加AOP切点,初始化AOP代理拦截器
			pointcutAdvisor =  ElevenBeanFactory.createBean(AbstractPointcutAdvisor.class);
			DefaultElevenApplicationContext.build().addSubTypesOfBean(pointcutAdvisor);
		}
		return pointcutAdvisor;
	}
}
