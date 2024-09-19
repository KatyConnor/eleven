package com.hx.nine.eleven.core.auth;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.handler.JWTAuthHandler;

/**
 * @author wml
 * @Discription
 * @Date 2023-08-09
 */
public interface HXJWTAuthHandler extends JWTAuthHandler {

    /**
     * Create a JWT auth handler
     *
     * @param authProvider  the auth provider to use
     * @return the auth handler
     */
    static JWTAuthHandler create(Vertx vertx, JWTAuth authProvider, JWTAuthOptions config) {
        return create(vertx,authProvider, null,config);
    }

    /**
     * Create a JWT auth handler
     *
     * @param authProvider  the auth provider to use
     * @return the auth handler
     */
    static JWTAuthHandler create(Vertx vertx,JWTAuth authProvider, String realm, JWTAuthOptions config) {
        return new HXJWTAuthHandlerImpl(vertx,authProvider, realm,config);
    }
}
