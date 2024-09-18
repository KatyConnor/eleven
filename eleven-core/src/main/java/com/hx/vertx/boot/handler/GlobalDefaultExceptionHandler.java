package com.hx.vertx.boot.handler;

import com.hx.vertx.boot.constant.ConstantType;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.utils.MDCThreadUtil;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

/**
 * 全局异常处理
 * @author wml
 * @date 2023-04-28
 */
public class GlobalDefaultExceptionHandler implements Handler<RoutingContext> {

	@Override
	public void handle(RoutingContext ctx) {
		GlobalExceptionHandler exceptionHandler = VertxApplicationContextAware.getSubTypesOfBean(GlobalExceptionHandler.class);
		exceptionHandler.handle(ctx,ctx.get(ConstantType.RESPONSE_BODY));
		MDCThreadUtil.clear(); //清空
	}

	public static GlobalDefaultExceptionHandler of(){
		return new GlobalDefaultExceptionHandler();
	}
}
