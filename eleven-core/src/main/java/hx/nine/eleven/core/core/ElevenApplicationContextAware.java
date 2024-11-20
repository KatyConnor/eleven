package hx.nine.eleven.core.core;

import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.env.ApplicationEnvConfigProperty;
import hx.nine.eleven.core.env.Environment;
import hx.nine.eleven.core.properties.ElevenBootApplicationProperties;

import java.util.Set;

public class ElevenApplicationContextAware {

	public static <T> T getBean(Class<T> target){
		return DefaultElevenApplicationContext.build().getBean(target);
	}

	public static <T> T getBean(String name,Class<T> target){
		return DefaultElevenApplicationContext.build().getBean(name,target);
	}

	public static <T> T getBean(String name){
		return (T) DefaultElevenApplicationContext.build().getBean(name);
	}

	public static <T> Set<Class<? extends T>> getSubTypesOfBeanClass(Class<T> tClass) {
		return DefaultElevenApplicationContext.build().getSubTypesOfBeanClass(tClass);
	}

	public static <T> Set<T> getSubTypesOfBeans(Class<T> tClass) {
		return DefaultElevenApplicationContext.build().getSubTypesOfBeans(tClass);
	}

	public static <T> T getSubTypesOfBean(Class<T> tClass) {
		return DefaultElevenApplicationContext.build().getSubTypesOfBean(tClass);
	}

	public static boolean containsBean(String bean) {
		return DefaultElevenApplicationContext.build().containsBean(bean);
	}

	public static ApplicationEnvConfigProperty getProperties() {
		return DefaultElevenApplicationContext.build().getProperties();
	}

	public static <T> T getProperties(Class<T> properties) {
		return DefaultElevenApplicationContext.build().getProperties(properties);
	}

	public static ElevenBootApplicationProperties getElevenApplicationProperties(){
		return DefaultElevenApplicationContext.build().getBean(ElevenBootApplicationProperties.class);
	}

	public static Environment getEnvironment() {
		return DefaultElevenApplicationContext.build().getEnvironment();
	}
}
