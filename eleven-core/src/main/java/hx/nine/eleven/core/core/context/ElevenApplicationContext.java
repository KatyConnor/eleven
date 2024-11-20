package hx.nine.eleven.core.core.context;

import hx.nine.eleven.core.env.ApplicationEnvConfigProperty;
import hx.nine.eleven.core.env.Environment;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 应用上下文
 * @author wml
 * @Discription
 * @Date 2023-03-18
 */
public interface ElevenApplicationContext {

	/**
	 * 注入Bean
	 * @param value
	 */
	<T extends ElevenApplicationContext> T addBean(Object value);

	/**
	 * 注入Bean
	 * @param beanName
	 * @param value
	 */
	<T extends ElevenApplicationContext> T addBean(String beanName, Object value);

	/**
	 * 注入Bean
	 * @param beanName
	 * @param value
	 * @param override
	 * @param <T>
	 * @return
	 */
	<T extends ElevenApplicationContext> T addBean(String beanName, Object value, boolean override);

	/**
	 * bean对象查找核实注解
	 * @param name
	 * @param aClass
	 * @param <A>
	 * @return
	 */
	<A extends Annotation> A findAnnotationOnBean(String name, Class<A> aClass);

	/**
	 * 获取bean对象
	 * @param name
	 * @return
	 */
	Object getBean(String name);

	/**
	 *  获取bean对象
	 * @param name
	 * @param aClass
	 * @param <T>
	 * @return
	 */
	<T> T getBean(String name,Class<T> aClass);

	/**
	 * 获取bean对象
	 * @param aClass
	 * @param <T>
	 * @return
	 */
	<T> T getBean(Class<T> aClass);

	/**
	 * 获取当前类的所有子类class
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	<T> Set<Class<? extends T>> getSubTypesOfBeanClass(Class<T> tClass);

	/**
	 * 获取当前类的所有初始化的子类对象
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	<T> Set<T> getSubTypesOfBeans(Class<T> tClass);

	/**
	 * 获取当前类的所有初始化的子类对象
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	<T> T getSubTypesOfBean(Class<T> tClass);

	/**
	 * 添加类的所有子类class
	 * @param tClass
	 * @param subTypesOfBeanClass
	 * @param <T>
	 * @return
	 */
	<T extends ElevenApplicationContext,R> T addSubTypesOfBeanClass(Class<R> tClass, Set<Class<? extends R>> subTypesOfBeanClass);

	/**
	 * 添加类的所有子类初始化的对象
	 * @param tClass
	 * @param subTypesOfBean
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	<T extends ElevenApplicationContext,R> T addSubTypesOfBean(Class<R> tClass, Set<R> subTypesOfBean);

	/**
	 * 添加类的所有子类初始化的对象
	 * @param subTypesOfBean
	 * @param <T>
	 * @param <R>
	 * @return
	 */
	<T extends ElevenApplicationContext,R> T addSubTypesOfBean(R subTypesOfBean);

	/**
	 * 判断bean是否存在
	 * @param name
	 * @return
	 */
	boolean containsBean(String name);

	/**
	 * 获取所有Properties
	 * @return
	 */
	ApplicationEnvConfigProperty getProperties();

	/**
	 * 获取properties对象
	 * @param properties
	 * @param <T>
	 * @return
	 */
	<T> T getProperties(Class<T> properties);

	/**
	 * 获取环境变量
	 * @return
	 */
	Environment getEnvironment();
}
