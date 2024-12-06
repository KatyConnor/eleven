package hx.nine.eleven.vertx;

import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystemException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.impl.jose.JWK;
import io.vertx.ext.auth.impl.jose.JWT;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.impl.JWTAuthProviderImpl;
import io.vertx.ext.web.handler.JWTAuthHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.util.List;

/**
 * @auth wml
 * @date 2024/12/6
 */
public class JWTDemoTest {

	public static void main(String[] args) {
		// 如果你需要使用RS256签名刷法，使用的RSA私钥必须别名必须为: -alias RS256， 如果你使用-alias my_rs256_private_key，Vert.x将找不到密钥。
		jwtCreate1();
	}


	public static  void jwtCreate1() {
		final JsonObject claims = new JsonObject()
				.put("userid", 1)
				.put("email", "jwt@example.com")
				.put("role", "admin");

		final JWTOptions jwtOptions = new JWTOptions()
				.setAlgorithm("RS256")  // header
				.setIssuer("auth-server")
				.setSubject("FSSM")
				.setExpiresInSeconds(60 * 60 * 24 * 360); //60 * 60 * 24 * 360

		final JWT jwt = new JWT();

		Vertx vertx = Vertx.vertx();
		JWTAuthOptions config = new JWTAuthOptions().setKeyStore(
				new KeyStoreOptions()
						.setPath("D:\\code\\" + "mykeystore2.p12")
						.setType("pkcs12")
						.setPassword("storepass"));
		JWTAuth jwtAuth = JWTAuth.create(vertx, config);
		String token = jwtAuth.generateToken(claims, jwtOptions); // 生成令牌
		System.out.println(token);
		jwt.nonceAlgorithm(jwtOptions.getNonceAlgorithm());
		final KeyStoreOptions keyStore = config.getKeyStore();
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
						e.printStackTrace();
					}
				}
			}
		} catch (KeyStoreException | IOException | FileSystemException | CertificateException | NoSuchAlgorithmException |
				NoSuchProviderException e) {
			throw new RuntimeException(e);
		}

		JsonObject jsonObject = jwt.decode(token);
		System.out.println(jsonObject.toString());
	}


}
