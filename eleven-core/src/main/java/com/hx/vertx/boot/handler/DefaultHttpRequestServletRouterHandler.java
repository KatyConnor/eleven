package com.hx.vertx.boot.handler;

import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.handler.servlet.DefaultWebServletRouter;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;
import java.util.Set;

public class DefaultHttpRequestServletRouterHandler implements HttpRequestServletRouterHandler {

	private Set<HttpRequestServletRouterHandler> routerHandlers;

	@Override
	public void preRouter(RoutingContext context) {
		routerHandlers = VertxApplicationContextAware.getSubTypesOfBeans(HttpRequestServletRouterHandler.class);
		routerHandlers.forEach(handler -> {
			if (!(handler instanceof DefaultHttpRequestServletRouterHandler)){
				handler.preRouter(context);
			}
		});
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
