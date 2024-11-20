package hx.nine.eleven.thread.pool.executor.fiber;

import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.fibers.Suspendable;
import hx.nine.eleven.sync.fiber.HXFiberSync;
import hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;
import hx.nine.eleven.thread.pool.executor.factory.ThreadPoolFactory;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 协程初始化
 *
 * @author wml
 * @date 2023-03-07
 */
public class FiberPoolExecutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(FiberPoolExecutor.class);

	//任务调度线程池
	private FiberScheduler scheduler;
	//线程池组名称
	private String threadGroupName;
	// 线程池执行器
	private ThreadPoolExecutor executor;

	/**
	 * 初始化协程
	 *
	 * @param threadGroupName
	 * @return
	 */
	public FiberPoolExecutor init(String threadGroupName) {
		this.threadGroupName = threadGroupName;
		this.scheduler = defaultFiberExecutorScheduler(threadGroupName);
		return this;
	}

	/**
	 * 初始化协程
	 *
	 * @return
	 */
	public FiberPoolExecutor init() {
		this.scheduler = defaultFiberExecutorScheduler();
		return this;
	}

	/**
	 * 执行任务，其实就是把任务加入任务队列，何时执行由协程池管理器决定
	 *
	 * @param task
	 */
	@Suspendable
	public <T, R> R submit(ThreadPoolEvent<T, R> task) {
		return HXFiberSync.awaitResult(h -> {
			try {
				LOGGER.info("-----开始执行异步任务");
				R result = task.run();
				h.handle(Future.succeededFuture(result));
			} catch (Exception e) {
				h.handle(Future.failedFuture(e));
			}
		},this.scheduler);
	}

	public ThreadPoolExecutor getExecutor() {
		return executor;
	}

	public FiberPoolExecutor setExecutor(ThreadPoolExecutor executor) {
		this.executor = executor;
		return this;
	}

	public FiberScheduler getScheduler() {
		return scheduler;
	}

	public String getThreadGroupName() {
		return threadGroupName;
	}

	public void setThreadGroupName(String threadGroupName) {
		this.threadGroupName = threadGroupName;
	}

	/**
	 * 初始化FiberScheduler
	 *
	 * @return
	 */
	private FiberScheduler defaultFiberExecutorScheduler(String threadGroupName) {
		ThreadPoolExecutor executor = ThreadPoolFactory.getThreadPool(threadGroupName);
		FiberExecutorScheduler scheduler = new FiberExecutorScheduler("FibersExecutorSchedulerPool", executor);
		return scheduler;
	}

	private FiberScheduler defaultFiberExecutorScheduler() {
		FiberExecutorScheduler scheduler = new FiberExecutorScheduler("FibersExecutorSchedulerPool", this.executor);
		return scheduler;
	}
}
