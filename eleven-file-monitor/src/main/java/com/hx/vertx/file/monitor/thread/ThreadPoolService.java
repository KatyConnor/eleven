package com.hx.vertx.file.monitor.thread;

import com.github.f4b6a3.ulid.UlidCreator;
import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import com.hx.vertx.file.monitor.fiber.FiberPoolExecutor;
import com.hx.vertx.file.monitor.queue.FiberTaskQueue;
import com.hx.vertx.file.monitor.task.FiberCallExecuteTask;
import com.hx.vertx.file.monitor.task.FiberExecuteTask;
import com.hx.vertx.file.monitor.thread.factory.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 将线程交给ThreadPoolExecutor管理
 *
 * @Author wml
 * @Date 2017-12-29 11:25
 */
public class ThreadPoolService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolService.class);

  private static ReentrantLock lock = new ReentrantLock();
  private static ThreadPoolService threadPoolService;

  /**
   * 将任务放入协程队列
   *
   * @param task
   * @param <T>
   * @return
   */
  public <T extends FiberExecuteTask> ThreadPoolService addQueue(T task) {
    FiberTaskQueue.build().add(task);
    return this;
  }

  /**
   * 将任务放入协程队列,协程处理之后需要返回处理结果
   *
   * @param task
   * @param <T>
   * @return
   */
  public <T extends FiberCallExecuteTask> ThreadPoolService addCallQueue(T task) {
    FiberTaskQueue.build().addCall(task);
    return this;
  }

  /**
   * 触发线程池处理
   * threadGroupName 线程池中调度线程组名称
   */
  public void execute(String threadGroupName) {
    if (threadGroupName == null) {
      LOGGER.info("未指定处理线程组，threadGroupName={} ，将使用默认线程池组分配处理！", threadGroupName);
    }
    ThreadPoolExecutor executor = ThreadPoolFactory.getThreadPool(threadGroupName);
    executor.execute(() -> {
      FiberExecuteTask task = null;
      try {
        task = FiberTaskQueue.build().poll();
        if (task != null) {
          ApplicationContextContainer.build().getBean(FiberPoolExecutor.class).execute(task);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        task = null; // GC回收
      }
    });
  }

  /**
   * 有返回值处理，将任务丢入协程处理队列之后，返回生成的对应任务ID
   *
   * @param threadGroupName 线程池中调度线程组名称
   * @return 返回任务ID主键
   */
  public String submit(String threadGroupName) {
    if (threadGroupName == null) {
      LOGGER.info("未指定处理线程组，threadGroupName={} ，将使用默认线程池组分配处理！", threadGroupName);
    }
    String taskId = UlidCreator.getUlid().toString();
    ThreadPoolExecutor executor = ThreadPoolFactory.getThreadPool(threadGroupName);
    executor.execute(() -> {
      FiberCallExecuteTask task = null;
      try {
        task = FiberTaskQueue.build().pollCallTask();
        if (task != null) {
          task.setTaskId(taskId);
          ApplicationContextContainer.build().getBean(FiberPoolExecutor.class).submit(task);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        task = null; // GC回收
      }
    });
    return taskId;
  }

  public static ThreadPoolService build() {
    threadPoolService = Single.NEWSTANCE;
    return threadPoolService;
  }

  private static final class Single {
    private static final ThreadPoolService NEWSTANCE = new ThreadPoolService();
  }
}
