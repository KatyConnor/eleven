package com.hx.nine.eleven.core.factory;

import org.reflections.Reflections;

/**
 * 注解解析加载
 */
public interface ApplicationAnnotationFactory {

  void loadAnnotations(Reflections reflections) throws Throwable;
}
