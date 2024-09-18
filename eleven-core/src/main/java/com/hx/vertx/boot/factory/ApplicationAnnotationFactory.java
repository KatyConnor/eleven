package com.hx.vertx.boot.factory;

import org.reflections.Reflections;

/**
 * 注解解析加载
 */
public interface ApplicationAnnotationFactory {

  void loadAnnotations(Reflections reflections) throws Throwable;
}
