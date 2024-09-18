package com.hx.thread.pool.executor.pool;


import com.hx.thread.pool.executor.factory.AbstractThreadFactory;
import com.hx.thread.pool.executor.StringUtils;

import java.util.concurrent.*;

/**
 * 线程池构造参数管理类
 * @Author mingliang
 * @Date 2019-02-12
 */
public class ThreadPoolManageEntity {

    //线程组名称
    private String threadGroupName;
    //线程名称前缀
    private String threadNamePrefix;
    // 核心线程数
    private int corePoolSize;
    // 最大线程数
    private int maximumPoolSize;
    // 线程维持存活时间
    private long keepAliveTime;
    // 执行时间单位
    private TimeUnit unit;
    // 线程队列数量
    private int workQueueNum = 50;
    // 线程处理队列
    private BlockingQueue<Runnable> workQueue;
    // 线程处理工厂
    private AbstractThreadFactory threadFactory;
    // 拒绝策略
    private RejectedExecutionHandler handler;
    // 线程组
    private ThreadGroup threadGroup;

    public ThreadPoolManageEntity() {
    }

    public ThreadPoolManageEntity(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                  TimeUnit unit) {
        check(corePoolSize,maximumPoolSize);
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime == 0?60:keepAliveTime;
        this.unit = unit == null?TimeUnit.SECONDS:unit;
        this.workQueue = workQueue == null?new LinkedBlockingDeque<Runnable>(this.workQueueNum):workQueue;
    }

    public ThreadPoolManageEntity(String threadNamePrefix,int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                  TimeUnit unit) {
        check(corePoolSize,maximumPoolSize);
        this.threadNamePrefix = threadNamePrefix;
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime == 0?60:keepAliveTime;
        this.unit = unit == null?TimeUnit.SECONDS:unit;
        this.workQueue = workQueue == null?new LinkedBlockingDeque<Runnable>(this.workQueueNum):workQueue;
    }

    public String getThreadGroupName() {
        return threadGroupName;
    }

    public ThreadPoolManageEntity setThreadGroupName(String threadGroupName) {
        this.threadGroupName = threadGroupName;
        if (StringUtils.isNotBlank(this.threadGroupName)){
            this.threadGroup = new ThreadGroup( this.threadGroupName);
        }
        return this;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public ThreadPoolManageEntity setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
        return this;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public ThreadPoolManageEntity setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public ThreadPoolManageEntity setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public ThreadPoolManageEntity setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
        return this;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public ThreadPoolManageEntity setUnit(TimeUnit unit) {
        this.unit = unit;
        return this;
    }

    public int getWorkQueueNum() {
        return workQueueNum;
    }

    public ThreadPoolManageEntity setWorkQueueNum(int workQueueNum) {
        this.workQueueNum = workQueueNum;
        return this;
    }

    public BlockingQueue<Runnable> getWorkQueue() {
        return workQueue;
    }

    public ThreadPoolManageEntity setWorkQueue(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    public AbstractThreadFactory getThreadFactory() {
        return threadFactory;
    }

    public ThreadPoolManageEntity setThreadFactory(AbstractThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        return this;
    }

    public RejectedExecutionHandler getHandler() {
        return handler;
    }

    public ThreadPoolManageEntity setHandler(RejectedExecutionHandler handler) {
        this.handler = handler;
        return this;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    public ThreadPoolManageEntity setThreadGroup(ThreadGroup threadGroup) {
        this.threadGroup = threadGroup;
        return this;
    }

    private void check(int corePoolSize, int maximumPoolSize){
        if (corePoolSize == 0 || maximumPoolSize == 0){
            throw new RuntimeException(String.format("corePoolSize=[%s],maximumPoolSize=[%s] 不能为空！", corePoolSize,maximumPoolSize));
        }
    }
}
