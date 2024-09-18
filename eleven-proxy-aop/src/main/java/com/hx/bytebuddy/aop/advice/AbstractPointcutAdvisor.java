package com.hx.bytebuddy.aop.advice;

import com.hx.bytebuddy.aop.ProxyTypeAnnotationEntity;
import com.hx.bytebuddy.aop.enums.AopProxyTypeEnums;
import com.hx.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.bytebuddy.aop.interceptor.jdk.MethodInvocationInterceptor;
import com.hx.bytebuddy.aop.util.Assert;
import com.hx.vertx.boot.core.bean.VertxBeanFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 添加切点，暂时只支持一模代理模式
 *
 * @author wml
 * @date 2023-04-02
 */
public abstract class AbstractPointcutAdvisor implements PointcutAdvisor {

	/**
	 * advice拦截实现
	 */
	private MethodInvocationInterceptor advice;
	/**
	 * 切点注入注解
	 */
	private final List<Class<? extends Annotation>> annotations;
	/**
	 * 设置拦截器以及代理模式  ProxyTypeAnnotationEntity
	 */
	private final Map<AopProxyTypeEnums,List<ProxyTypeAnnotationEntity>> aopProxyType;

	public AbstractPointcutAdvisor() {
		annotations = new ArrayList<>();
		aopProxyType = new LinkedHashMap<>();
	}

	/**
	 * 添加切点，将切点注解加入annotations集合中
	 */
	public abstract void init();

	/**
	 * 根据切点注解查找代理模式
	 * @param annotation
	 * @return
	 */
	public ProxyTypeAnnotationEntity getProxyTypeAnnotationEntity(Class<? extends Annotation> annotation) {
		return getProxyTypeEnums(annotation);
	}

	/**
	 * 根据切点注解查找代理模式
	 * @param annotation
	 * @return
	 */
	public ProxyTypeAnnotationEntity getProxyTypeAnnotationEntity(Annotation annotation) {
		Class<? extends Annotation> anClass = annotation.annotationType();
		return getProxyTypeEnums(anClass);
	}

	/**
	 * 设置拦截切点
	 *
	 * @param advice
	 * @return
	 */
	public AbstractPointcutAdvisor setAdvice(MethodInvocationInterceptor advice) {
		Assert.notNull(advice, "MethodInterceptor advice must not be null");
		this.advice = advice;
		return this;
	}

	/**
	 * 添加切点，代理模式为默认bytebuddy代理模式
	 *
	 * @param annotation
	 * @return
	 */
	public AbstractPointcutAdvisor addAnnotation(Class<? extends Annotation> annotation, MethodInterceptor methodInterceptor) {
		Assert.notNull(annotation, "advice pointcut Annotation type must not be null");
		this.annotations.add(annotation);
		List<ProxyTypeAnnotationEntity> aopProxyAnnotation = aopProxyType.get(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		if (!Optional.ofNullable(aopProxyAnnotation).isPresent()) {
			aopProxyAnnotation = new ArrayList<>();
		}
		ProxyTypeAnnotationEntity annotationEntity = new ProxyTypeAnnotationEntity();
		annotationEntity.setAnnotation(annotation);
		annotationEntity.setMethodInterceptor(methodInterceptor);
		annotationEntity.setProxyType(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY,aopProxyAnnotation);
		return this;
	}

	public AbstractPointcutAdvisor addAnnotation(Class<? extends Annotation> annotation,Class<? extends MethodInterceptor> interceptor) {
		Assert.notNull(annotation, "advice pointcut Annotation type must not be null");
		this.annotations.add(annotation);
		List<ProxyTypeAnnotationEntity> aopProxyAnnotation = aopProxyType.get(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		if (!Optional.ofNullable(aopProxyAnnotation).isPresent()) {
			aopProxyAnnotation = new ArrayList<>();
		}
		ProxyTypeAnnotationEntity annotationEntity = new ProxyTypeAnnotationEntity();
		annotationEntity.setAnnotation(annotation);
		MethodInterceptor methodInterceptor = VertxBeanFactory.createBean(interceptor);
		annotationEntity.setMethodInterceptor(methodInterceptor);
		annotationEntity.setProxyType(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY,aopProxyAnnotation);
		return this;
	}

	/**
	 * 添加切点，指定代理模式
	 *
	 * @param annotation        切点
	 * @param aopProxyTypeEnums 代理模式
	 * @return
	 */
	public AbstractPointcutAdvisor addAnnotation(Class<? extends Annotation> annotation,
												 Class<? extends MethodInterceptor> interceptor,
												 AopProxyTypeEnums aopProxyTypeEnums) {
		Assert.notNull(annotation, "advice pointcut Annotation type must not be null");
		this.annotations.add(annotation);
		List<ProxyTypeAnnotationEntity> aopProxyAnnotation = aopProxyType.get(aopProxyTypeEnums);
		if (!Optional.ofNullable(aopProxyAnnotation).isPresent()) {
			aopProxyAnnotation = new ArrayList<>();
		}
		ProxyTypeAnnotationEntity annotationEntity = new ProxyTypeAnnotationEntity();
		annotationEntity.setAnnotation(annotation);
		MethodInterceptor methodInterceptor = VertxBeanFactory.createBean(interceptor);
		annotationEntity.setMethodInterceptor(methodInterceptor);
		annotationEntity.setProxyType(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(aopProxyTypeEnums,aopProxyAnnotation);
		return this;
	}

	public AbstractPointcutAdvisor addAnnotation(Class<? extends Annotation> annotation,
												 MethodInterceptor methodInterceptor,
												 AopProxyTypeEnums aopProxyTypeEnums) {
		Assert.notNull(annotation, "advice pointcut Annotation type must not be null");
		this.annotations.add(annotation);
		List<ProxyTypeAnnotationEntity> aopProxyAnnotation = aopProxyType.get(aopProxyTypeEnums);
		if (!Optional.ofNullable(aopProxyAnnotation).isPresent()) {
			aopProxyAnnotation = new ArrayList<>();
		}
		ProxyTypeAnnotationEntity annotationEntity = new ProxyTypeAnnotationEntity();
		annotationEntity.setAnnotation(annotation);
		annotationEntity.setMethodInterceptor(methodInterceptor);
		annotationEntity.setProxyType(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(aopProxyTypeEnums,aopProxyAnnotation);
		return this;
	}

	@Override
	public MethodInvocationInterceptor getAdvice() {
		return this.advice;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return new AnnotationMethodMatcher(annotations);
	}

	public Map<AopProxyTypeEnums, List<ProxyTypeAnnotationEntity>> getAopProxyType() {
		return aopProxyType;
	}

	public List<Class<? extends Annotation>> getAnnotations() {
		return annotations;
	}

	private ProxyTypeAnnotationEntity getProxyTypeEnums(Class<? extends Annotation> annotation){
		AtomicReference<ProxyTypeAnnotationEntity> aopProxyTypeEnums = new AtomicReference<>();
		aopProxyType.forEach((k, v) -> {
			for (ProxyTypeAnnotationEntity entity : v) {
				if (entity.getAnnotation().getName().equals(annotation.getName())) {
					aopProxyTypeEnums.set(entity);
				}
			}
		});
		return aopProxyTypeEnums.get();
	}
}
