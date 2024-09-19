package com.hx.nine.eleven.thread.pool.executor.pool;

import co.paralleluniverse.concurrent.util.EnhancedAtomicReference;
import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.Suspendable;
import com.hx.nine.eleven.sync.fiber.HXFiberSync;
import com.hx.nine.eleven.thread.pool.executor.StringUtils;
import com.hx.nine.eleven.thread.pool.executor.factory.ThreadPoolFactory;
import com.hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;
import com.hx.nine.eleven.thread.pool.executor.fiber.FiberPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 将线程交给ThreadPoolExecutor管理
 *
 * @Author wml
 * @Date 2017-12-29 11:25
 */
public class ThreadPoolService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolService.class);
	private static final String DEFAULT_THREAD_GROUP_NAME = "DEFAULT_THREAD_GROUP_NAME";
	private static ReentrantLock lock = new ReentrantLock();
	private static ThreadPoolService threadPoolService;

	/**
	 * 执行线程，默认添加日志链路
	 *
	 * @param event
	 */
	public void execute(final ThreadPoolEvent event) {
		if (event == null) {
			LOGGER.info("发布事件 event={} 不能为空！", event);
			return;
		}

		ThreadPoolExecutor executor = ThreadPoolFactory.getThreadPool(event.getThreadGroupName());
		executor.execute(() -> {
			event.doing();
			event.executeEvent();
		});
	}

	/**
	 * 执行线程，默认由线程池调度协程进行处理,默认添加日志链路
	 *
	 * @param event
	 */
	@Suspendable
	public <P, R> R run(final ThreadPoolEvent<P, R> event) {
		if (event == null) {
			LOGGER.info("发布事件 event={} 不能为空！", event);
			return null;
		}
		ThreadPoolExecutor executor = ThreadPoolFactory.getThreadPool(event.getThreadGroupName());
		String threadGroupName = StringUtils.isNotBlank(event.getThreadGroupName()) ? event.getThreadGroupName() :
				DEFAULT_THREAD_GROUP_NAME;
		FiberPoolExecutor fiberPoolExecutor = ThreadPoolFactory.getFiberPool(threadGroupName);
		if (fiberPoolExecutor == null) {
			// 初始化fiber
			fiberPoolExecutor = new FiberPoolExecutor();
			fiberPoolExecutor.setThreadGroupName(threadGroupName);
			fiberPoolExecutor.setExecutor(executor).init();
			ThreadPoolFactory.addFiberPool(fiberPoolExecutor);
		}

		// 判断当前任务是否已经在协程中运行，如果是，则直接运行
		if (Fiber.currentFiber() != null) {
			try {
				return HXFiberSync.fiberExecuteCall(() -> {
					LOGGER.info("-----当前执行任务在[{}],fiber中执行，直接运行实现方法",Fiber.currentFiber().getName());
					return event.run();
				});
			} catch (Exception e) {
				LOGGER.error("异步执行异常",e);
				return null;
			}
		}

		final FiberPoolExecutor fiberExecutor = fiberPoolExecutor;
		AtomicReference<R> res = new EnhancedAtomicReference<>();
		executor.execute(() -> {
			event.doing();
			LOGGER.info("-----[{}]:协程中执行任务-----", threadGroupName);
			res.set(fiberExecutor.submit(event));
		});
		return res.get();
	}

	/**
	 * 异步处理
	 *
	 * @param event
	 * @return
	 */
	public FutureTask submit(final ThreadPoolEvent event) {
		if (event == null) {
			LOGGER.info("发布事件 event={} 不能为空！", event);
			return null;
		}
		ThreadPoolExecutor executor = ThreadPoolFactory.getThreadPool(event.getThreadGroupName());
		ThreadCallable threadCallable = new ThreadCallable(event);
		HXFutureTask futureTask = new HXFutureTask(threadCallable);
		executor.submit(futureTask);
		return futureTask;
	}

	public static ThreadPoolService build() {
		threadPoolService = Single.NEWSTANCE;
		return threadPoolService;
	}

	private static final class Single {
		private static final ThreadPoolService NEWSTANCE = new ThreadPoolService();
	}
}
