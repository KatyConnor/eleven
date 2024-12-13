package hx.nine.eleven.http.client.config;

import hx.nine.eleven.http.client.entity.ContentType;
import hx.nine.eleven.http.client.entity.HttpHeader;
import hx.nine.eleven.http.client.enums.RequestMethodEnum;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author mingliang
 * @Date 2020-06-06
 */
public class HttpRequestConfig<REQ,BODY>{

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestConfig.class);

    private RequestMethodEnum method;
    private String hostAddress;
    private String url;
    private HttpHeader headers;
    private BODY params;
    private REQ requestParam;
    private RequestConfig requestConfig;
    private boolean form;
    private ContentType contentType;
    private boolean cookieStore;
    private HttpClientContext httpClientContext;


    private HttpRequestConfig(){}

    public static HttpRequestConfig build(){
        return new HttpRequestConfig();
    }

//    public HttpRequestConfig body(Map<String,Object> paramMap){
//        this.params = checkMap(this.params);
//        this.params.putAll(paramMap);
//        return this;
//    }

//    public HttpRequestConfig body(Object param){
//        this.params = checkMap(this.params);
//        if (param instanceof JSONObject){
//            this.params.putAll(JSONObject.parseObjectToMap(param));
//            return this;
//        }
//
//        if (param instanceof Map){
//            return body((Map<String, Object>) param);
//        }
//
//        try {
//            this.params.putAll(BeanMapUtil.beanToMap(param));
//        } catch (Exception e) {
//            LOGGER.error("cast bean to map,exception:{}",e);
//        }
//        return this;
//    }

//    public HttpRequestConfig body(String jsonParam){
//        ObjectMapper object = new ObjectMapper();
//        Map<String,Object> params = null;
//        try{
//            params = object.readValue(jsonParam,Map.class);
//        }catch (Exception e){
//            LOGGER.error("param string is not json string, jsonParam:{}",jsonParam);
//        }
//        this.params = checkMap(this.params);
//        this.params.putAll(params);
//        return this;
//    }

//    public HttpRequestConfig body(String key,Object value){
//        this.params = checkMap(this.params);
//        this.params.put(key,value);
//        return this;
//    }
//
//    public HttpRequestConfig requestParam(Map<String,Object> paramMap){
//        this.requestParam = checkMap(this.requestParam);
//        this.requestParam.putAll(paramMap);
//        return this;
//    }
//
//    public HttpRequestConfig requestParam(Object param){
//        this.requestParam = checkMap(this.requestParam);
//        if (param instanceof JSONObject){
//            this.requestParam.putAll(JSONObject.parseObjectToMap(param));
//            return this;
//        }
//
//        if (param instanceof Map){
//            return requestParam((Map<String, Object>) param);
//        }
//
//        try {
//            this.requestParam.putAll(BeanMapUtil.beanToMap(param));
//        } catch (Exception e) {
//            LOGGER.error("cast bean to map,exception:{}",e);
//        }
//        return this;
//    }
//
//    public HttpRequestConfig requestParam(String jsonParam){
//        Map<String,Object> requestParam = null;
//        try{
//            requestParam = JSONObject.parseObjectToMap(jsonParam);
//        }catch (Exception e){
//            LOGGER.error("param string is not json string, jsonParam:{}",jsonParam);
//        }
//        this.requestParam = checkMap(this.requestParam);
//        this.requestParam.putAll(requestParam);
//        return this;
//    }
//
//    public HttpRequestConfig requestParam(String key,String value){
//        this.requestParam = checkMap(this.requestParam);
//        this.requestParam.put(key,value);
//        return this;
//    }

    public RequestMethodEnum getMethod() {
        return method;
    }

    public HttpRequestConfig setMethod(RequestMethodEnum method) {
        this.method = method;
        return this;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public HttpRequestConfig setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpRequestConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public HttpRequestConfig setHeaders(HttpHeader headers) {
        this.headers = headers;
        return this;
    }

    public BODY getParams() {
        return params;
    }

    public void setParams(BODY params) {
        this.params = params;
    }


    public REQ getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(REQ requestParam) {
        this.requestParam = requestParam;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }

    public HttpRequestConfig setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
        return this;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public HttpRequestConfig setContentType(ContentType contentType) {
        this.contentType = contentType;
        return this;

    }

    public boolean isForm() {
        return form;
    }

    public HttpRequestConfig setForm(boolean form) {
        this.form = form;
        return this;
    }

    public boolean isCookieStore() {
        return cookieStore;
    }

    public HttpRequestConfig setCookieStore(boolean cookieStore) {
        this.cookieStore = cookieStore;
        return this;
    }

    public HttpClientContext getHttpClientContext() {
        return httpClientContext;
    }

    public HttpRequestConfig setHttpClientContext(HttpClientContext httpClientContext) {
        this.httpClientContext = httpClientContext;
        return this;
    }

//    private Map<String,Object> checkMap(Map<String,Object> map){
//        if (null == map){
//            return new HashMap<>();
//        }
//        return map;
//    }

}
