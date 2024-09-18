package com.hx.vertx.file.monitor.thread;

import com.esotericsoftware.reflectasm.ConstructorAccess;

/**
 * 线程创建
 * @author wml
 * @Description
 * @data 2022-07-26
 */
public class ThreadFactory {

    private static int threadInitNumber;

    public static ThreadFactory build(){
        return Single.NEWSTANCE;
    }

    public Thread create(Runnable runnable){
        return new Thread(runnable,"file-monitor-thread-"+nextThreadNum());
    }

    public Thread create(String threadName,Runnable runnable){
        return new Thread(runnable,threadName);
    }

    public <T extends Thread> T create(String threadName,Class<T> threadClass,Boolean daemon){
        ConstructorAccess<T> constructorAccess = ConstructorAccess.get(threadClass);
        T thread = constructorAccess.newInstance();
        thread.setName(threadName);
        if (daemon != null){
            thread.setDaemon(daemon);
        }
        return thread;
    }

    private static synchronized int nextThreadNum() {
        return threadInitNumber++;
    }

    private static final class Single{
        private static final ThreadFactory NEWSTANCE = new ThreadFactory();
    }
}
