package com.hx.vertx.file.monitor.thread.factory;

import com.hx.vertx.file.monitor.thread.pool.ThreadPoolManageEntity;
import com.hx.vertx.file.monitor.thread.pool.ThreadPoolManageExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池的工厂类
 * @Author wml
 * @Date 2017-12-29 20:45
 */
public class ThreadPoolFactory {

    private final static ThreadPoolManageExecutor threadPoolManageExecutor = ThreadPoolManageExecutor.getNewInstance();

    /**
     * 获取一个线程池
     * @param threadGroupName
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(String threadGroupName){
        return threadPoolManageExecutor.getThreadPoolExecutor(threadGroupName);
    }

    /**
     * 添加一个新的线程池
     * @param entity
     */
    public static void addThreadPool(ThreadPoolManageEntity entity){
        threadPoolManageExecutor.addThreadPoolExcutor(entity);
    }
}
