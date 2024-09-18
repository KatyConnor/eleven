package com.hx.vertx.boot.handler;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * 全局异常处理接口
 */
public interface GlobalExceptionHandler {

	/**
	 * 全局异常处理
	 * @param ctx
	 */
	void handle(RoutingContext ctx,Object res);

	default void sendFailure(RoutingContext ctx,Object json){
		ctx.json(json);
	}

	default void sendFailure(RoutingContext ctx,Object json,Handler<AsyncResult<Void>> handler){
		ctx.json(json,handler);
	}

}
