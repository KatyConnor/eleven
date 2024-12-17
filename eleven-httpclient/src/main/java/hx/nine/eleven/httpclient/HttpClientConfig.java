package hx.nine.eleven.httpclient;

import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Component
public class HttpClientConfig {


    public void initHttpClient(){

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(1000))
                .build();
        try {
            SSLContext sslContext = SSLContextBuilder.create()
                    .loadTrustMaterial((chain, authType) -> true)// 信任所有证书
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }


        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder
                .create()
                .setDefaultSocketConfig(socketConfig)
                .setMaxConnTotal(1000)
                .setMaxConnPerRoute(50)
                .build();

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.ofMilliseconds(8000))
                .setResponseTimeout(Timeout.ofMilliseconds(8000))
                .setConnectTimeout(Timeout.ofMilliseconds(8000))
                .build();

        CloseableHttpClient httpClient = HttpClients
                .custom()
                .disableRedirectHandling()  // 禁用自动重定向
                .disableContentCompression()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        DefaultElevenApplicationContext.build().addBean(httpClient);
    }

}
