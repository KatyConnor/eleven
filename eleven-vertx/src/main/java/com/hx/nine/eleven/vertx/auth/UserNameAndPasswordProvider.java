package com.hx.nine.eleven.vertx.auth;

import com.hx.nine.eleven.commons.utils.Builder;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.vertx.handler.DefaultHttpRequestServletRouterHandler;
import com.hx.nine.eleven.vertx.handler.HttpRequestServletRouterHandler;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.core.utils.MDCThreadUtil;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;


/**
 * 继承该接口，实现用户密码验证
 * @author wml
 * @Discription
 * @Date 2023-08-06
 */
public class UserNameAndPasswordProvider{

    private Future<Object> authenticate(RoutingContext ctx){
        MDCThreadUtil.wrap();
        HttpRequestServletRouterHandler servletRouterHandler = ElevenApplicationContextAware.
                getBean(DefaultHttpRequestServletRouterHandler.class);
        Object res = null;
        try {
            if (Optional.of(servletRouterHandler).isPresent()){
                servletRouterHandler.preRouter(ctx);
                res = servletRouterHandler.doRouter(ctx);
                servletRouterHandler.afterRouter(ctx, res);
            }else {
                res = servletRouterHandler.doRouter(ctx);
            }
            JsonObject responseBody = JsonObject.mapFrom(res).getJsonObject(ConstantType.HTTP_RESPONSE_BODY)
                    .getJsonObject(ConstantType.RESPONSE_BODY_ENTITY);
            if (ConstantType.SUCCESS.equals(responseBody.getString(ConstantType.STATUS_CODE))){
                return Future.succeededFuture(res);
            }
            MDCThreadUtil.clear();
            return Future.failedFuture(JsonObject.mapFrom(res).toString());
        } catch (Throwable ex) {
            if (res != null){
                ctx.put(ConstantType.RESPONSE_BODY,res);
            }
            AuthenticateResponse response = Builder.of(AuthenticateResponse::new)
                    .with(AuthenticateResponse::setCode,"B0000000001")
                    .with(AuthenticateResponse::setCode,"用户登录授权失败").build();
            ElevenLoggerFactory.build(this).error("用户登录授权失败",ex);
           return Future.failedFuture(JsonObject.mapFrom(response).toString());
        }
    }

    public void authenticate(RoutingContext ctx, Handler<AsyncResult<Object>> handler) {
        this.authenticate(ctx).onComplete(handler);
    }
}
