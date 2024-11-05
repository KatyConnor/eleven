package com.hx.nine.eleven.bytebuddy.aop.interceptor.advice;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.Method;
import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;

/**
 * advice 模式拦截
 * @author wml
 * @date 2023-04-12
 */
public class AdvisorMethodInterceptor {

	private static final Logger LOGGER = LoggerFactory.getLogger(AdvisorMethodInterceptor.class);

	/**
	 * 目标方法执行之前
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
		AdviceInterceptor interceptor = null;
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
		AdviceInterceptor interceptor = null;
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
