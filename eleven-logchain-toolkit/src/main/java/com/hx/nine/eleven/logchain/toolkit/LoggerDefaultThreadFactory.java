package com.hx.nine.eleven.logchain.toolkit;

import com.hx.nine.eleven.logchain.toolkit.util.MDCThreadUtil;
import com.hx.nine.eleven.thread.pool.executor.factory.DefaultThreadFactory;
import com.hx.vertx.boot.annotations.Component;
import org.slf4j.MDC;
import javax.annotation.Resource;

/**
 *  创建带有日志链路唯一标识ID的线程
 *
 *  @Author wml
 *  @Date 2023/02/06
 */
@Component
public class LoggerDefaultThreadFactory{

    @Resource
    private  LogTraceProperties logTraceProperties;

    public Thread newThread(Runnable r) {
        if (this.logTraceProperties.getEnable()){
           return DefaultThreadFactory.build().newThread(MDCThreadUtil.wrap(r, MDC.getCopyOfContextMap()));
        }
        return DefaultThreadFactory.build().newThread(r);
    }

    public <T extends HXLogThread> T newThread(Class<T> threadClass) {
        T thread = DefaultThreadFactory.build().newThread(threadClass);
        if (this.logTraceProperties.getEnable()){
            thread.setContext(MDC.getCopyOfContextMap());
        }
        return  thread;
    }
}
