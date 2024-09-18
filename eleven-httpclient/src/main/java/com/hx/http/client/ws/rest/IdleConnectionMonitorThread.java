package com.hx.http.client.ws.rest;

import java.util.concurrent.TimeUnit;
import org.apache.http.conn.HttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wml
 * 2020-06-08
 */
public class IdleConnectionMonitorThread extends Thread {
    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;
    private long idleSeconds = 5L;
    private long clearPeriod = 5000L;
    private static final Logger LOGGER = LoggerFactory.getLogger(IdleConnectionMonitorThread.class);

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr, long idleSeconds, long clearPeriod) {
        if (clearPeriod > 0L) {
            this.clearPeriod = clearPeriod;
        }

        if (idleSeconds > 0L) {
            this.idleSeconds = idleSeconds;
        }

        this.connMgr = connMgr;
    }

    public void run() {
        while(true) {
            try {
                if (!this.shutdown) {
                    synchronized(this) {
                        this.wait(this.clearPeriod);
                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.info("===================清理空闲和已过期HTTP链接======================");
                        }

                        this.connMgr.closeExpiredConnections();
                        this.connMgr.closeIdleConnections(this.idleSeconds, TimeUnit.SECONDS);
                        continue;
                    }
                }
            } catch (InterruptedException var4) {
                LOGGER.error("定时清理空闲和已过期HTTP链接异常", var4);
            }

            return;
        }
    }

    public void shutdown() {
        this.shutdown = true;
        synchronized(this) {
            this.notifyAll();
        }
    }

}
