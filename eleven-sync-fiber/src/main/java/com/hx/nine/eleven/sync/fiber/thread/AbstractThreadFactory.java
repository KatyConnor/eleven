package com.hx.nine.eleven.sync.fiber.thread;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractThreadFactory implements ThreadFactory {

    //线程池数量
    public static final AtomicInteger poolNumber = new AtomicInteger(1);
    //线程组
    public static final Map<String,AtomicInteger> threadGroupMap = new ConcurrentHashMap<>();

    private ThreadGroup threadGroup;
    private String threadNamePrefix;

    public AbstractThreadFactory(){
        SecurityManager securityManager = System.getSecurityManager();
        if (null == getThreadGroup()){
            setDefaultThreadGroup();
        }

        this.threadNamePrefix = "thread-";
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public AbstractThreadFactory setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
        return this;
    }

    public AbstractThreadFactory setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
        return this;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    private void setDefaultThreadGroup(){
        this.threadGroup = new ThreadGroup("hx-thread-pool-"+poolNumber.getAndIncrement());
        threadGroupMap.put(this.getThreadGroup().getName(),new AtomicInteger(1));
    }
}
