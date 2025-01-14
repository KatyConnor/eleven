package hx.nine.eleven.core.core.bean;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import hx.nine.eleven.commons.utils.CollectionUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.annotations.BeanRegister;
import hx.nine.eleven.core.annotations.ConditionalOnBean;
import hx.nine.eleven.core.annotations.ConditionalOnClass;
import hx.nine.eleven.core.aop.ElevenObjectProxy;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.exception.ApplicationInitialzerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 初始化bean
 */
public class ElevenBeanFactory {

	private final static Logger LOGGER = LoggerFactory.getLogger(ElevenBeanFactory.class);

	/**
	 * 创建 bean
	 *
	 * @param tClass
	 * @param <T>
	 * @return       创建成功返回创建的对象，创建失败或者异常返回null
	 */
	public static <T> T initBean(Class<T> tClass) throws Exception{
		ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
		// 获取bean创建之前处理类
		Set<Class<? extends ApplicationBeanRegister>> beanRegisters = DefaultElevenApplicationContext.build()
				.getSubTypesOfBeanClass(ApplicationBeanRegister.class);
		T bean = null;
		ApplicationBeanRegister beanRegister = null;
		Class<? extends ApplicationBeanRegister> beanRegisterClass = null;
		if (Optional.ofNullable(beanRegisters).isPresent() && beanRegisters.size() > 0) {
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
		if (!checkOnClass(tClass)) {
			throw new ApplicationInitialzerException(StringUtils.format("Class=[{}] 注解[ConditionalOnClass]中依赖class在当前运行环境有遗漏，请检查!",tClass.getSimpleName()));
		}

		// 初始化之前检查时候有其他类依赖
		if (!checkAfterBean(tClass)) {
			LOGGER.info("Class=[{}] 有依赖类在容器中没有找到实例对象，[{}]放入缓存待依赖对象实例完成后再实例化"
					, tClass.getSimpleName(), tClass.getSimpleName());
			return null;
		}
		ElevenObjectProxy elevenObjectProxy = ElevenApplicationContextAware.getSubTypesOfBean(ElevenObjectProxy.class);
		if (beanRegister != null) {
			beanRegister.before(DefaultElevenApplicationContext.build());
			bean = elevenObjectProxy.checkProxy(tClass) ? elevenObjectProxy.creatProxy(tClass) : constructorAccess.newInstance();
			beanRegister.setBean(bean);
			beanRegister.after(DefaultElevenApplicationContext.build());
			return (T) beanRegister.getBean();
		} else {
			bean = elevenObjectProxy.checkProxy(tClass) ? elevenObjectProxy.creatProxy(tClass) : constructorAccess.newInstance();
		}
		Optional.ofNullable(beanRegisterClass).ifPresent(br -> {
			beanRegisters.remove(br); // 删除处理之后的class
		});
		return bean;
	}

	public static Object invoke(String methodName, Object obj, Object... args) {
		MethodAccess method = MethodAccess.get(obj.getClass());
		return method.invoke(obj, methodName, args);
	}

	public static <T> T createBean(Class<T> tClass) {
		ConstructorAccess<T> constructorAccess = ConstructorAccess.get(tClass);
		return constructorAccess.newInstance();
	}

	private static boolean checkOnClass(Class<?> tClass) {
		ConditionalOnClass conditionalOnClass = tClass.getAnnotation(ConditionalOnClass.class);
		if (Optional.ofNullable(conditionalOnClass).isPresent()) {
			Class<?>[] onClass = conditionalOnClass.value();
			List<Class<?>> list = Arrays.asList(onClass);
			for (Class<?> c : list) {
				if (!isPresent(c.getName())) {
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
					if (DefaultElevenApplicationContext.build().getBean(beanName) == null) {
						next = false;
						LOGGER.info("bean=[{}] 还没有初始化到当前容器", beanName);
						break;
					}
				}
			}

			if (beans != null && beans.length > 0 && next) {
				return true;
			}

			Class<?>[] classes = conditionalAfterBean.value();
			if (classes != null && classes.length > 0) {
				for (Class<?> beanClass : classes) {
					if (ElevenApplicationContextAware.getBean(beanClass) == null) {
						next = false;
						LOGGER.info("bean=[{}] 还没有初始化到当前容器", beanClass.getSimpleName());
						break;
					}
				}
			}

			if (next) {
				return true;
			}


			ConcurrentLinkedQueue<Class<?>> afterBeansQueue = ElevenApplicationContextAware.
					getBean(ConstantType.CONDITIONAL_ON_AFTER_BEAN);
			if (CollectionUtils.isEmpty(afterBeansQueue)) {
				afterBeansQueue = new ConcurrentLinkedQueue();
			}
			afterBeansQueue.add(tClass);
			DefaultElevenApplicationContext.build().addBean(ConstantType.CONDITIONAL_ON_AFTER_BEAN, afterBeansQueue, true);
			return false;

		}
		return true;
	}

	/**
	 * 判断类class 是否存在
	 *
	 * @param name
	 * @return
	 */
	public static boolean isPresent(String name) {
		try {
			Thread.currentThread().getContextClassLoader().loadClass(name);
			return true;
		} catch (ClassNotFoundException e) {
			LOGGER.error("Class=[{}] 不存在，请检查", name);
			return false;
		}
	}
}
