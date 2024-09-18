package com.hx.etx.sync.fiber.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池工厂
 * @Author wml
 * @Date 2018-12-11
 */
public class DefaultThreadFactory extends AbstractThreadFactory  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultThreadFactory.class);

    @Override
    public DefaultThreadFactory setThreadNamePrefix(String threadNamePrefix) {
        Optional.ofNullable(threadNamePrefix).ifPresent(thread -> super.setThreadNamePrefix(thread));
        return this;
    }

    @Override
    public DefaultThreadFactory setThreadGroup(ThreadGroup threadGroup) {
        Optional.ofNullable(threadGroup).ifPresent(group -> {
            this.threadGroupMap.remove(this.getThreadGroup().getName());
            int poolNUm = this.poolNumber.get();
            this.poolNumber.set(poolNUm - 1);
            super.setThreadGroup(group);
        });
        this.threadGroupMap.put(this.getThreadGroup().getName(), new AtomicInteger(1));
        return this;
    }

    public static DefaultThreadFactory build() {
        return new DefaultThreadFactory();
    }

    private DefaultThreadFactory() {
        super();
    }

    /**
     * @TODO 后续添加日志traceID 判断
     * @param r
     * @return
     */
    @Override
    public Thread newThread(Runnable r) {
        StringBuffer poolName = new StringBuffer(this.getThreadGroup().getName());
        poolName.append("-").append(this.getThreadNamePrefix()).append(this.threadGroupMap.get(this.getThreadGroup().getName()).getAndIncrement());
        Thread thread = new Thread(this.getThreadGroup(), ()->{MDCThreadUtil.wrap(r, MDC.getCopyOfContextMap());},  poolName.toString() , 0);
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

    /**
     * @param threadClass
     * @param <T>
     * @return
     */
    public <T extends Thread> T newThread(Class<T> threadClass) {
        StringBuffer poolName = new StringBuffer(this.getThreadGroup().getName());
        poolName.append("-").append(this.getThreadNamePrefix()).append(this.threadGroupMap.get(this.getThreadGroup().getName()).getAndIncrement());
        T thread = null;
        try {
            thread = threadClass.getDeclaredConstructor(ThreadGroup.class,Runnable.class,String.class,long.class)
                    .newInstance(this.getThreadGroup(),null, poolName.toString(),0);
        } catch (Exception e) {}
        if (thread == null){
            LOGGER.error("初始化线程失败[{}]",threadClass.getName());
            return null;
        }

        thread.setName(poolName.toString());
        if (thread.isDaemon()) {
            thread.setDaemon(false);
        }

        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }
}
