package com.hx.thread.pool.executor.pool;

import com.hx.thread.pool.executor.exception.ThreadExecuteException;
import com.hx.thread.pool.executor.factory.AbstractThreadFactory;
import com.hx.thread.pool.executor.StringUtils;
import com.hx.thread.pool.executor.factory.DefaultThreadFactory;
import com.hx.thread.pool.executor.fiber.FiberPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池的配置管理
 * @Author mingliang
 * @Date 2017-12-29
 */
public class ThreadPoolManageExecutor{

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolManageExecutor.class);

    private static volatile Map<String,ThreadPoolExecutor> threadPoolMap = new ConcurrentHashMap<>();
    private static volatile Map<String, FiberPoolExecutor> fiberPoolMap = new ConcurrentHashMap<>();
    private static ThreadPoolManageExecutor threadPoolManageExecutor;

    /**
     * 获取线程池,根据传入的线程组进行获取，如果获取到对应的线程组则直接返回改组，如果没获取到，返回线程组活跃度最低的。
     *
     * @param threadGroupName
     * @return
     */
    public ThreadPoolExecutor getThreadPoolExecutor(String threadGroupName) {
        if (StringUtils.isNotBlank(threadGroupName)){
            ThreadPoolExecutor executor = threadPoolMap.get(threadGroupName);
            if (null != executor){
                LOGGER.info("获取线程池组，threadGroup：[{}]",threadGroupName);
                return executor;
            }
        }
        int count = 0;
        ThreadPoolExecutor poolExecutor = null;
        String groupName = null;
        for (Map.Entry entry : threadPoolMap.entrySet()){
            ThreadPoolExecutor value = (ThreadPoolExecutor)entry.getValue();
            int activeCount = value.getActiveCount();
            if (count == 0 || count > activeCount){
                count = activeCount;
                poolExecutor = value;
                groupName = entry.getKey().toString();
            }
        }
        LOGGER.info("没有设置线程池组，threadGroup：[{}]，获取当前空闲的线程组，default ThreadGroup: [{}],活跃线程数：activeCount: [{}]",
                threadGroupName,groupName,count);
        return poolExecutor;
    }

    /**
     * 根据指定的线程池组名称获取对应线程池组下面的协程处理器，如果美欧指定线程池组则获取当下空余线程较多的线程池及旗下的协程处理器
     * @param threadGroupName
     * @return
     */
   public FiberPoolExecutor getFiberPoolExecutor(String threadGroupName){
       FiberPoolExecutor fiberPoolExecutor = findFiberPoolExecutor(threadGroupName);
        if (fiberPoolExecutor != null){
            return fiberPoolExecutor;
        }

       int count = 0;
       String groupName = null;
       for (Map.Entry entry : threadPoolMap.entrySet()){
           ThreadPoolExecutor value = (ThreadPoolExecutor)entry.getValue();
           int activeCount = value.getActiveCount();
           if (count == 0 || count > activeCount){
               count = activeCount;
               groupName = entry.getKey().toString();
           }
       }
       LOGGER.info("没有设置线程池组，threadGroup：[{}]，获取当前空闲的线程组对应的协程处理器，default ThreadGroup: [{}],活跃线程数：activeCount: [{}]",
               threadGroupName,groupName,count);
       fiberPoolExecutor = findFiberPoolExecutor(groupName);
       return fiberPoolExecutor;
//       throw new ThreadExecuteException(com.hx.lang.commons.utils.StringUtils.format("没有查找到线程池[{}]对应的协程执行器",groupName));
    }

    public void addFiberPoolExecutor(FiberPoolExecutor fiberPoolExecutor){
        this.fiberPoolMap.put(fiberPoolExecutor.getThreadGroupName(),fiberPoolExecutor);
    }

    /**
     * 获取对象
     * @return
     */
    public static ThreadPoolManageExecutor getNewInstance(){
        threadPoolManageExecutor = ThreadPoolFactory.NEW_INSTANCE;
        return threadPoolManageExecutor;
    }

    /**
     * 添加一个新的线程
     * @param entity
     */
    public void addThreadPoolExcutor(ThreadPoolManageEntity entity){
        initThreadPool(entity);
    }


    /**
     * <p>
     *    初使化线程池，这是JDK5.0中自带的线程池，这里的参数依次代表：  
     *    核心线程数(最小活动线程数)  
     *    最大线程数及并发数【这个要注意，如果你的实际发大于该数，则有些请求这个时候虽然被接收，但是去得不到处理，这个数据一定得根据实际情况而设定，如我这里设值为20，实际模拟并发50，如循环一次，或者是二次并发，总会有20个不能够处理，如果设为25，就有15得不到处理，如果设为50则全部可以被处理，这个可以折磨了我好几天】  
     *    线程池维护线程所允许的空闲时间  
     *    线程池维护线程所允许的空闲时间的单位  
     *    线程池所使用的缓冲队列  
     *    线程池对拒绝任务的处理策略(通常是抛出异常)
     * </p>
     *
     * 初始化一个线程池，每一个线程池组对应自己的协程处理器
     * @param entity
     */
    private void initThreadPool(ThreadPoolManageEntity entity){
        synchronized (this){
            // 检查必传参数
            if (checkInitParams(entity)) {
                return;
            }
            // 线程处理工厂
            AbstractThreadFactory abstractThreadFactory= DefaultThreadFactory.build().setThreadGroup(entity.getThreadGroup()).setThreadNamePrefix(entity.getThreadNamePrefix());
            AbstractThreadFactory threadFactory = entity.getThreadFactory() == null ? abstractThreadFactory: entity.getThreadFactory();
            String threadGroupName = threadFactory.getThreadGroup().getName();
            LOGGER.info("初始化线程池！threadPoolGroup = {}", threadGroupName);
            if (this.threadPoolMap.containsKey(threadGroupName)) {
                LOGGER.info("线程池 threadPoolGroup={} 已经存在！", threadGroupName);
                return;
            }

            // 线程处理队列
            BlockingQueue<Runnable> workQueue = entity.getWorkQueue() == null ? new LinkedBlockingDeque<Runnable>(100) : entity.getWorkQueue();

            // 拒绝策略
            RejectedExecutionHandler handler = entity.getHandler() == null ? DefaultRefuseThreadPoolHandler.build() : entity.getHandler();

            ThreadPoolExecutor threadPool = getThreadPoolExecutor(entity.getCorePoolSize(), entity.getMaximumPoolSize(),
                    entity.getKeepAliveTime(), entity.getUnit(), workQueue, threadFactory, handler);
            this.threadPoolMap.put(threadFactory.getThreadGroup().getName(), threadPool);
            LOGGER.info("初始化协程池 threadPoolGroup={}！", threadGroupName);
        }
    }


    /**
     * 线程池初始化，全参数
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     * @param threadFactory
     * @param handler
     * @return
     */
    private ThreadPoolExecutor getThreadPoolExecutor(int corePoolSize,
                                                     int maximumPoolSize,
                                                     long keepAliveTime,
                                                     TimeUnit unit,
                                                     BlockingQueue<Runnable> workQueue,
                                                     ThreadFactory threadFactory,
                                                     RejectedExecutionHandler handler) {
        return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
                workQueue, threadFactory, handler);
    }

    private boolean checkInitParams(ThreadPoolManageEntity entity) {
        boolean isBlank = false;
        if (entity.getCorePoolSize() <= 0 || entity.getMaximumPoolSize() <= 0 || entity.getKeepAliveTime() <= 0L
                || entity.getUnit() == null) {
            isBlank = true;
            LOGGER.info("线程池初始化参数，corePoolSize=[{}],maximumPoolSize=[{}],keepAliveTime=[{}],unit=[{}],",
                    entity.getCorePoolSize(), entity.getMaximumPoolSize(), entity.getKeepAliveTime(), entity.getUnit().name());
        }

        return isBlank;
    }

    private FiberPoolExecutor findFiberPoolExecutor(String threadGroupName){
        if (StringUtils.isNotBlank(threadGroupName)){
            FiberPoolExecutor fiberPoolExecutor =  fiberPoolMap.get(threadGroupName);
            if (null != fiberPoolExecutor){
                LOGGER.info("获取线程池对应的协程处理器，threadGroup：[{}]",threadGroupName);
                return fiberPoolExecutor;
            }
        }
        return null;
    }

    private final static class ThreadPoolFactory{
        private static final ThreadPoolManageExecutor NEW_INSTANCE = new ThreadPoolManageExecutor();
    }
}
