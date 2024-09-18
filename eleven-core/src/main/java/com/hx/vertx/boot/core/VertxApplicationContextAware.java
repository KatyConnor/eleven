package com.hx.vertx.boot.core;

import com.hx.vertx.boot.core.context.DefaultVertxApplicationContext;
import com.hx.vertx.boot.env.ApplicationEnvConfigProperty;
import com.hx.vertx.boot.env.Environment;
import com.hx.vertx.boot.properties.VertxApplicationProperties;

import java.util.Set;

public class VertxApplicationContextAware {

	public static <T> T getBean(Class<T> target){
		return DefaultVertxApplicationContext.build().getBean(target);
	}

	public static <T> T getBean(String name,Class<T> target){
		return DefaultVertxApplicationContext.build().getBean(name,target);
	}

	public static <T> T getBean(String name){
		return (T) DefaultVertxApplicationContext.build().getBean(name);
	}

	public static <T> Set<Class<? extends T>> getSubTypesOfBeanClass(Class<T> tClass) {
		return DefaultVertxApplicationContext.build().getSubTypesOfBeanClass(tClass);
	}

	public static <T> Set<T> getSubTypesOfBeans(Class<T> tClass) {
		return DefaultVertxApplicationContext.build().getSubTypesOfBeans(tClass);
	}

	public static <T> T getSubTypesOfBean(Class<T> tClass) {
		return DefaultVertxApplicationContext.build().getSubTypesOfBean(tClass);
	}

	public static boolean containsBean(String bean) {
		return DefaultVertxApplicationContext.build().containsBean(bean);
	}

	public static ApplicationEnvConfigProperty getProperties() {
		return DefaultVertxApplicationContext.build().getProperties();
	}

	public static <T> T getProperties(Class<T> properties) {
		return DefaultVertxApplicationContext.build().getProperties(properties);
	}

	public static VertxApplicationProperties getVertxApplicationProperties(){
		return DefaultVertxApplicationContext.build().getBean(VertxApplicationProperties.class);
	}

	public static Environment getEnvironment() {
		return DefaultVertxApplicationContext.build().getEnvironment();
	}
}
