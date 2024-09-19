package com.hx.nine.eleven.bytebuddy.aop;

import com.hx.nine.eleven.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import com.hx.nine.eleven.bytebuddy.aop.exception.ProxyAdviceRunException;
import com.hx.nine.eleven.core.annotations.Order;
import com.hx.nine.eleven.core.core.bean.VertxBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import com.hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import org.reflections.Reflections;

import java.util.*;

/**
 * 初始化AOP代理切点
 *
 * @author wml
 * @date 2023-04-14
 */
@Order(order = -9998)
public class ProxyAdvicePointFactory implements ApplicationAnnotationFactory {
	@Override
	public void loadAnnotations(Reflections reflections) throws Exception {
		//
		DynamicObjectProxy objectProxy = VertxBeanFactory.createBean(DynamicObjectProxy.class);
		DefaultVertxApplicationContext.build().addSubTypesOfBean(objectProxy);
		// 添加AOP切点,初始化AOP代理拦截器
		Set<Class<? extends AbstractPointcutAdvisor>> advisores = reflections.getSubTypesOf(AbstractPointcutAdvisor.class);
		if (Optional.ofNullable(advisores).isPresent()) {
			if (advisores.size() != 1){
				throw new ProxyAdviceRunException("advisor 不唯一 ");
			}
			Class<? extends AbstractPointcutAdvisor> advisorClass = advisores.stream().findFirst().get();
			AbstractPointcutAdvisor advisor = VertxBeanFactory.createBean(advisorClass);
			advisor.init();
			DefaultVertxApplicationContext.build().addSubTypesOfBean(advisor);
		}
	}
}
