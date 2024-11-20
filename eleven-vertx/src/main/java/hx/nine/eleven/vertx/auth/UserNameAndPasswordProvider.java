package hx.nine.eleven.vertx.auth;

import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.web.http.HttpResponse;
import hx.nine.eleven.core.web.http.HttpServlet;
import hx.nine.eleven.core.web.http.HttpServletRequest;
import hx.nine.eleven.core.web.http.HttpServletResponse;
import hx.nine.eleven.vertx.constant.DefualtProperType;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;


/**
 * 继承该接口，实现用户密码验证
 * @author wml
 * @Discription
 * @Date 2023-08-06
 */
public class UserNameAndPasswordProvider{

    private Future<Object> authenticate(RoutingContext ctx){
        HttpServlet httpServlet = ElevenApplicationContextAware.getBean(HttpServlet.class);
        HttpResponse res = null;
        try {
            HttpServletRequest req = HttpServletRequest.build();
            HttpServletResponse resp = HttpServletResponse.build();
            req.setAttribute(DefualtProperType.VERTX_CONTEXT,ctx);
            httpServlet.service(req, resp);
            res = resp.httpResponse();

            JsonObject responseBody = JsonObject.mapFrom(res.getBody()).getJsonObject(ConstantType.HTTP_RESPONSE_BODY)
                    .getJsonObject(ConstantType.RESPONSE_BODY_ENTITY);
            if (ConstantType.SUCCESS.equals(responseBody.getString(ConstantType.STATUS_CODE))){
                return Future.succeededFuture(res);
            }
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
