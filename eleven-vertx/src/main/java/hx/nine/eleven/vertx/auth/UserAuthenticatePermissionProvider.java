package hx.nine.eleven.vertx.auth;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.annotations.SubComponent;
import hx.nine.eleven.core.auth.UserAuthenticateProvider;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.auth.jwt.JWTAuth;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 继承该接口，实现用户密码验证
 * @TODO 系统交易码没有laugh统一规则定义，需要后期更正
 * @author wml
 * @Discription
 * @Date 2023-08-06
 */
@SubComponent(interfaces = UserAuthenticateProvider.class)
public class UserAuthenticatePermissionProvider implements UserAuthenticateProvider {

    private JWT jwt;
    private JWTAuth authProvider;
    private JWTOptions jwtOptions;

    @Override
    public boolean authenticate(String authToken) {
        // 获取token
        if (StringUtils.isNotBlank(authToken)) {
            AtomicBoolean authNext = new AtomicBoolean(false);
            this.authProvider.authenticate(new TokenCredentials(authToken), auth -> {
                if (auth.succeeded()) {
                    ElevenLoggerFactory.build(this).info("token 认证成功！");
                    authNext.set(true);
                } else {
                    ElevenLoggerFactory.build(this).info("token认证失败,token无效");
                }
            });
            return authNext.get();
        }
        return false;
    }

    @Override
    public <T> String generateToken(T obj) {
        return  this.generateToken(obj,30);
    }

    @Override
    public <T> String generateToken(T obj, int expires) {
        return  this.authProvider.generateToken(obj instanceof JsonObject?
                (JsonObject)obj:JsonObject.mapFrom(obj), jwtOptions.setExpiresInMinutes(expires));
    }

    @Override
    public String generateToken(String token) {
        return  this.generateToken(token,30);
    }

    @Override
    public String generateToken(String token, int expires) {
        Object obj = this.decodeToken(token);
        return  this.authProvider.generateToken(obj instanceof JsonObject?
                (JsonObject)obj:JsonObject.mapFrom(obj), jwtOptions.setExpiresInMinutes(expires));
    }

    @Override
    public Object decodeToken(String authToken) {
        JsonObject payload = null;
        try {
            payload = this.jwt.decode(authToken);
        } catch (RuntimeException e) {
            // 认证失败，返回信息
            ElevenLoggerFactory.build(this).error("token解析失败",e);
            throw new RuntimeException("token解析失败",e);
        }
        return payload;
    }

    @Override
    public Object decodeTokenAuth(String authToken) {
        JsonObject payload = null;
        try {
            payload = this.jwt.decode(authToken);
        } catch (RuntimeException e) {
            // 认证失败，返回信息
            ElevenLoggerFactory.build(this).error("token解析失败",e);
            throw new RuntimeException("token解析失败",e);
        }
        return payload;
    }

    public UserAuthenticatePermissionProvider setAuthProvider(JWTAuth authProvider) {
        this.authProvider = authProvider;
        return this;
    }

    public UserAuthenticatePermissionProvider setJwtOptions(JWTOptions jwtOptions) {
        this.jwtOptions = jwtOptions;
        return this;
    }

    public UserAuthenticatePermissionProvider setJwt(JWT jwt) {
        this.jwt = jwt;
        return this;
    }
}
