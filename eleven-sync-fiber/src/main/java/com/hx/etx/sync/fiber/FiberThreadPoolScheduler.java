package com.hx.etx.sync.fiber;

import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import com.hx.etx.sync.fiber.thread.AbstractThreadFactory;
import com.hx.etx.sync.fiber.thread.DefaultRefuseThreadPoolHandler;
import com.hx.etx.sync.fiber.thread.DefaultThreadFactory;
import com.hx.etx.sync.fiber.thread.ThreadPoolManageEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author wml
 */
public class FiberThreadPoolScheduler {

	private static final Logger LOGGER = LoggerFactory.getLogger(FiberThreadPoolScheduler.class);

	//任务调度线程池
	private FiberScheduler fiberScheduler;
	private ThreadPoolExecutor threadPoolExecutor;

	public FiberScheduler getFiberScheduler(){
		return this.fiberScheduler;
	}

	public ThreadPoolExecutor getThreadPoolExecutor() {
		return this.threadPoolExecutor;
	}

	public FiberThreadPoolScheduler(){
		init();
	}

	/**
	 * 创建协程调度默认线程池
	 */
	public void init(){
		int numCores = Runtime.getRuntime().availableProcessors();
		ThreadPoolManageEntity entity = new ThreadPoolManageEntity(numCores+1, numCores*2,
				60, TimeUnit.SECONDS)
				.setThreadGroupName(FiberConstant.FIBER_THREAD_POOL_NAME)
				.setThreadNamePrefix(FiberConstant.THREAD_NAME_PREFIX);
		initThreadPool(entity);
	}

	private void initThreadPool(ThreadPoolManageEntity entity) {
		synchronized (this) {
			// 检查必传参数 MDCThreadUtil.wrap();
			if (checkInitParams(entity)) {
				return;
			}
			// 线程处理工厂
			AbstractThreadFactory threadFactory = DefaultThreadFactory.build().setThreadGroup(entity.getThreadGroup()).setThreadNamePrefix(entity.getThreadNamePrefix());
			String threadGroupName = threadFactory.getThreadGroup().getName();
			LOGGER.info("初始化线程池！threadPoolGroup = {}", threadGroupName);
			// 线程处理队列
			BlockingQueue<Runnable> workQueue = entity.getWorkQueue() == null ? new LinkedBlockingDeque<Runnable>(100) : entity.getWorkQueue();
			// 拒绝策略
			RejectedExecutionHandler handler = entity.getHandler() == null ? DefaultRefuseThreadPoolHandler.build() : entity.getHandler();

			this.threadPoolExecutor = newThreadPoolExecutor(entity.getCorePoolSize(), entity.getMaximumPoolSize(),
					entity.getKeepAliveTime(), entity.getUnit(), workQueue, threadFactory, handler);
			this.fiberScheduler=defaultFiberExecutorScheduler(this.threadPoolExecutor);
			LOGGER.info("初始化协程池 threadPoolGroup={}！", threadGroupName);
		}
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
	private ThreadPoolExecutor newThreadPoolExecutor(int corePoolSize,
													 int maximumPoolSize,
													 long keepAliveTime,
													 TimeUnit unit,
													 BlockingQueue<Runnable> workQueue,
													 ThreadFactory threadFactory,
													 RejectedExecutionHandler handler) {
		return new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit,
				workQueue, threadFactory, handler);
	}

	/**
	 * 初始化FiberScheduler
	 *
	 * @return
	 */
	private FiberScheduler defaultFiberExecutorScheduler(ThreadPoolExecutor executor) {
		FiberExecutorScheduler scheduler = new FiberExecutorScheduler("FibersExecutorSchedulerPool", executor);
		LOGGER.info("----------------------------创建FiberExecutorScheduler成功--------------------------------");
		return scheduler;
	}

	public static FiberThreadPoolScheduler build(){
		return BeanFactory.INSTANCE;
	}

	private final static class BeanFactory{
		private static final FiberThreadPoolScheduler INSTANCE = new FiberThreadPoolScheduler();
	}

}
