package com.hx.nine.eleven.bytebuddy.aop.creator;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.MethodInterceptor;
import com.hx.nine.eleven.bytebuddy.aop.interceptor.JDKMethodInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * JDK 代理创建
 * @auth wml
 * @date 2024/11/14
 */
public class JdkProxyCreator implements ObjectProxyCreator {

	private final static Logger LOGGER = LoggerFactory.getLogger(JdkProxyCreator.class);

	@Override
	public <T> T createInterceptorProxy(Class<T> target, MethodInterceptor interceptor) {
		return createProxy(Thread.currentThread().getContextClassLoader(),target, interceptor);
	}

	@Override
	public <T> T createInterceptorProxy(ClassLoader classLoader, Class<T> target, MethodInterceptor interceptor) {
		return createProxy(classLoader, target, interceptor);
	}

	@Override
	public <T> T createAdviceInterceptorProxy(Class<T> target) {
		return createAdviceInterceptorProxy(Thread.currentThread().getContextClassLoader(),target);
	}

	@Override
	public <T> T createAdviceInterceptorProxy(ClassLoader classLoader, Class<T> target) {
		return createAdviceProxy(classLoader,target);
	}


	/**
	 * 目标对象有实现类的代理,JDK动态代理，需要有接口和接口实现类
	 *
	 * @TODO issue：1 目前指定MethodInvocationInterceptor的拦截器实现只能有一个，不符合实际场景使用，需要根据用户指定的源类和拦截实现来创建代理对象
	 *
	 * @author wml
	 * @data   2024-11-15
	 * @des    <p>
	 *     修复 issue：目前指定MethodInvocationInterceptor的拦截器实现只能有一个，不符合实际场景使用，
	 *     需要根据用户指定的源类和拦截实现来创建代理对象,代理拦截器实现统一 byteBuddy 使用统一个
	 *     {MethodInterceptor}
	 * </p>
	 *
	 * @param classLoader   类加载器
	 * @param tClass        目标类
	 * @param interceptor   拦截器实现
	 * @param <T>           目标类类型
	 * @return              返回创建的代理类
	 */
	private <T> T createProxy(ClassLoader classLoader, Class<T> tClass, MethodInterceptor interceptor) {
		try {
			ConstructorAccess constructorAccess = ConstructorAccess.get(tClass);
			Object target = constructorAccess.newInstance();
			InvocationHandler handler = new JDKMethodInvocationHandler(interceptor,target);
			return (T) Proxy.newProxyInstance(classLoader, target.getClass().getInterfaces(), handler);
		} catch (Exception e) {
			LOGGER.error("创建代理类对象失败: {}",e);
			return null;
		}
	}

	/**
	 * 创建advice类型代理类
	 * @param target  目标代理类
	 * @param <T>     代理类型
	 * @return        返回生成的代理类
	 */
	private <T> T createAdviceProxy(ClassLoader classLoader,Class<T> target){
		throw new RuntimeException("站不支持该类型代理");
	}
}
