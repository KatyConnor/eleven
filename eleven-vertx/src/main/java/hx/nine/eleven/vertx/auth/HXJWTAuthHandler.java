package hx.nine.eleven.vertx.auth;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;

/**
 *
 * 权限验证拦截器
 * @author wml
 * @Discription
 * @Date 2023-08-09
 */
public interface HXJWTAuthHandler extends JWTAuthHandler {

    /**
     * 创建一个 JWT auth handler
     * @param vertx         Vertx 对象
     * @param authProvider  用户验证提供方
     * @param config        权限配置
     * @return
     */
    static JWTAuthHandler create(Vertx vertx, JWTAuth authProvider, JWTAuthOptions config) {
        return create(vertx,authProvider, null,config);
    }

    /**
     * 创建一个 JWT auth handler
     * @param vertx        Vertx 对象
     * @param authProvider 用户验证提供方
     * @param realm        权限对象
     * @param config       权限配置
     * @return
     */
    static JWTAuthHandler create(Vertx vertx,JWTAuth authProvider, String realm, JWTAuthOptions config) {
        return new HXJWTAuthHandlerImpl(vertx,authProvider, realm,config);
    }
}
