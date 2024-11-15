package com.hx.nine.eleven.bytebuddy.aop.advice;

import com.hx.nine.eleven.bytebuddy.aop.ProxyTypeAnnotationEntity;
import com.hx.nine.eleven.bytebuddy.aop.enums.AopProxyTypeEnums;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.nine.eleven.bytebuddy.aop.util.Assert;
import com.hx.nine.eleven.core.core.bean.ElevenBeanFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 添加切点，暂时只支持一模代理模式
 *
 * @author wml
 * @date 2023-04-02
 */
public class AbstractPointcutAdvisor implements PointcutAdvisor {

	/**
	 * 注解切点
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
	 * 注入注解切点，使用默认的代理模式创建代理对象
	 * 默认使用 {AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY} 代理模式
	 * @param annotation           注解切点
	 * @param methodInterceptor    切点拦截逻辑实现的拦截器
	 * @return
	 */
	public AbstractPointcutAdvisor addAnnotation(Class<? extends Annotation> annotation,
												 MethodInterceptor methodInterceptor) {
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

	/**
	 * 注入注解切点，使用默认的代理模式创建代理对象
	 * 默认使用 {AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY} 代理模式
	 *
	 * @param annotation     注解切点
	 * @param interceptor    切点拦截逻辑实现的拦截器
	 * @return
	 */
	public AbstractPointcutAdvisor addAnnotation(Class<? extends Annotation> annotation,
												 Class<? extends MethodInterceptor> interceptor) {
		Assert.notNull(annotation, "advice pointcut Annotation type must not be null");
		this.annotations.add(annotation);
		List<ProxyTypeAnnotationEntity> aopProxyAnnotation = aopProxyType.get(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		if (!Optional.ofNullable(aopProxyAnnotation).isPresent()) {
			aopProxyAnnotation = new ArrayList<>();
		}
		ProxyTypeAnnotationEntity annotationEntity = new ProxyTypeAnnotationEntity();
		annotationEntity.setAnnotation(annotation);
		MethodInterceptor methodInterceptor = ElevenBeanFactory.createBean(interceptor);
		annotationEntity.setMethodInterceptor(methodInterceptor);
		annotationEntity.setProxyType(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(AopProxyTypeEnums.BYTEBUDDY_INTERCEPTOR_PROXY,aopProxyAnnotation);
		return this;
	}

	/**
	 * 注入注解切点，指定代理模式
	 *
	 * @param annotation        注解切点
	 * @param interceptor       切点拦截逻辑实现的拦截器
	 * @param aopProxyTypeEnums 创建代理对象的代理模式
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
		MethodInterceptor methodInterceptor = ElevenBeanFactory.createBean(interceptor);
		annotationEntity.setMethodInterceptor(methodInterceptor);
		annotationEntity.setProxyType(aopProxyTypeEnums);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(aopProxyTypeEnums,aopProxyAnnotation);
		return this;
	}

	/**
	 * 注入注解切点
	 *
	 * @param annotation          注解切点
	 * @param methodInterceptor   切点拦截逻辑实现的拦截器
	 * @param aopProxyTypeEnums   创建代理对象的代理模式
	 * @return
	 */
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
		annotationEntity.setProxyType(aopProxyTypeEnums);
		aopProxyAnnotation.add(annotationEntity);
		aopProxyType.put(aopProxyTypeEnums,aopProxyAnnotation);
		return this;
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
