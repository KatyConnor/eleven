package com.hx.nine.eleven.bytebuddy.aop.interceptor.advice;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.nine.eleven.bytebuddy.aop.creator.ObjectProxyCreator;
import com.hx.nine.eleven.bytebuddy.aop.ProxyTypeAnnotationEntity;
import com.hx.nine.eleven.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import com.hx.nine.eleven.bytebuddy.aop.enums.AopProxyTypeEnums;
import com.hx.nine.eleven.bytebuddy.aop.util.ProxyUtil;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;

/**
 * bytebuddy代理：advice 模式拦截，此模式默认拦截被代理类的所有方法，因此AdvisorMethodInterceptor中会根据指定的注解切点判断是否执行
 * 代理操作
 * @author wml
 * @date 2023-04-12
 */
public class AdvisorMethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvisorMethodInterceptor.class);

	/**
	 * 目标方法执行之前
	 * @TODO issue： 切点注解只能有一个，存在多个的话可能会随机寻炸一个存在的代理进行执行
	 * @param target    被代理的目标对象
	 * @param proxy     目标对象生成的代理对象
	 * @param method    被代理的目标对象当前执行方法
	 * @param arguments 当前执行方法参数
	 *
	 */
	@Advice.OnMethodEnter
	void onMethodEnter(@Advice.Origin Object target ,@Advice.This Object proxy,@Advice.Origin Method method,
					   @Advice.AllArguments Object[] arguments){
		LOGGER.info("进入[{}.{}]方法",target.getClass().getName(),method.getName());

		AbstractPointcutAdvisor pointcutAdvisor = ElevenApplicationContextAware.getBean(AbstractPointcutAdvisor.class);

		Class<?> tClass = target.getClass();
		Annotation annotation = pointcutAdvisor.getMethodMatcher().matches(tClass);

		if (!Optional.ofNullable(annotation).isPresent()) {
			LOGGER.warn("当前类[{}]没有添加AOP切点", tClass.getName());
			return;
		}

		ProxyTypeAnnotationEntity proxyTypeAnnotationEntity = pointcutAdvisor.getProxyTypeAnnotationEntity(annotation);
		AdviceInterceptor interceptor = proxyTypeAnnotationEntity.getAdviceInterceptor();
		interceptor.onMethodEnter(target,proxy,method,arguments);
	}

	/**
	 * 方法执行之后
	 * @param method    当前执行方法
	 * @param arguments 当前方法执行参数
	 * @param ret       当前执行方法返回对象
	 */
	@Advice.OnMethodExit(onThrowable = Throwable.class)
	public static void onMethodExit(@Advice.Origin Object target ,@Advice.This Object proxy,@Advice.Origin Method method,
					  @Advice.AllArguments Object[] arguments,
					  @Advice.Return(readOnly = false, typing = DYNAMIC) Object ret,
					  @Advice.Thrown(readOnly=false) Throwable excp){
		LOGGER.info("执行[{}.{}]方法完毕",target.getClass().getName(),method.getName());
		AbstractPointcutAdvisor pointcutAdvisor = ElevenApplicationContextAware.getBean(AbstractPointcutAdvisor.class);

		Class<?> tClass = target.getClass();
		Annotation annotation = pointcutAdvisor.getMethodMatcher().matches(tClass);

		if (!Optional.ofNullable(annotation).isPresent()) {
			LOGGER.warn("当前类[{}]没有添加AOP切点", tClass.getName());
			return;
		}

		ProxyTypeAnnotationEntity proxyTypeAnnotationEntity = pointcutAdvisor.getProxyTypeAnnotationEntity(annotation);
		AdviceInterceptor interceptor = proxyTypeAnnotationEntity.getAdviceInterceptor();
		if (excp != null){
			LOGGER.info("Method end with exception:\n" +
					"--------------------------------------------\n" +
					"Exception type:" + excp.getClass().getName() + "\n" +
					"Exception message:" + excp.getMessage()+"--------------------------------------------\n");
			interceptor.onMethodException(target,proxy,method,arguments,ret,excp);
		}else {
			LOGGER.info("Method proccess end");
			interceptor.onMethodExit(target,proxy,method,arguments,ret);
		}
		excp = null;
	}
}
