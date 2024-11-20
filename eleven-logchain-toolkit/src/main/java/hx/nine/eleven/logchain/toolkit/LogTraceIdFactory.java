package hx.nine.eleven.logchain.toolkit;

import hx.nine.eleven.logchain.toolkit.util.HXLogger;
import hx.nine.eleven.logchain.toolkit.util.MDCThreadUtil;
import hx.nine.eleven.thread.pool.executor.factory.LogTraceFactory;
import hx.nine.eleven.core.annotations.Component;

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
