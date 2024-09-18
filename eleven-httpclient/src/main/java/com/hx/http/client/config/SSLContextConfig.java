package com.hx.http.client.config;

import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.net.URL;

/**
 * @Author mingliang
 * @Date 2018-08-03 17:40
 */
//@Configuration
//@ConfigurationProperties(prefix = "servlet.https.ssl")
public class SSLContextConfig {

    // 安全认证文件
    private static final URL classPath = Thread.currentThread().getContextClassLoader().getResource("keystore.p12");

    @Value("${keystorePath}")
    private String keystorePath;

    @Value("${keystorePassword}")
    private String keystorePassword;

//    @Value("${http.maxIdleTime}")
//    private long maxIdleTime;

    // 采用http2
    @Bean(name = "sSLContext")
    public SSLContext initSSLContext(){
        SSLContext sslcontext = null;
        try {
            sslcontext = org.apache.http.ssl.SSLContexts.custom()
                    .loadTrustMaterial(new File(classPath.getPath()), keystorePassword.toCharArray(),new TrustSelfSignedStrategy())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslcontext;
    }
}
