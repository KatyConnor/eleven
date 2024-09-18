package com.hx.vertx.file.monitor.fiber;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.FiberExecutorScheduler;
import co.paralleluniverse.fibers.FiberScheduler;
import co.paralleluniverse.strands.SuspendableRunnable;
import com.hx.vertx.file.monitor.annotations.Component;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.queue.FiberWorkTaskQueue;
import com.hx.vertx.file.monitor.task.FiberCallExecuteTask;
import com.hx.vertx.file.monitor.thread.factory.ThreadPoolFactory;

import java.util.concurrent.*;

/**
 * 协程初始化
 *
 * @author wml
 * @date 2023-03-07
 */
@Component
public class FiberPoolExecutor {
  //默认协程池中默认协程的个数为5
  private final static int DEFAULT_MAX_FIBER_NUM = 50000;
  //默认队列默认任务为50000000
  private final static int TASK_NUM = 5000000;
  //用户在构造这个协程池时，但愿启动的协程数
  private int coreFibers;
  //任务调度线程池
  private FiberScheduler scheduler;
  //线程池组名称
  private String threadGroupName;

  /**
   * 初始化协程
   * @param threadGroupName
   * @return
   */
  public FiberPoolExecutor init(String threadGroupName) {
    this.threadGroupName = threadGroupName;
    this.scheduler = defaultFiberExecutorScheduler(threadGroupName);
    return this;
  }

  /**
   * 执行任务，其实就是把任务加入任务队列，何时执行由协程池管理器决定
   * @TODO 需要根据当前协程队列判断是否超过最大值，如果超过需要等待消费
   * @param task
   */
  public void execute(SuspendableRunnable task) {
    try {
      createFiber(task);
    } catch (Exception e) {
      HXLogger.build(this).info("添加任务失败，{}", e);
    }
  }

  /**
   * 执行任务，其实就是把任务加入任务队列，何时执行由协程池管理器决定
   * @TODO 需要根据当前协程队列判断是否超过最大值，如果超过需要等待消费
   * @param task
   */
  public <T extends FiberCallExecuteTask> void submit(T task) {
    try {
      createCallFiber(task);
    } catch (Exception e) {
      HXLogger.build(this).info("添加任务失败，{}", e);
    }
  }

  /**
   * 创建一个协程，然后获取执行任务
   * 如果将任务放入queue队列中，协程在队列中获取任务时存在获取同一个任务可能性，需要考虑协程安全控制
   */
  private void createFiber(SuspendableRunnable task) {
    if (task == null) {
      HXLogger.build(this).info("任务对象为[null]");
      return;
    }
    Fiber fiber = new Fiber<>(this.scheduler, () -> {
      try {
          task.run();
      } catch (Exception e) {
        HXLogger.build(this).info("任务获取失败，{}", e);
      }
    });
    fiber.start();
  }

  /**
   * 有返回结果处理
   *
   * @param task
   * @param <T>
   * @return
   */
  private <T extends FiberCallExecuteTask> void createCallFiber(T task) {
    if (task == null) {
      HXLogger.build(this).info("任务对象为[null]");
      return;
    }
    final Fiber<T> fiber = new Fiber(this.scheduler, () -> {
      try {
          return task.run();
      } catch (Exception e) {
        HXLogger.build(this).info("任务获取失败，{}", e);
      }
      return "处理异常";
    });
    fiber.start();
    // 处理完结果放入结果队列进行维护
    try {
      T result = fiber.get();
      FiberWorkTaskQueue.build().put(task.getTaskId(), result);
    } catch (Exception e) {
      HXLogger.build(this).info("任务处理结果获取异常，{}", e);
      FiberWorkTaskQueue.build().put(task.getTaskId(), "获取结果失败");
    }
  }

  public int getCoreFibers() {
    return coreFibers;
  }

  public FiberPoolExecutor setCoreFibers(int coreFibers) {
    this.coreFibers = coreFibers > DEFAULT_MAX_FIBER_NUM ? coreFibers : DEFAULT_MAX_FIBER_NUM;
    return this;
  }

  public FiberScheduler getScheduler() {
    return scheduler;
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
}
