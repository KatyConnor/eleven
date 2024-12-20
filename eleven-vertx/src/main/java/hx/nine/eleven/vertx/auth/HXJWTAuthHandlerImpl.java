package hx.nine.eleven.vertx.auth;

import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.core.utils.MDCThreadUtil;
import hx.nine.eleven.vertx.properties.VertxApplicationProperties;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystemException;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.impl.jose.JWK;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.impl.JWTAuthProviderImpl;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.JWTAuthHandlerImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wml
 * @Discription
 * @Date 2023-08-09
 */
public class HXJWTAuthHandlerImpl extends JWTAuthHandlerImpl {

    private Boolean authentication;

    private List<String> ignoreAuthentication;

    protected final JWTAuth authProvider;
    private final JWT jwt = new JWT();
    private final String permissionsClaimKey;
    private final JWTOptions jwtOptions;
    /**
     * 会话过期时长
     */
    private int expires;

    public HXJWTAuthHandlerImpl(Vertx vertx, JWTAuth authProvider, String realm, JWTAuthOptions config) {
        super(authProvider, realm);
        MDCThreadUtil.wrap();
        VertxApplicationProperties properties = ElevenApplicationContextAware.getProperties(VertxApplicationProperties.class);
        this.authProvider = authProvider;
        this.authentication = properties.getAuthentication();
        String[] ignoreAuthentications = properties.getIgnoreAuthentication();
        this.expires = properties.getExpires();
        this.ignoreAuthentication = ObjectUtils.isNotEmpty(ignoreAuthentications) ? Arrays.asList(ignoreAuthentications) : new ArrayList<>();

        this.permissionsClaimKey = config.getPermissionsClaimKey();
        this.jwtOptions = config.getJWTOptions();
        // set the nonce algorithm
        jwt.nonceAlgorithm(jwtOptions.getNonceAlgorithm());

        final KeyStoreOptions keyStore = config.getKeyStore();
        // attempt to load a Key file
        try {
            if (keyStore != null) {
                final KeyStore ks;
                if (keyStore.getProvider() == null) {
                    ks = KeyStore.getInstance(keyStore.getType());
                } else {
                    ks = KeyStore.getInstance(keyStore.getType(), keyStore.getProvider());
                }

                // synchronize on the class to avoid the case where multiple file accesses will overlap
                synchronized (JWTAuthProviderImpl.class) {
                    String path = keyStore.getPath();
                    if (path != null) {
                        final Buffer keystore = vertx.fileSystem().readFileBlocking(keyStore.getPath());

                        try (InputStream in = new ByteArrayInputStream(keystore.getBytes())) {
                            ks.load(in, keyStore.getPassword().toCharArray());
                        }
                    } else {
                        ks.load(null, keyStore.getPassword().toCharArray());
                    }
                }
                // load all available keys in the keystore
                for (JWK key : JWK.load(ks, keyStore.getPassword(), keyStore.getPasswordProtection())) {
                    jwt.addJWK(key);
                }
            }

            // attempt to load pem keys
            final List<PubSecKeyOptions> keys = config.getPubSecKeys();
            if (keys != null) {
                for (PubSecKeyOptions pubSecKey : config.getPubSecKeys()) {
                    jwt.addJWK(new JWK(pubSecKey));
                }
            }

            // attempt to load jwks
            final List<JsonObject> jwks = config.getJwks();
            if (jwks != null) {
                for (JsonObject jwk : jwks) {
                    try {
                        jwt.addJWK(new JWK(jwk));
                    } catch (Exception e) {
                        ElevenLoggerFactory.build(this).warn("Unsupported JWK", e);
                    }
                }
            }
        } catch (KeyStoreException | IOException | FileSystemException | CertificateException | NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void handle(RoutingContext ctx) {
        // 检查是否需要鉴权,请求是否跳过鉴权
        if (!this.authentication || ignoreAuthentication.contains(ctx.request().getHeader(ConstantType.TRADE_CODE))) {
            ctx.next();
            return;
        }

        // pause the request
        if (!ctx.request().isEnded()) {
            ctx.request().pause();
        }

        final HttpServerRequest request = ctx.request();
        String authToken = request.getHeader(ConstantType.AUTH_TOKEN);
        // 如果token为空，则判断是否登录，如果没有登录需要进行登录操作
        if (StringUtils.isEmpty(authToken)) {
            UserNameAndPasswordProvider nameAndPasswordProvider = Builder.of(UserNameAndPasswordProvider::new).build();
            nameAndPasswordProvider.authenticate(ctx, auth -> {
                if (auth.succeeded()) {
                    //认证通过之后，再生成token，以后就使用token进行认证
                    JsonObject data = JsonObject.mapFrom(auth.result());
                    JsonObject user = data.getJsonObject(ConstantType.HTTP_RESPONSE_BODY)
                            .getJsonObject(ConstantType.RESPONSE_BODY_ENTITY)
                            .getJsonObject(ConstantType.DATA)
                            .getJsonObject(ConstantType.USER);
                    String token = this.authProvider.generateToken(user, new JWTOptions().setExpiresInMinutes(this.expires));
                    ElevenLoggerFactory.build(this).info("登录认证成功");
                    data.getJsonObject(ConstantType.HTTP_RESPONSE_BODY).getJsonObject(ConstantType.RESPONSE_HEADER_ENTITY).put(ConstantType.AUTH_TOKEN, token);
                    // 登录验证成功，设置token和权限菜单
                    ctx.response().putHeader(ConstantType.AUTH_TOKEN, token).send(data.toString());
                } else {
                    ElevenLoggerFactory.build(this).info("认证失败,请输出正确的用户名和密码");
                    Throwable result = auth.cause();
                    if (StringUtils.isNotEmpty(result)){
                        ctx.response().send(result.getMessage());
                        return;
                    }
                    AuthenticateResponse response = Builder.of(AuthenticateResponse::new)
                            .with(AuthenticateResponse::setCode, "B0000000001")
                            .with(AuthenticateResponse::setMessage, "用户认证失败，无访问权限，请登录").build();
                    ctx.response().send(JsonObject.mapFrom(response).toString());
                }
            });
            MDCThreadUtil.clear();
            return;
        }

        // 上传token，则进行token验证
        if (authtoken(authToken)) {
            if (!ctx.request().isEnded()) {
                ctx.request().resume();
            }
            JsonObject payload = null;
            try {
                payload = this.jwt.decode(authToken);
            } catch (RuntimeException var5) {
                // 认证失败，返回信息
                AuthenticateResponse response = Builder.of(AuthenticateResponse::new)
                        .with(AuthenticateResponse::setCode, "B0000000001")
                        .with(AuthenticateResponse::setMessage, "token解析失败").build();
                ctx.response().send(JsonObject.mapFrom(response).toString());
                return;
            }
            String token = this.authProvider.generateToken(payload, new JWTOptions().setExpiresInMinutes(30));
            ctx.response().putHeader(ConstantType.AUTH_TOKEN, token);
            ctx.next();
            MDCThreadUtil.clear();
            return;
        }
        // 认证失败，返回信息
        AuthenticateResponse response = Builder.of(AuthenticateResponse::new)
                .with(AuthenticateResponse::setCode, "B0000000001")
                .with(AuthenticateResponse::setMessage, "权限认证失败，请登录").build();
        ctx.response().send(JsonObject.mapFrom(response).toString());
        MDCThreadUtil.clear();
    }

    /**
     * 首先检查是否带有token
     *
     * @param authToken
     * @return
     */
    private boolean authtoken(String authToken) {
        // 获取token
        if (StringUtils.isNotBlank(authToken)) {
            AtomicBoolean authNext = new AtomicBoolean(false);
            this.authProvider.authenticate(new JsonObject().put(ConstantType.TOKEN, authToken), auth -> {
                if (auth.succeeded()) {
                    User user = auth.result();
                    JsonObject authData = user.principal();
                    String userId = authData.getString("userCode");
                    ElevenLoggerFactory.build(this).info("用户 {} token认证成功！", userId);
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
}
