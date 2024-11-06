package com.hx.nine.eleven.vertx.web;

import com.hx.nine.eleven.core.annotations.Order;
import com.hx.nine.eleven.core.core.bean.ElevenBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.exception.ApplicationInitialzerException;
import com.hx.nine.eleven.core.factory.WebRouteHandler;
import com.hx.nine.eleven.vertx.handler.DefaultHttpRequestServletRouterHandler;
import com.hx.nine.eleven.vertx.handler.HttpRequestServletRouterHandler;
import com.hx.nine.eleven.vertx.handler.WebRequestServiceHandler;
import io.vertx.core.json.JsonObject;
import org.reflections.Reflections;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @auth wml
 * @date 2024/9/29
 */
public class VertxWebRouteHandlerInitializer implements WebRouteHandler {

	@Override
	public void loadWebRouteHandler(Reflections reflections) throws Throwable{
		Set<Class<? extends WebRequestServiceHandler>> serviceHandler = reflections.getSubTypesOf(WebRequestServiceHandler.class);
		Optional.ofNullable(serviceHandler).ifPresent(an -> {
			if (an.size() > 1) {
				throw new ApplicationInitialzerException("WebRequestServletHandler 实例不唯一,{}", JsonObject.mapFrom(an).toString());
			}

			if (an.size() == 1) {
				DefaultElevenApplicationContext.build().
						addBean(WebRequestServiceHandler.class.getName(), ElevenBeanFactory.createBean(an.stream().findFirst().get()));
			}
		});
		initHttpRequestRouterHandler(reflections);
	}

	private void initHttpRequestRouterHandler(Reflections reflections) {
		DefaultElevenApplicationContext.build().
				addBean(DefaultHttpRequestServletRouterHandler.class.getName(), new DefaultHttpRequestServletRouterHandler());
		Set<Class<? extends HttpRequestServletRouterHandler>> routerHandler = reflections.getSubTypesOf(HttpRequestServletRouterHandler.class);
		Optional.ofNullable(routerHandler).ifPresent(tclass -> {
			if (tclass.size() > 0) {
				int[] count = {-1};
				Set<HttpRequestServletRouterHandler> subTypesOfBean = new LinkedHashSet<>();
				tclass.stream().sorted((o1, o2) -> {
					Order order1 = o1.getAnnotation(Order.class);
					Order order2 = o2.getAnnotation(Order.class);
					return Integer.valueOf(order1 == null ? ++count[0] : order1.order())
							.compareTo(order2 == null ? ++count[0] : order2.order());
				}).forEach(c -> {
					if (!c.equals(DefaultHttpRequestServletRouterHandler.class)) {
						subTypesOfBean.add(ElevenBeanFactory.createBean(c));
					}
				});
				DefaultElevenApplicationContext.build().
						addSubTypesOfBean(HttpRequestServletRouterHandler.class, subTypesOfBean);
			}
		});
	}
}
