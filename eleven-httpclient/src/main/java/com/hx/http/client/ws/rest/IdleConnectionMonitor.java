package com.hx.http.client.ws.rest;

import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wml
 * 2020-06-08
 */
public class IdleConnectionMonitor{

    private static final Logger logger = LoggerFactory.getLogger(IdleConnectionMonitor.class);

    private long idleSeconds;
    private long clearPeriod;

    public IdleConnectionMonitor() {
    }

    public long getIdleSeconds() {
        return this.idleSeconds;
    }

    public void setIdleSeconds(long idleSeconds) {
        this.idleSeconds = idleSeconds;
    }

    public long getClearPeriod() {
        return this.clearPeriod;
    }

    public void setClearPeriod(long clearPeriod) {
        this.clearPeriod = clearPeriod;
    }

    public void afterPropertiesSet() throws Exception {
        HttpClientConnectionManager clientConnectionManager = (HttpClientConnectionManager)context.getBean(HttpClientConnectionManager.class);
        if (clientConnectionManager == null) {
            logger.warn("================启动清理空闲连接线程失败：找不到HttpClientConnectionManager类。如未使用连接池请忽略此警告=========================");
        } else {
            IdleConnectionMonitorThread thread = new IdleConnectionMonitorThread(clientConnectionManager, this.idleSeconds, this.clearPeriod);
            thread.start();
        }

    }
}
