package com.hx.nine.eleven.thread.pool.executor.factory;

import com.hx.nine.eleven.thread.pool.executor.fiber.FiberPoolExecutor;
import com.hx.nine.eleven.thread.pool.executor.pool.ThreadPoolManageEntity;
import com.hx.nine.eleven.thread.pool.executor.pool.ThreadPoolManageExecutor;
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
     * @param threadPoolName
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(String threadPoolName){
        return threadPoolManageExecutor.getThreadPoolExecutor(threadPoolName);
    }

    /**
     * 获取协程执行器
     * @param threadPoolName
     * @return
     */
    public static FiberPoolExecutor getFiberPool(String threadPoolName){
        return threadPoolManageExecutor.getFiberPoolExecutor(threadPoolName);
    }

    /**
     * 获取协程执行器
     * @return
     */
    public static void addFiberPool(FiberPoolExecutor fiberPoolExecutor){
        threadPoolManageExecutor.addFiberPoolExecutor(fiberPoolExecutor);
    }

    /**
     * 添加一个新的线程池
     * @param entity
     */
    public static void addThreadPool(ThreadPoolManageEntity entity){
        threadPoolManageExecutor.addThreadPoolExcutor(entity);
    }
}
