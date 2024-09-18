package com.hx.etx.sync.fiber.thread;

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
     * 线程处理策略
     * 立即执行
     *
     * @param r
     * @param executor
     */
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            if (!executor.isShutdown()){
                r.run();
                LOGGER.info("立即执行当前线程！threadName = {},threadId = {}", Thread.currentThread().getName(), Thread.currentThread().getId());
            }else {
                LOGGER.info("当前线程不做任何处理，放弃当前线程任务！threadName = {},threadId = {}", Thread.currentThread().getName(), Thread.currentThread().getId());
            }
        } catch (Exception e) {
            LOGGER.info("当前线程执行异常！exception = {}", e);
        }
    }

    private static final class BeanNewInstanceFactory{
        private static final DefaultRefuseThreadPoolHandler NEW_INSTANCE = new DefaultRefuseThreadPoolHandler();
    }
}
