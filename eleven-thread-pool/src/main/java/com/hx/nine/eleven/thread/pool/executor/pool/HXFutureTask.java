package com.hx.nine.eleven.thread.pool.executor.pool;

import com.hx.nine.eleven.thread.pool.executor.factory.LogTraceFactory;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import org.slf4j.MDC;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class HXFutureTask<V> extends FutureTask<V> {


    public HXFutureTask(Callable<V> callable) {
        super(callable);
    }

    public HXFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    @Override
    public void run() {
        LogTraceFactory logTraceFactory = DefaultVertxApplicationContext.build().getBean(LogTraceFactory.class);
        if (logTraceFactory != null){
            logTraceFactory.wrapTraceId();
        }
        super.run();
        if (logTraceFactory != null){
            MDC.clear();
        }
    }
}
