package hx.nine.eleven.httpclient;

import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.core.context.ElevenApplicationContext;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.util.Timeout;

@Component
public class HttpClientConfig {


    public void initHttpClient(){

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(Timeout.ofMilliseconds(1000))
                .build();

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
                .disableContentCompression()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();

        DefaultElevenApplicationContext.build().addBean(httpClient);
    }

}
