package com.hx.nine.eleven.vertx.handler;

import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.vertx.handler.servlet.DefaultWebServletRouter;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;
import java.util.Set;

public class DefaultHttpRequestServletRouterHandler implements HttpRequestServletRouterHandler {

	private Set<HttpRequestServletRouterHandler> routerHandlers;

	public DefaultHttpRequestServletRouterHandler(){
		routerHandlers = ElevenApplicationContextAware.getSubTypesOfBeans(HttpRequestServletRouterHandler.class);
	}

	@Override
	public void preRouter(RoutingContext context) {
		if (Optional.ofNullable(routerHandlers).isPresent()){
			routerHandlers.forEach(handler -> {
				if (!(handler instanceof DefaultHttpRequestServletRouterHandler)){
					handler.preRouter(context);
				}
			});
		}
	}

	@Override
	public Object doRouter(RoutingContext context) {
		 return DefaultWebServletRouter.build().router(context);
	}

	@Override
	public void afterRouter(RoutingContext context, Object res) {
		Optional.ofNullable(routerHandlers).ifPresent(handlers -> {
			handlers.forEach(handler -> {
				if (!(handler instanceof DefaultHttpRequestServletRouterHandler)){
					handler.afterRouter(context, res);
				}
			});
		});
	}

}
