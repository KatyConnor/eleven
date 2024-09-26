package com.hx.nine.eleven.domain;

import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;

public class BeanFactoryLocator {

	public static <T> T getBean(String name){
		return (T)DefaultElevenApplicationContext.build().getBean(name);
	}

	public static <T> T getBean(Class<T> name){
		return DefaultElevenApplicationContext.build().getBean(name);
	}
 }
