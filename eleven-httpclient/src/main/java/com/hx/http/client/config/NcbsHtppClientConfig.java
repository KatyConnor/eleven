package com.hx.http.client.config;

import com.hx.http.client.properties.HttpConfigProperties;
import com.hx.http.client.ws.HttpClient;
import com.hx.http.client.ws.rest.IdleConnectionMonitor;
import com.hx.http.client.ws.soap.SoapClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wml
 * 2020-06-08
 */
@Configuration
@EnableConfigurationProperties(value = {HttpConfigProperties.class})
public class NcbsHtppClientConfig {

    private HttpConfigProperties httpConfigProperties;

    public NcbsHtppClientConfig(HttpConfigProperties httpConfigProperties) {
        this.httpConfigProperties = httpConfigProperties;
    }

    @Bean("idleConnectionMonitor")
    public IdleConnectionMonitor iniIidleConnectionMonitor(){
        IdleConnectionMonitor idleConnectionMonitor = new IdleConnectionMonitor();
        idleConnectionMonitor.setClearPeriod(httpConfigProperties.getClearPeriod());
        idleConnectionMonitor.setIdleSeconds(httpConfigProperties.getIdleSeconds());
        return idleConnectionMonitor;
    }

    @Bean("soapHttpClient")
    public HttpClient initHttpClient(){
        HttpClient httpClient = new HttpClient();
        return httpClient;

    }

    @Bean("soapClient")
    public SoapClient initSoapClient(@Qualifier("soapHttpClient") HttpClient httpClient){
        SoapClient soapClient = new SoapClient();
        soapClient.setHttpclient(httpClient);
        soapClient.setCharsetName(httpConfigProperties.getSoapCharsetName());
        return soapClient;
    }
}
