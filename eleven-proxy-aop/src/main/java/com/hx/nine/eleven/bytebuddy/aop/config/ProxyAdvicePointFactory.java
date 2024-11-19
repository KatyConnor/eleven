package com.hx.nine.eleven.bytebuddy.aop.config;

import com.hx.nine.eleven.bytebuddy.aop.DynamicObjectProxy;
import com.hx.nine.eleven.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import com.hx.nine.eleven.core.annotations.Order;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.bean.ElevenBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import org.reflections.Reflections;

import java.util.*;

/**
 * 初始化 AbstractPointcutAdvisor 实现类，注入 AOP 代理切点
 *
 * @author wml
 * @date 2023-04-14
 */
@Order(order = -9998)
public class ProxyAdvicePointFactory implements ApplicationAnnotationFactory {
	@Override
	public void loadAnnotations(Reflections reflections) throws Exception {
		// 实例化 ElevenObjectProxy 接口实现类 DynamicObjectProxy 并注入到容器中
		DynamicObjectProxy objectProxy = ElevenBeanFactory.createBean(DynamicObjectProxy.class);
		DefaultElevenApplicationContext.build().addSubTypesOfBean(objectProxy);

		// 添加AOP切点,初始化AOP代理拦截器
		AbstractPointcutAdvisor pointcutAdvisor = ElevenApplicationContextAware.getBean(AbstractPointcutAdvisor.class);
		if (!Optional.ofNullable(pointcutAdvisor).isPresent()){
			// 添加AOP切点,初始化AOP代理拦截器
			pointcutAdvisor =  ElevenBeanFactory.createBean(AbstractPointcutAdvisor.class);
			DefaultElevenApplicationContext.build().addBean(pointcutAdvisor);
		}

	}
}
