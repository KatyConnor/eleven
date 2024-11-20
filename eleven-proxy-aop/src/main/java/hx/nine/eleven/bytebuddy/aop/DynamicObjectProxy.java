package hx.nine.eleven.bytebuddy.aop;

import com.esotericsoftware.reflectasm.MethodAccess;
import hx.nine.eleven.bytebuddy.aop.advice.AbstractPointcutAdvisor;
import hx.nine.eleven.bytebuddy.aop.advice.AopAdvice;
import hx.nine.eleven.bytebuddy.aop.creator.ObjectProxyCreator;
import hx.nine.eleven.bytebuddy.aop.enums.AopProxyTypeEnums;
import hx.nine.eleven.bytebuddy.aop.exception.ProxyCreatorException;
import hx.nine.eleven.bytebuddy.aop.util.ProxyUtil;
import hx.nine.eleven.core.aop.ElevenObjectProxy;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class DynamicObjectProxy extends ElevenObjectProxy {

	private final static Logger LOGGER = LoggerFactory.getLogger(DynamicObjectProxy.class);

	/**
	 * 通过添加切点代理执行某个方法
	 *
	 * @param object     目标对象
	 * @param methodName 执行方法名称
	 * @param paramTypes 方法参数类型
	 * @param args       方法参数
	 * @return
	 * @TODO issue: 只能有一个AopAdvice实现类，不满足多切点场景使用
	 */
	public static Object invoke(Object object, String methodName, Class[] paramTypes, Object... args) {
		MethodAccess methodAccess = MethodAccess.get(object.getClass());
		AopAdvice advice = DefaultElevenApplicationContext.build().getBean(ConstantType.DYNAMIC_PROXY_ADVICE, AopAdvice.class);
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
	 * @TODO issue: 只能有一个AopAdvice实现类，不满足多切点场景使用
	 */
	public static Object invoke(Object object, String methodName, Object... args) {
		MethodAccess methodAccess = MethodAccess.get(object.getClass());
		AopAdvice advice = DefaultElevenApplicationContext.build().getBean(ConstantType.DYNAMIC_PROXY_ADVICE, AopAdvice.class);
		// 切点，执行前处理
		advice.adviceBefore(object, methodName, null, args);
		Object result = methodAccess.invoke(object, methodName, args);
		// 切点执行后处理
		advice.adviceAfter(result);
		return result;
	}

	/**
	 * 多数据源代理匹配
	 *
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> boolean checkProxy(Class<T> tClass) {
		AbstractPointcutAdvisor advisor = ElevenApplicationContextAware.getBean(AbstractPointcutAdvisor.class);
		Annotation annotation = advisor.getMethodMatcher().matches(tClass);
		return annotation != null;
	}

	/**
	 * 创建一个ByteBuddy代理类
	 * 当前版本只支持 ByteBuddy 使用 MethodInterceptor 实现拦截，暂不支持advice模式后续考虑添加advice模式
	 * @TODO issue：考虑支持 advice 模式拦截
	 * @param target
	 * @param <T>
	 * @return
	 */
	@Override
	public <T> T creatProxy(Class<T> target) throws Exception {
		// 获取创建类的
		try {
			return createProxyInstance(target, false);
		} catch (ProxyCreatorException e) {
			throw new ProxyCreatorException("创建代理失败，", e);
		}
	}

	public static <T> T createProxyInstance(Class<T> target, boolean advice) throws ProxyCreatorException {
		AbstractPointcutAdvisor advisor = DefaultElevenApplicationContext.build()
				.getSubTypesOfBean(AbstractPointcutAdvisor.class);

		Annotation annotation = advisor.getMethodMatcher().matches(target);
		if (!Optional.ofNullable(annotation).isPresent()) {
			LOGGER.warn("当前类[{}]没有添加AOP切点", target.getName());
			return null;
		}

		ProxyTypeAnnotationEntity proxyTypeAnnotationEntity = advisor.getProxyTypeAnnotationEntity(annotation);
		AopProxyTypeEnums proxyTypeEnums = proxyTypeAnnotationEntity.getProxyType();
		ObjectProxyCreator proxyCreator = ProxyUtil.getInstance(proxyTypeEnums.getProxyClass());
		MethodAccess methodAccess = MethodAccess.get(proxyTypeEnums.getProxyClass());
		if (!advice) {
			if (Optional.ofNullable(proxyTypeEnums.getInterceptor()).isPresent()) {
				return (T) methodAccess.invoke(proxyCreator, proxyTypeEnums.getMethod(),
						target, proxyTypeAnnotationEntity.getMethodInterceptor());
			}
			// 需要实现MethodInterceptor
			throw new ProxyCreatorException("jdk proxy not implements MethodInterceptor");
		}
		return (T) methodAccess.invoke(proxyCreator, proxyTypeEnums.getMethod(), target);
	}
}
