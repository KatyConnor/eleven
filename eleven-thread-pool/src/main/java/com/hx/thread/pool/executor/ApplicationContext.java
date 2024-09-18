package com.hx.thread.pool.executor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.FutureTask;

public class ApplicationContext {

    private final static ConcurrentLinkedQueue<FutureTask> processRsultQueue= new ConcurrentLinkedQueue();

    public static ApplicationContext build(){
        return BeanNewFactory.NEW_INSTANCE;
    }

    /**
     * 将放入线程池待处理任务的FutureTask 存入缓存
     * @param futureTask
     */
    public void addFutureTask(FutureTask futureTask){
        processRsultQueue.add(futureTask);
    }

    /**
     * 删除已经获取的数据
     * @param futureTask
     * @return
     */
    public boolean removeFutureTask(FutureTask futureTask){
        return processRsultQueue.remove(futureTask);
    }

    /**
     * 获取已处理完
     * @return
     */
    public FutureTask getIsDoneFutureTask(){
        FutureTask futureTask = null;
        int size = processRsultQueue.size();
        while (futureTask == null && size > 0){
            futureTask = processRsultQueue.stream().filter(f-> f.isDone()).findFirst().orElse(null);
        }
        return futureTask;
    }

    public final static class BeanNewFactory {
        public final static ApplicationContext NEW_INSTANCE = new ApplicationContext();
    }
}
