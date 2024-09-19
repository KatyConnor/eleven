package com.hx.nine.eleven.core.handler;

import io.vertx.ext.web.RoutingContext;

/**
 * handler 路由
 * @author wml
 * @date 2023-03-27
 */
public interface WebServletRouter {

	Object router(RoutingContext context);

}
