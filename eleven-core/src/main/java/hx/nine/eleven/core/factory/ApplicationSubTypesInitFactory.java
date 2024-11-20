package hx.nine.eleven.core.factory;

import org.reflections.Reflections;

/**
 * 可自定义基础类供下游继承/实现，通过ApplicationSubTypesInitFactory.loadSubTypesObject方法扫描所有子类并实现对其加载注入等
 * @auth wml
 * @date 2024/9/25
 */
public interface ApplicationSubTypesInitFactory {

	void loadSubTypesObject(Reflections reflections) throws Throwable;
}
