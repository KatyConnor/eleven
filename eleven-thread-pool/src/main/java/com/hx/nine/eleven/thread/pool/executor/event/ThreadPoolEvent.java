package com.hx.nine.eleven.thread.pool.executor.event;


import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.fibers.Suspendable;
import com.hx.nine.eleven.thread.pool.executor.exception.ThreadExecuteException;

/**
 * 线程事件
 * @Author wml
 * @Date 2017-12-29 11:27
 */
public abstract class ThreadPoolEvent<T, R> {

    /** 使用的线程池组名称 */
    private String threadGroupName;

    private T entity;

    private boolean doing = false;

    public ThreadPoolEvent(T entity){
        if (null != entity){
            this.entity = entity;
        }
    }

    public abstract void executeEvent();

    public abstract R executeCallEvent();

    @Suspendable
    public abstract R run() throws SuspendExecution, InterruptedException;

    public T getEntity() {
        return entity;
    }

    public <E extends ThreadPoolEvent<T,R>> E setT(T entity) {
        this.entity = entity;
        return (E) this;
    }

    public String getThreadGroupName() {
        return threadGroupName;
    }

    public <E extends ThreadPoolEvent<T,R>> E setThreadGroupName(String threadGroupName) {
        this.threadGroupName = threadGroupName;
        return (E) this;
    }

    public void doing(){
        if (this.doing){
            throw new ThreadExecuteException(Thread.currentThread().getName()+"线程已经运行，不可重复发起执行");
        }
        this.doing = true;
    }
}
