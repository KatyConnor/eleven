package com.hx.logchain.toolkit;

import com.hx.logchain.toolkit.util.HXLogger;
import com.hx.logchain.toolkit.util.MDCThreadUtil;
import com.hx.nine.eleven.thread.pool.executor.factory.LogTraceFactory;
import com.hx.vertx.boot.annotations.Component;

/**
 *
 */
@Component
public class LogTraceIdFactory extends LogTraceFactory {
    public LogTraceIdFactory() {
        HXLogger.build(this).info("初始化日志链路ID生成工厂类");
    }

    @Override
    public void wrapTraceId() {
        MDCThreadUtil.wrap();
    }
}
