package hx.nine.eleven.core.core.context;

import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.core.env.ApplicationEnvConfigProperty;
import hx.nine.eleven.core.exception.ApplicationInitialzerException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean存储容器
 * @author wml
 * @date 2023-03-24
 */
public class ApplicationContextContainer {

	/** 存储对象 */
	private final static ConcurrentHashMap<String, Object> beanContainer = new ConcurrentHashMap<>();

	/**
	 * 添加一个对象到bean容器
	 * @param value
	 */
	public ApplicationContextContainer addBean(Object value) {
		// 判断是否是代理类
	    String name = checkProxyName(value.getClass());
		if (beanContainer.containsKey(value.getClass().getName())) {
			throw new ApplicationInitialzerException("容器已经注册[{}]对象,需要保存注册对象唯一",name);
		}
		beanContainer.put(name, value);
		return this;
	}

	/**
	 * 指定bean名称，添加一个bean对象到容器
	 * @param beanName
	 * @param value
	 */
	public ApplicationContextContainer addBean(String beanName, Object value) {
		if (beanContainer.containsKey(beanName)) {
			throw new ApplicationInitialzerException("容器已经注册[{}]对象,需要保存注册对象唯一", beanName);
		}
		beanContainer.put(beanName, value);
		return this;
	}

	/**
	 * 指定bean名称，添加一个bean对象到容器
	 * @param beanName
	 * @param value
	 */
	public ApplicationContextContainer addBean(String beanName, Object value,boolean override) {
		if (override){
			beanContainer.put(beanName, value);
			return this;
		}
		this.addBean(beanName, value);
		return this;
	}

	/**
	 * 根据名称获取bean
	 * @param name
	 * @return
	 */
	public Object getBean(String name) {
		return beanContainer.get(name);
	}

	/**
	 * 指定bean class类型获取bean
	 * @param className
	 * @param <T>
	 * @return
	 */
	public <T> T getBean(Class<T> className) {
		return (T) beanContainer.get(className.getName());
	}

	/**
	 * 查找父类的继承类
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public <T> Set<Class<? extends T>> getSubTypesOfBeanClass(Class<T> tClass) {
		return (Set<Class<? extends T>>) beanContainer.get(tClass.getName());
	}

	/**
	 * 查找父类的继承类实例化对象
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public <T> Set<T> getSubTypesOfBeans(Class<T> tClass) {
		Object value = beanContainer.get(tClass.getName());
		if (ObjectUtils.isNotEmpty(value) && value instanceof Set){
			return (Set<T>)value;
		}

		if (ObjectUtils.isNotEmpty(value)){
			Set<T> set = new HashSet<>();
			set.add((T) value);
			return set;
		}
		return null;
	}

	/**
	 * @TODO issue：当一个接口或则类有多个实现或继承时，且都各自使用@conponent 或者@subconponet不指定interfaces时，并成功注册到了容器中，这此处可能是返回第一个被匹配的子类对象
	 * 查找父类的继承类实例化对象
	 * @param tClass
	 * @param <T>
	 * @return
	 */
	public <T> T getSubTypesOfBean(Class<T> tClass) {
		Object obj =  beanContainer.get(tClass.getName());
		if (Optional.ofNullable(obj).isPresent()){
			return  (T)obj;
		}
		for (Map.Entry entry : beanContainer.entrySet()){
			Class valueClass = entry.getValue().getClass();
			if (valueClass.getSuperclass().getName().equals(tClass.getName()) || valueClass.isInstance(tClass) || checkImplInterfaces(entry.getValue(),tClass)){
				obj = entry.getValue();
			}
		}
		return  (T)obj;
	}

	/**
	 * 添加类的所有子类class
	 * @param tClass
	 * @param subTypesOfBeanClass
	 * @param <T>
	 */
	public <T> ApplicationContextContainer addSubTypesOfBeanClass(Class<T> tClass,Set<Class<? extends T>> subTypesOfBeanClass) {
		beanContainer.put(tClass.getName(),subTypesOfBeanClass);
		return this;
	}

	/**
	 * 添加类的所有子类实例化对象
	 * @param tClass
	 * @param subTypesOfBean
	 * @param <T>
	 * @return
	 */
	public <T> ApplicationContextContainer addSubTypesOfBean(Class<T> tClass,Set<T> subTypesOfBean) {
		beanContainer.put(tClass.getName(),subTypesOfBean);
		return this;
	}

	/**
	 * 添加类的所有子类实例化对象,只能支持抽象类或者普通类，接口不支持
	 * @param subTypesOfBean
	 * @param <T>
	 * @return
	 */
	public <T> ApplicationContextContainer addSubTypesOfBean(T subTypesOfBean) {
		String key = subTypesOfBean.getClass().getSuperclass().getName();
		if (beanContainer.containsKey(key)){
			throw new ApplicationInitialzerException("实例不唯一,{}",key);
		}
		beanContainer.put(key,subTypesOfBean);
		return this;
	}

	/**
	 * 获取应用所有properties
	 * @return
	 */
	public ApplicationEnvConfigProperty getProperties() {
		return this.getBean(ApplicationEnvConfigProperty.class);
	}

	/**
	 * 根据应用的properties class类型获取指定的properties
	 * @param properties
	 * @param <T>
	 * @return
	 */
	public <T> T getProperties(Class<T> properties) {
		return this.getBean(ApplicationEnvConfigProperty.class).getProperty(properties);
	}

	/**
	 * 判断bean是否在容器存在
	 * @param bean
	 * @return
	 */
	public boolean containsBean(String bean){
		return beanContainer.containsKey(bean);
	}

	private ApplicationContextContainer() {}

	public static ApplicationContextContainer build() {
		return Single.NEWSTANCE;
	}

	private String checkProxyName(Class<?> tclass){
		String name = tclass.getName();
		if (name.indexOf("Proxy") != -1){
			return checkProxyName(tclass.getSuperclass());
		}
		return name;
	}
	private boolean checkImplInterfaces(Object obj,Class<?> interfaces){
		Class<?>[] interfacesClasses = obj.getClass().getInterfaces();
		for (Class<?> aClass : Arrays.asList(interfacesClasses)) {
			if (aClass.equals(interfaces)){
				return true;
			}
		}
		return false;
	}

	private static final class Single {
		private static final ApplicationContextContainer NEWSTANCE = new ApplicationContextContainer();
	}


}
