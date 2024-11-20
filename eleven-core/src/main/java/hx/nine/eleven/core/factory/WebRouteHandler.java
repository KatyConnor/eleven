package hx.nine.eleven.core.factory;

import org.reflections.Reflections;

/**
 * @auth wml
 * @date 2024/9/29
 */
public interface WebRouteHandler {

	void loadWebRouteHandler(Reflections reflections) throws Throwable;
}
