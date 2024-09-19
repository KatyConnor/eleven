package com.hx.nine.eleven.core.core.bean;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.lang.commons.utils.CollectionUtils;
import com.hx.nine.eleven.core.annotations.BeanRegister;
import com.hx.nine.eleven.core.annotations.ConditionalOnBean;
import com.hx.nine.eleven.core.annotations.ConditionalOnClass;
import com.hx.nine.eleven.core.aop.VertxObjectProxy;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.VertxApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 初始化bean
 */
public class VertxBeanFactory {

	/**
	 * 创建 bean
	 *
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public static  <T> T initBean(Class<T> tClass) {
		ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
		// 获取bean创建之前处理类
		Set<Class<? extends ApplicationBeanRegister>> beanRegisters = DefaultVertxApplicationContext.build()
				.getSubTypesOfBeanClass(ApplicationBeanRegister.class);
		T bean = null;
		ApplicationBeanRegister beanRegister = null;
		Class<? extends ApplicationBeanRegister> beanRegisterClass = null;
		if (Optional.ofNullable(beanRegisters).isPresent() && beanRegisters.size() > 0){
			beanRegisterClass = beanRegisters.stream().filter(br -> {
				BeanRegister brAnnotation = br.getAnnotation(BeanRegister.class);
				Class<?> bClass = null;
				if (brAnnotation != null) {
					bClass = brAnnotation.value();
				}
				return bClass.getName().equals(tClass.getName());
			}).findFirst().orElseGet(null);
			if (beanRegisterClass != null) {
				beanRegister = ConstructorAccess.get(beanRegisterClass).newInstance();
			}
		}

		//检查onClass
		if (!checkOnClass(tClass)){
			return null;
		}

		// 初始化之前检查时候有其他类依赖
		if (!checkAfterBean(tClass)) {
			return null;
		}
		VertxObjectProxy vertxObjectProxy = VertxApplicationContextAware.getSubTypesOfBean(VertxObjectProxy.class);
		if (beanRegister != null) {
			beanRegister.before(DefaultVertxApplicationContext.build());
			bean = vertxObjectProxy.checkProxy(tClass)? vertxObjectProxy.newByteBuddyProxyInstancesss(tClass):constructorAccess.newInstance();
			beanRegister.setBean(bean);
			beanRegister.after(DefaultVertxApplicationContext.build());
			return (T) beanRegister.getBean();
		} else {
			bean =vertxObjectProxy.checkProxy(tClass)?vertxObjectProxy.newByteBuddyProxyInstancesss(tClass):constructorAccess.newInstance();
		}
		Optional.ofNullable(beanRegisterClass).ifPresent(br ->{
			beanRegisters.remove(br); // 删除处理之后的class
		});
		return bean;
	}

	public static Object invoke(String methodName, Object obj, Object... args) {
		MethodAccess method = MethodAccess.get(obj.getClass());
		return method.invoke(obj, methodName, args);
	}

	public static <T> T createBean(Class<T> tClass){
		ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
		return constructorAccess.newInstance();
	}

	private static boolean checkOnClass(Class<?> tClass){
		ConditionalOnClass conditionalOnClass = tClass.getAnnotation(ConditionalOnClass.class);
		if (Optional.ofNullable(conditionalOnClass).isPresent()) {
			Class<?>[] onClass = conditionalOnClass.value();
			List<Class<?>> list = Arrays.asList(onClass);
			for (Class<?> c : list){
				if (!isPresent(c.getName())){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param tClass
	 * @return
	 */
	private static boolean checkAfterBean(Class<?> tClass) {
		ConditionalOnBean conditionalAfterBean = tClass.getAnnotation(ConditionalOnBean.class);
		if (Optional.ofNullable(conditionalAfterBean).isPresent()) {
			boolean next = true;
			String[] beans = conditionalAfterBean.bean();
			if (beans != null && beans.length > 0) {
				for (String beanName : beans) {
					if (DefaultVertxApplicationContext.build().getBean(beanName) == null) {
						next = false;
					}
				}
			}
			Class<?>[] classes = conditionalAfterBean.value();
			if (classes != null && classes.length > 0) {
				for (Class<?> beanClass : classes) {
					if (VertxApplicationContextAware.getBean(beanClass) == null) {
						next = false;
					}
				}
			}
			if (!next) {
				ConcurrentLinkedQueue<Class<?>> afterBeansQueue = VertxApplicationContextAware.
						getBean(ConstantType.CONDITIONAL_ON_AFTER_BEAN);
				if (CollectionUtils.isEmpty(afterBeansQueue)) {
					afterBeansQueue = new ConcurrentLinkedQueue();
				}
				afterBeansQueue.add(tClass);
				DefaultVertxApplicationContext.build().addBean(ConstantType.CONDITIONAL_ON_AFTER_BEAN, afterBeansQueue,true);
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断类class 是否存在
	 * @param name
	 * @return
	 */
	public static boolean isPresent(String name) {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(name);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
}
