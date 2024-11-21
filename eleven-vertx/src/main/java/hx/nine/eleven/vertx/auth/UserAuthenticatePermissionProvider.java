package hx.nine.eleven.vertx.auth;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.UserAuthenticateProvider;
import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.User;
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
@Component
public class UserAuthenticatePermissionProvider implements UserAuthenticateProvider {

    private final JWT jwt = new JWT();
    private JWTAuth authProvider;
    private JWTOptions jwtOptions;

    @Override
    public boolean authenticate(String authToken) {
        // 获取token
        if (StringUtils.isNotBlank(authToken)) {
            AtomicBoolean authNext = new AtomicBoolean(false);
            this.authProvider.authenticate(new TokenCredentials(authToken), auth -> {
                if (auth.succeeded()) {
                    User user = auth.result();
                    JsonObject authData = user.principal();
                    String userId = authData.getString("userCode");
                    ElevenLoggerFactory.build(this).info("用户 {} token 认证成功！", userId);
                    authNext.set(true);
                    // 解析token，将解析信息放入请求中
                } else {
                    ElevenLoggerFactory.build(this).info("认证失败,token无效");
                }
            });
            return authNext.get();
        }
        return false;
    }

    @Override
    public <T> String generateToken(T obj) {
        return  this.authProvider.generateToken(obj instanceof JsonObject?
                (JsonObject)obj:JsonObject.mapFrom(obj), jwtOptions.setExpiresInMinutes(30));
    }

    @Override
    public String generateToken(String token) {
        Object obj = this.decodeToken(token);
        return  this.authProvider.generateToken(obj instanceof JsonObject?
                (JsonObject)obj:JsonObject.mapFrom(obj), jwtOptions.setExpiresInMinutes(30));
    }

    @Override
    public Object decodeToken(String authToken) {
        JsonObject payload = null;
        try {
            payload = this.jwt.decode(authToken);
        } catch (RuntimeException e) {
            // 认证失败，返回信息
            throw new RuntimeException("token解析失败",e);
        }
        return payload;
    }

    @Override
    public Object decodeTokenAuth(String authToken) {
        JsonObject payload = null;
        try {
            payload = this.jwt.decode(authToken);
            //TODO 删除token数据
        } catch (RuntimeException e) {
            // 认证失败，返回信息
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
}