package com.hx.vertx.boot.handler;

import io.vertx.ext.web.RoutingContext;

public interface HttpRequestServletRouterHandler {

	/**
	 * 执行前
	 * @param context
	 */
	void preRouter(RoutingContext context);

	/**
	 * 执行中
	 * @param context
	 * @return
	 */
	Object doRouter(RoutingContext context);

	/**
	 * 执行后
	 * @param context
	 * @param res
	 */
	void afterRouter(RoutingContext context,Object res);
}
