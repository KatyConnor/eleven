package hx.nine.eleven.http.client.config;

import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.core.annotations.ConditionalOnProperty;
import hx.nine.eleven.http.client.properties.HttpConfigProperties;
import hx.nine.eleven.http.client.ws.HttpClient;
import hx.nine.eleven.http.client.ws.rest.IdleConnectionMonitor;
import hx.nine.eleven.http.client.ws.soap.SoapClient;

/**
 * @author wml
 * 2020-06-08
 */
@Component
@ConditionalOnProperty(propertiesClass = HttpConfigProperties.class)
public class HttpClientConfig {

    private HttpConfigProperties httpConfigProperties;

    public HttpClientConfig(HttpConfigProperties httpConfigProperties) {
        this.httpConfigProperties = httpConfigProperties;
    }


    public IdleConnectionMonitor iniIidleConnectionMonitor(){
        IdleConnectionMonitor idleConnectionMonitor = new IdleConnectionMonitor();
        idleConnectionMonitor.setClearPeriod(httpConfigProperties.getClearPeriod());
        idleConnectionMonitor.setIdleSeconds(httpConfigProperties.getIdleSeconds());
        return idleConnectionMonitor;
    }


    public HttpClient initHttpClient(){
        HttpClient httpClient = new HttpClient();
        return httpClient;

    }


    public SoapClient initSoapClient(@Qualifier("soapHttpClient") HttpClient httpClient){
        SoapClient soapClient = new SoapClient();
        soapClient.setHttpclient(httpClient);
        soapClient.setCharsetName(httpConfigProperties.getSoapCharsetName());
        return soapClient;
    }
}
