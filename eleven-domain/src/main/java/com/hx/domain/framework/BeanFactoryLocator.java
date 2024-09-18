package com.hx.domain.framework;

import com.hx.vertx.boot.core.context.DefaultVertxApplicationContext;

public class BeanFactoryLocator {

	public static <T> T getBean(String name){
		return (T)DefaultVertxApplicationContext.build().getBean(name);
	}

	public static <T> T getBean(Class<T> name){
		return DefaultVertxApplicationContext.build().getBean(name);
	}
 }
