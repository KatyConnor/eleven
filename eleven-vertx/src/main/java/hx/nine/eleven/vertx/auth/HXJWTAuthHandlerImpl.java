package hx.nine.eleven.vertx.auth;

import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.constant.DefaultProperType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.core.utils.MDCThreadUtil;
import hx.nine.eleven.vertx.code.VertxApplicationMsgCode;
import hx.nine.eleven.vertx.constant.DefaultVertxProperType;
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
 * token验证以及用户自定义验证逻辑
 * @author wml
 * @Discription
 * @Date 2023-08-09
 */
public class HXJWTAuthHandlerImpl extends JWTAuthHandlerImpl {

    /**
     * 是否开启权限验证
     */
    private Boolean authentication;
    /**
     * 忽略权限验证路径
     */
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
        UserAuthenticatePermissionProvider provider = ElevenApplicationContextAware.getBean(UserAuthenticatePermissionProvider.class);
        this.authProvider = authProvider;
        this.authentication = properties.getAuthentication();
        String[] ignoreAuthentications = properties.getIgnoreAuthentication();
        this.expires = properties.getExpires();
        this.ignoreAuthentication = ObjectUtils.isNotEmpty(ignoreAuthentications) ? Arrays.asList(ignoreAuthentications) : new ArrayList<>();

        this.permissionsClaimKey = config.getPermissionsClaimKey();
        this.jwtOptions = config.getJWTOptions();
        provider.setAuthProvider(authProvider).setJwtOptions(config.getJWTOptions());
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
        // 检查是否需要鉴权,不需要鉴权则直接执行下一个handler
        final HttpServerRequest request = ctx.request();
        if (!this.authentication || ignoreAuthentication.contains(ctx.request().getHeader(ConstantType.TRADE_CODE))) {
            //不需要鉴权，直接放行
            request.headers().add(DefaultProperType.AUTHENTICATE,"true");
            request.headers().add(DefaultProperType.IS_LOGIN,"true");
            ctx.next();
            return;
        }
        //暂停本次 request 请求
        if (!ctx.request().isEnded()) {
            ctx.request().pause();
        }

        String authToken = request.getHeader(ConstantType.AUTH_TOKEN);
        // 如果token为空，则直接设置登录标志，交由下游domain进行登录验证操作；验证通过后才能进入service业务逻辑处理层
        if (StringUtils.isEmpty(authToken)) {
            // 默认需要登录，设置状态为无权限，且需要登录标志， 交给下游domain拦截处理登录
            request.headers().add(DefaultProperType.AUTHENTICATE,"false");
            request.headers().add(DefaultProperType.IS_LOGIN,"false");
            ctx.next();
            return;
        }
    }


}
