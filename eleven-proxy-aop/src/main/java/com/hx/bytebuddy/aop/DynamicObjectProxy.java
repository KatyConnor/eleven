package com.hx.bytebuddy.aop;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import com.hx.bytebuddy.aop.advice.AopAdvice;
import com.hx.bytebuddy.aop.enums.AopProxyTypeEnums;
import com.hx.bytebuddy.aop.interceptor.CglibMethodInterceptor;
import com.hx.bytebuddy.aop.interceptor.jdk.MethodInvocationInterceptor;
import com.hx.bytebuddy.aop.util.ProxyUtil;
import com.hx.vertx.boot.annotations.Order;
import com.hx.vertx.boot.annotations.SubComponent;
import com.hx.vertx.boot.aop.VertxObjectProxy;
import com.hx.vertx.boot.constant.ConstantType;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.core.context.DefaultVertxApplicationContext;
import net.sf.cglib.proxy.Enhancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Optional;

public class DynamicObjectProxy extends VertxObjectProxy {

	private final static Logger LOGGER = LoggerFactory.getLogger(DynamicObjectProxy.class);
	/**
	 * 通过添加切点代理执行某个方法
	 *
	 * @param object     目标对象
	 * @param methodName 执行方法名称
	 * @param paramTypes 方法参数类型
	 * @param args       方法参数
	 * @return
	 */
	public static Object invoke(Object object, String methodName, Class[] paramTypes, Object... args) {
		MethodAccess methodAccess = MethodAccess.get(object.getClass());
		AopAdvice advice = DefaultVertxApplicationContext.build().getBean(ConstantType.DYNAMIC_PROXY_ADVICE, AopAdvice.class);
		// 切点，执行前处理
		advice.adviceBefore(object, methodName, paramTypes, args);
		Object result = methodAccess.invoke(object, methodName, paramTypes, args);
		// 切点执行后处理
		advice.adviceAfter(result);
		return result;
	}

	/**
	 * 通过添加切点代理执行某个方法
	 *
	 * @param object     目标对象
	 * @param methodName 执行方法
	 * @param args       方法参数
	 * @return
	 */
	public static Object invoke(Object object, String methodName, Object... args) {
		MethodAccess methodAccess = MethodAccess.get(object.getClass());
		AopAdvice advice = DefaultVertxApplicationContext.build().getBean(ConstantType.DYNAMIC_PROXY_ADVICE, AopAdvice.class);
		// 切点，执行前处理
		advice.adviceBefore(object, methodName, null, args);
		Object result = methodAccess.invoke(object, methodName, args);
		// 切点执行后处理
		advice.adviceAfter(result);
		return result;
	}

	/**
	 * 创建JDK动态代理类
	 * 目标对象有实现类的代理,JDK动态代理，需要有接口和接口实现类
	 *
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public static <T> T newJdkProxyInstance(Class<T> tClass) {
		AbstractPointcutAdvisor advisor = DefaultVertxApplicationContext.build()
				.getBean(ConstantType.DYNAMIC_PROXY_POINTCUT_ADVISOR, AbstractPointcutAdvisor.class);
		MethodInvocationInterceptor methodInterceptor = advisor.getAdvice();
		return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), tClass.getInterfaces(), methodInterceptor);
	}

	/**
	 * 创建cglib代理类
	 *
	 * @param target
	 * @param <T>
	 * @return
	 */
	public static <T> T createCglibProxyInstance(Class<T> target) {
		// 1.cglib工具类
		Enhancer enhancer = new Enhancer();
		// 2.设置父类
		enhancer.setSuperclass(target);
		// 3.设置回调函数
		enhancer.setCallback(DefaultVertxApplicationContext.build()
				.getSubTypesOfBean(CglibMethodInterceptor.class));
		return (T) enhancer.create();
	}

	/**
	 * 多数据源代理匹配
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> boolean checkProxy(Class<T> tClass) {
		AbstractPointcutAdvisor advisor = DefaultVertxApplicationContext.build()
				.getSubTypesOfBean(AbstractPointcutAdvisor.class);
		Annotation annotation = advisor.getMethodMatcher().matches(tClass);
		return annotation != null;
	}

	/**
	 * 创建一个ByteBuddy代理类
	 *
	 * @param aClass
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> T newByteBuddyProxyInstancesss(Class<T> aClass) {
		// 获取创建类的
		AbstractPointcutAdvisor advisor = DefaultVertxApplicationContext.build()
				.getSubTypesOfBean(AbstractPointcutAdvisor.class);
		Annotation annotation = advisor.getMethodMatcher().matches(aClass);
		if (!Optional.ofNullable(annotation).isPresent()) {
			LOGGER.warn("当前类[{}]没有添加AOP切点",aClass.getName());
			return null;
		}
		//查找代理类型
		ProxyTypeAnnotationEntity proxyTypeAnnotationEntity = advisor.getProxyTypeAnnotationEntity(annotation);
		AopProxyTypeEnums proxyTypeEnums = proxyTypeAnnotationEntity.getProxyType();
		ObjectProxyCreator proxyCreator = ProxyUtil.getInstance(proxyTypeEnums.getProxyClass());
		MethodAccess methodAccess = MethodAccess.get(proxyTypeEnums.getProxyClass());
		if (Optional.ofNullable(proxyTypeEnums.getInterceptor()).isPresent()) {
			// byteBuddy interceptor模式拦截
			return (T) methodAccess.invoke(proxyCreator, proxyTypeEnums.getMethod(),
					aClass,proxyTypeAnnotationEntity.getMethodInterceptor());
		}
		// byteBuddy advice模式拦截
		return (T) methodAccess.invoke(proxyCreator, proxyTypeEnums.getMethod(), aClass);
	}
}
