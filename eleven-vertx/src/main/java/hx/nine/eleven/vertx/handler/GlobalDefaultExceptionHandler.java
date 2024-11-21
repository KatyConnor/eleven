package hx.nine.eleven.vertx.handler;

import hx.nine.eleven.vertx.constant.VertxConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.utils.MDCThreadUtil;
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
		GlobalExceptionHandler exceptionHandler = ElevenApplicationContextAware.getSubTypesOfBean(GlobalExceptionHandler.class);
		exceptionHandler.handle(ctx,ctx.get(VertxConstantType.RESPONSE_BODY));
		MDCThreadUtil.clear(); //清空
	}

	public static GlobalDefaultExceptionHandler of(){
		return new GlobalDefaultExceptionHandler();
	}
}
