package com.hx.vertx.file.monitor.thread.pool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 默认拒绝策略
 *
 * @Author wml
 * @Date 2017-12-29 14:33
 */
public class DefaultRefuseThreadPoolHandler implements RejectedExecutionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRefuseThreadPoolHandler.class);

    private DefaultRefuseThreadPoolHandler() {
    }

    public static DefaultRefuseThreadPoolHandler build() {
        return BeanNewInstanceFactory.NEW_INSTANCE;
    }

    /**
     * 线程处理策略，线程重新放回队列queue中
     *
     * @param r
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            LOGGER.info("当前线程重新放回queue！threadName = {},threadId = {}", Thread.currentThread().getName(), Thread.currentThread().getId());
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            LOGGER.info("当前线程重新放回queue失败！exception = {}", e);
        }
    }

    public static final class BeanNewInstanceFactory{
        private static final DefaultRefuseThreadPoolHandler NEW_INSTANCE = new DefaultRefuseThreadPoolHandler();
    }
}
