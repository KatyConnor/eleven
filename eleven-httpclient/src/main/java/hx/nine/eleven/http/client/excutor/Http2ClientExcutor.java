package hx.nine.eleven.http.client.excutor;//package hx.http.client.excutor;
//
//import com.alibaba.fastjson.JSONObject;
//import jdk.incubator.http.HttpClient;
//import jdk.incubator.http.HttpRequest;
//import jdk.incubator.http.HttpResponse;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.conn.ssl.NoopHostnameVerifier;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.ssl.SSLContextBuilder;
//import org.apache.http.ssl.SSLContexts;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.net.ssl.SSLContext;
//import java.io.File;
//import java.io.IOException;
//import java.net.*;
//import java.security.KeyManagementException;
//import java.security.KeyStore;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.util.Map;
//
///**
// * @Author mingliang
// * @Date 2018-08-01 17:27
// */
////@Component
//public class Http2ClientExcutor {
//
//    private static final String QUESTION_MARK = "?";
//    private static final String AND_MARK = "&";
//    private static final String EQUAL = "=";
//
//    @Autowired
//    private RequestConfig requestConfig;
//
//    public static String get() throws URISyntaxException, IOException, InterruptedException {
//        HttpClient client = HttpClient.newHttpClient();
//        HttpClient.newBuilder().authenticator(new Authenticator() {
//            @Override
//            public PasswordAuthentication requestPasswordAuthenticationInstance(String host, InetAddress addr, int port, String protocol, String prompt, String scheme, URL url, RequestorType reqType) {
//                return super.requestPasswordAuthenticationInstance(host, addr, port, protocol, prompt, scheme, url, reqType);
//            }
//
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return  new PasswordAuthentication("username", "password".toCharArray());
//            }
//
//            @Override
//            protected URL getRequestingURL() {
//                return super.getRequestingURL();
//            }
//
//            @Override
//            protected RequestorType getRequestorType() {
//                return super.getRequestorType();
//            }
//        });
//        HttpRequest request = HttpRequest.newBuilder().uri(new URI("https://labs.consol.de/")).build();
//        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandler.asString());
//        System.out.println(response.statusCode());
//        System.out.println(response.body());
//        return "";
//    }
//
//    public <T> T excute(Map<String,Object> params,Class<T> classzz){
//
//        return JSONObject.parseObject("",classzz);
//    }
//
//    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//        URI uri = new URI("https://localhost:8081/webPlatform/test/rest");
//        HttpRequest httpRequest = HttpRequest.newBuilder(uri).GET().build();
////        HttpRequest.Builder post = HttpRequest.newBuilder(uri).POST(HttpRequest.BodyPublisher.fromString("###要请求的参数")).headers();
//        //post.setHeader可以设置UA、Cookie等参数
////        post.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
//        URL classPath = Thread.currentThread().getContextClassLoader().getResource("keystore.p12");
////        HttpClient httpClient = getHttpClient();
//        SSLContext sslcontext = null;
//        // Trust own CA and all self-signed certs
//        try {
//            sslcontext = SSLContexts.custom()
//                    .loadTrustMaterial(new File(classPath.getPath()), "root,.159357".toCharArray(), new TrustSelfSignedStrategy())
//                    .build();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////        httpClient.sslContext();
////        KeyStore truststore = new KeyStore();
////        SSLContextBuilder sslContext = new SSLContextBuilder().loadTrustMaterial(, new TrustSelfSignedStrategy());
////        sslContext.l
////        new SSLConnectionSocketFactory(sslContext.build(),SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//        HttpClient httpClient = HttpClient.newBuilder().sslContext(sslcontext).build();
//        HttpResponse httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandler.asString());
//        System.out.println(JSONObject.toJSONString(httpResponse));
////        httpClient.sendAsync()
//        int code = httpResponse.statusCode();
//        System.out.println(code);
////        System.out.println(uncompress(httpResponse.body().toString().getBytes()));
//    }
//
//    // 包含授信公钥文件路径
////    static String trustStorePath = HttpClientUtils.class.getResource("/rabbittruststore.jks").getPath();
//
//    /**
//     *  获取安全的加密（Https）的HttpClient
//     * @return
//     */
////    public static HttpClient getTLSHttpClient(){
////        SSLContext sslcontext = null;
////        // Trust own CA and all self-signed certs
////        try {
////
////            sslcontext = SSLContexts.custom()
////                    .loadTrustMaterial(new File("keystore.p12"), "root,.159357".toCharArray(), new TrustSelfSignedStrategy())
////                    .build();
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        // Allow TLSv1 protocol only
////        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
////                sslcontext, new String[] { "TLSv1" }, null,
////                NoopHostnameVerifier.INSTANCE);
////
////        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
////        return httpclient;
////    }
//
////    private static HttpClient getHttpClient(){
////        return HttpClient.newHttpClient();
////    }
//
//    private HttpClient.Builder getBuilder(){
//        return HttpClient.newBuilder();
//    }
//
////    public static String uncompress(byte[] bytes) {
////        if (bytes == null || bytes.length == 0) {
////            return null;
////        }
////        ByteArrayOutputStream out = new ByteArrayOutputStream();
////        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
////        try {
////            GZIPInputStream ungzip = new GZIPInputStream(in);
////            byte[] buffer = new byte[256];
////            int n;
////            while ((n = ungzip.read(buffer)) >= 0) {
////                out.write(buffer, 0, n);
////            }
////        } catch (IOException e) {
////            System.out.println(e);
////        }
////
////        return out.toString();
////    }
//
//
//
//
//
//    private HttpResponse doGet(String url) throws URISyntaxException {
//        return doGet(url,null);
//    }
//
//    private HttpResponse doGet(String url, Map<String,Object> params) throws URISyntaxException {
//        // 组装参数
//        if (null != params) {
//            url = doGetParams(url,params);
//        }
//
//        return null;
//    }
//
//    private String doGetParams(String url,Map<String,Object> params){
//        StringBuffer sb = new StringBuffer();
//        sb.append(url).append(QUESTION_MARK);
//        final Integer[] mark = {0};
//        params.forEach((k,v) ->{
//            sb.append(k).append(EQUAL).append(v).append(mark[0] == params.size()-1?"":AND_MARK);
//            mark[0]++;
//        });
//        return sb.toString();
//    }
//
//
//    public void getHttpClient() throws URISyntaxException, IOException, InterruptedException {
////        HttpRequest req = HttpRequest.create(new URI("http://www.infoq.com"))
////                .body(noBody()).GET();
////        CompletableFuture<HttpResponse> aResp = req.sendAsync();
////        Thread.sleep(10);
////        if (!aResp.isDone()) {
////            aResp.cancel(true);
////            System.out.println("Failed to reply quickly...");
////            return;
////        }
////        HttpResponse response = aResp.get();
//
//        HttpClient httpClient = HttpClient.newHttpClient();
//        HttpResponse httpResponse = httpClient.send(
//                HttpRequest.newBuilder(new URI("http://transport.opendata.ch/")).GET().build(),
//                HttpResponse.BodyHandler.asString()
//        );
//        int code = httpResponse.statusCode();
//        System.out.println(code);
//        System.out.println(httpResponse.body().toString());
//    }
//
//    public void postTest() throws URISyntaxException {
////        URI uri = new URI("https://www.baidu.com/");
////        HttpRequest.Builder post = HttpRequest.newBuilder(uri).POST(HttpRequest.BodyProcessor.fromString("###要请求的参数"));
////        //post.setHeader可以设置UA、Cookie等参数
////        post.setHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3 like Mac OS X) AppleWebKit/602.1.50 (KHTML, like Gecko) CriOS/56.0.2924.75 Mobile/14E5239e Safari/602.1");
////
////        HttpClient httpClient = HttpClient.newHttpClient();
////        HttpResponse httpResponse = httpClient.send(post.build(), HttpResponse.BodyHandler.asString());
////        int code = httpResponse.statusCode();
////        System.out.println(code);
////        System.out.println(uncompress(httpResponse.body().toString().getBytes()));
//    }
//
//
//}
