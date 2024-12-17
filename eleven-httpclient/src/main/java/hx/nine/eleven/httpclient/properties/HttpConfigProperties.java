package hx.nine.eleven.httpclient.properties;

import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

/**
 * @Author mingliang
 * @Date 2020-06-08
 */

@ConfigurationPropertiesBind(prefix = "eleven.boot.http.client")
public class HttpConfigProperties {

    //整个连接池的并发
    private int maxTotal;
    //每个主机的并发
    private int defaultMaxPerRoute;
    //连接的最长时间
    private int connectTimeout;
    //从连接池中获取到连接的最长时间
    private int connectionRequestTimeout;
    //读取超时时间
    private int readTimeout;
    //是否缓存读取
    private boolean bufferRequestBody;
    //数据传输的最长时间
    private int socketTimeout;
    //提交请求前测试连接是否可用
    private boolean staleConnectionCheckEnabled;
    // 重试次数
    private int retryCount;
    // 是否重试发起
    private boolean requestSentRetryEnabled;
    private long maxIdleTime;
    // 请求报文头默认配置
    private String[] basicHeader;
    private long idleSeconds;
    private long clearPeriod;
    private String soapCharsetName;

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getDefaultMaxPerRoute() {
        return defaultMaxPerRoute;
    }

    public void setDefaultMaxPerRoute(int defaultMaxPerRoute) {
        this.defaultMaxPerRoute = defaultMaxPerRoute;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isBufferRequestBody() {
        return bufferRequestBody;
    }

    public void setBufferRequestBody(boolean bufferRequestBody) {
        this.bufferRequestBody = bufferRequestBody;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public boolean isStaleConnectionCheckEnabled() {
        return staleConnectionCheckEnabled;
    }

    public void setStaleConnectionCheckEnabled(boolean staleConnectionCheckEnabled) {
        this.staleConnectionCheckEnabled = staleConnectionCheckEnabled;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public String[] getBasicHeader() {
        return basicHeader;
    }

    public void setBasicHeader(String[] basicHeader) {
        this.basicHeader = basicHeader;
    }

    public long getIdleSeconds() {
        return idleSeconds;
    }

    public void setIdleSeconds(long idleSeconds) {
        this.idleSeconds = idleSeconds;
    }

    public long getClearPeriod() {
        return clearPeriod;
    }

    public void setClearPeriod(long clearPeriod) {
        this.clearPeriod = clearPeriod;
    }

    public String getSoapCharsetName() {
        return soapCharsetName;
    }

    public void setSoapCharsetName(String soapCharsetName) {
        this.soapCharsetName = soapCharsetName;
    }
}
