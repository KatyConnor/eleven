package com.hx.http.client.excutor;

import com.hx.http.client.config.HttpRequestConfig;
import com.hx.http.client.entity.HttpHeader;
import com.hx.http.client.enums.HttpStatus;
import com.hx.http.client.enums.RequestMethodEnum;
import com.hx.http.client.exception.HttpConnectException;
import com.hx.http.client.response.HttpResponse;
import com.hx.http.client.utils.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hx.lang.commons.utils.BeanMapUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mingliang
 * @Date 2017-10-11 15:50
 */
public class HttpClientExcutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientExcutor.class);
    private static final String UTF_8 = "UTF-8";

    // 创建Httpclient对象
    private CloseableHttpClient httpClient;

    // 请求信息的配置 Httpclient
    private RequestConfig requestConfig;

    private HttpClientContext httpClientContext;

    /**
     *
     * @param httpRequestConfig
     * @param classzz
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public HttpResponse excute(HttpRequestConfig httpRequestConfig,
							   Class<? extends HttpResponse> classzz)
            throws IOException, URISyntaxException {

        switch (httpRequestConfig.getMethod()){
            case GET:
                return doGet(httpRequestConfig,classzz);
            case POST:
                return doPost(httpRequestConfig,classzz);
            case PUT:
                break;
            case HEAD:
                break;
            case TRACE:
                break;
            case DELETE:
                break;
            case CONNECT:
                break;
            case OPTIONS:
                break;
            default:
                throw new HttpConnectException(String.format("request method is not in method list,method :[s%], methodList:[s%]",
                        httpRequestConfig.getMethod(), RequestMethodEnum.values()));
        }
        return null;
    }


    /**
     * 执行Get请求
     *
     * @param httpRequestConfig 请求中的参数
     * @param classzz           返回数据封装类
     * @return <T>
     * @throws URISyntaxException
     * @throws IOException
     * @throws ClientProtocolException
     */
    private <T> HttpResponse<T> doGet(HttpRequestConfig httpRequestConfig, Class<T> classzz)
            throws URISyntaxException, ClientProtocolException, IOException {

        LOGGER.info("do get request, requestConfig:{}",JSONObject.toJSONString(httpRequestConfig));
        String url = getUrl(httpRequestConfig.getHostAddress(),httpRequestConfig.getUrl());
        URI uri = setGetUrl(url,httpRequestConfig.getParams());
        // 创建http GET请求
        HttpGet httpGet =new HttpGet(uri);

        // 设置请求参数
        HttpHeader header = httpRequestConfig.getHeaders();
        // 如果没有设置header 伪装浏览器请求
        setHeader(httpGet,null,null != header && null != header.getHeaders()?header.getHeaders():HttpHeader.build().setDfaultHeader());
        // 配置请求的超时设置
        setConfig(httpGet,null,httpRequestConfig);

        // 获取HTTPContext，维持cookie
        boolean cookieStore = setCookie(httpRequestConfig);
        // 请求的结果
        return execute(null,httpGet,cookieStore,classzz);
    }

    /**
     * post 请求
     * @param httpRequestConfig 参数配置
     * @param classzz
     * @return
     * @throws URISyntaxException
     * @throws ClientProtocolException
     * @throws IOException
     */
    private <T> HttpResponse<T> doPost(HttpRequestConfig httpRequestConfig,
                                                           Class<T> classzz) throws URISyntaxException,
            ClientProtocolException, IOException {
        LOGGER.info("do post request, requestConfig:{}",JSONObject.toJSONString(httpRequestConfig));
        // 创建http POST请求
        String url = getUrl(httpRequestConfig.getHostAddress(),httpRequestConfig.getUrl());
        Object requestParam = httpRequestConfig.getRequestParam();
        if (null != requestParam){
            url = setPostRequestParam(url, BeanMapUtil.beanToMap(requestParam));
        }
        HttpPost httpPost = new HttpPost(url);

        // 设置请求头参数
        HttpHeader header = httpRequestConfig.getHeaders();
        // 如果没有设置header默认伪装浏览器请求
        setHeader(null,httpPost, null != header && null != header.getHeaders()?header.getHeaders():HttpHeader.build().setDfaultHeader());

        Object params = httpRequestConfig.getParams();
        setParam(params,httpPost,httpRequestConfig.isForm());
        // 配置请求的超时设置
        setConfig(null,httpPost,httpRequestConfig);

        // 获取HTTPContext， 维持cookie
        boolean cookieStore = setCookie(httpRequestConfig);
        // 请求的结果
        return execute(httpPost,null,cookieStore,classzz);
    }

    /**
     * 请求执行
     * @param httpPost  post
     * @param httpGet   get
     * @param classzz   结果包装对象
     * @param <T>       数据data封装对象
     * @return
     * @throws IOException
     */
    private <T> HttpResponse<T> execute(HttpPost httpPost,HttpGet httpGet,boolean cookieStore,
                                        Class<T> classzz) throws IOException {
        CloseableHttpResponse response = null;
        HttpResponse httpResponse = new HttpResponse();
        try {
            // 执行请求
            if (cookieStore){
                response = null != httpPost?httpClient.execute(httpPost,this.httpClientContext):httpClient.execute(httpGet,this.httpClientContext);
            }else {
                response = null != httpPost?httpClient.execute(httpPost):httpClient.execute(httpGet);
            }

            // 获取服务端返回的数据,并返回
            T parseObject = null;
            String resultStr =EntityUtils.toString(response.getEntity(), UTF_8);
            try{
                parseObject = JSONObject.parseObject(resultStr,classzz);
                httpResponse.setData(parseObject);
            }catch (Exception e){
                httpResponse.setData(resultStr);
            }
            httpResponse.setCode(HttpStatus.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() != 200){
                httpResponse.failure();
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
//        LOGGER.info("request service end, response ：{}",JSONObject.toJSONStringWithDateFormat(httpResponse,"yyyy-MM-dd HH:mm:ss"));
        return httpResponse;
    }

    /**
     * 设置 header
     * @param httpGet     get
     * @param httpPost    post
     * @param headerMap   参数组
     * @since jdk 1.8
     */
    private void setHeader(HttpGet httpGet,HttpPost httpPost,Map<String,String> headerMap){
        if (headerMap != null && headerMap.size() > 0){
            if (null != httpGet){
                headerMap.forEach((k,v) ->{
                    httpGet.setHeader(k,v);
                });
            }

            if (null != httpPost){
                headerMap.forEach((k,v) ->{
                    httpPost.setHeader(k,v);
                });
            }
        }
    }

    /**
     * 设置get 请求参数
     * @param url
     * @param params
     * @return 返回一个 URL 对象
     * @throws URISyntaxException
     */
    private URI setGetUrl(String url, Map<String, Object> params) throws URISyntaxException {
        return setUrl(url,params);
    }

    /**
     * 设置post的requestParam 参数
     * @param url
     * @param params
     * @return 返回一个带参数的 String 类型的 url
     * @throws URISyntaxException
     */
    private String setPostRequestParam(String url, Map<String, Object> params) throws URISyntaxException{
        URI uri = setUrl(url,params);
        return uri.toString();
    }

    /**
     * 设置请求的链接地址
     * @param url  请求的URL地址
     * @param params 请求的参数
     * @return
     * @throws URISyntaxException
     */
    private URI setUrl(String url, Map<String, Object> params) throws URISyntaxException {
        URIBuilder builder = new URIBuilder(url);
        if (params != null) {
            params.forEach((k,v) ->{
                // @TODO 如果是时间和其他类型的对象参数需要进行修改
                builder.addParameter(k,String.valueOf(v));
            });
            return builder.build();
        }
        return builder.build();
    }

    /**
     * 获取请求的url
     * @param hostAddress 请求主机、服务器IP/域名
     * @param requestUrl 访问的接口地址
     * @return
     */
    private String getUrl(String hostAddress,String requestUrl){
        if (null == hostAddress || hostAddress.isEmpty()){
            throw new HttpConnectException(String.format("request HostAddress can not be null! HostAddress:s%",hostAddress));
        }

        if (null == requestUrl || requestUrl.isEmpty()){
            throw new HttpConnectException(String.format("request url can not be null! url:s%",requestUrl));
        }

        return new StringBuffer().append(hostAddress.endsWith("/")?hostAddress:hostAddress+"/").
                append(requestUrl.startsWith("/")?requestUrl.substring(1):requestUrl).toString();
    }

    private static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 组装 post 参数
     * @param params
     * @param httpPost
     * @param isform
     * @throws UnsupportedEncodingException
     */
    private void setParam(Map<String,Object> params,HttpPost httpPost,boolean isform) throws UnsupportedEncodingException {
        if (params != null) {
            // 设置post参数
            final List<NameValuePair> parameters = new ArrayList<>();
            final Map<String,String> paramsObject = new HashMap<>();
            params.forEach((k,v) ->{
                if (isform){
                    parameters.add(new BasicNameValuePair(k,String.valueOf(v)));
                }else {
                    paramsObject.put(k,String.valueOf(v));
                }
            });

            // 构造一个form表单式的实体
            if (parameters.size() >0){
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters);
                httpPost.setEntity(formEntity);
            }
            if (paramsObject.size() >0){
                // 构建一个json，body数据传递
//                JacksonJsonParser parser = new JacksonJsonParser();
                StringEntity stringEntity = new StringEntity(JSONObject.toJSONString(paramsObject), Charset.forName("UTF-8"));
                httpPost.setEntity(stringEntity);
            }
        }
    }

    private void setConfig(HttpGet httpGet,HttpPost httpPost,HttpRequestConfig httpRequestConfig){
        RequestConfig requestConfig = httpRequestConfig.getRequestConfig();
        if (null != httpGet ){
            httpGet.setConfig(null != requestConfig?requestConfig:this.requestConfig);
        }else {
            httpPost.setConfig(null != requestConfig?requestConfig:this.requestConfig);
        }
    }

    private boolean setCookie(HttpRequestConfig httpRequestConfig){
        boolean cookieStore = httpRequestConfig.isCookieStore();
        HttpClientContext context = httpRequestConfig.getHttpClientContext();
        if (cookieStore && null != context){
            this.httpClientContext = context;
        }
        return cookieStore;
    }

}
