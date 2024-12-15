package hx.nine.eleven.httpclient;

import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;

import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class ElevenHttpClient {

    private CloseableHttpClient httpClient;

    private String url;

    private String method;

    private Map<String, String> headers;

    private Map<String,Object> body;

    public ElevenHttpClient build(){
       if (this.httpClient == null){
           this.httpClient = ElevenApplicationContextAware.getBean(CloseableHttpClient.class);
       }
        HttpClients.custom()
                .disableRedirectHandling()  // 禁用自动重定向
                .build();

        return new ElevenHttpClient();
    }

    public String getOrHead(String url, String method, Map<String, String> headers) throws IOException {
        HttpUriRequestBase request = new HttpUriRequestBase(method, URI.create(url));
        BasicHeader[] head = mapToHeaders(headers);
        request.setHeaders(head);
        return httpClient.execute(request,response -> {
            try {
                return EntityUtils.toString( response.getEntity());
            } catch (ParseException var3) {
                throw new ClientProtocolException(var3);
            }
        });
    }


    public CloseableHttpResponse post(String url, Map<String, String> headers, HttpEntity httpEntity) throws IOException {
        HttpPost request = new HttpPost(url);
        BasicHeader[] head = mapToHeaders(headers);
        request.setHeaders(head);
        request.setEntity(httpEntity);
        return httpClient.execute(request);
    }

    public static BasicHeader[] mapToHeaders(Map<String, String> map) {
        BasicHeader[] headers = new BasicHeader[map.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            headers[i++] = new BasicHeader(entry.getKey(), entry.getValue());
        }
        return headers;
    }

    public static void closeQuietly(Closeable is) {
        if (is != null) {
            try {
                is.close();
            } catch (Exception ex) {
//                log.error("Resources encounter an exception when closing，ex：{}", ex.getMessage());
            }
        }
    }
}
