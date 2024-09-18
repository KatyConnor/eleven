package com.hx.vertx.file.monitor.queue;

import co.paralleluniverse.concurrent.util.SingleConsumerNonblockingProducerQueue;
import co.paralleluniverse.strands.queues.SingleConsumerLinkedObjectQueue;
import com.hx.vertx.file.monitor.task.FiberCallExecuteTask;
import com.hx.vertx.file.monitor.task.FiberExecuteTask;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 消费任务队列
 *
 * @param <T>
 */
public class FiberTaskQueue<T extends FiberExecuteTask,C extends FiberCallExecuteTask> {
  /**
   * 异步处理队列
   */
  private SingleConsumerLinkedObjectQueue queue = new SingleConsumerLinkedObjectQueue();
  private SingleConsumerNonblockingProducerQueue<T> taskQueue = new SingleConsumerNonblockingProducerQueue<>(queue);

  /**
   * 异步处理并返回处理结果队列
   */
  private SingleConsumerLinkedObjectQueue callQueue = new SingleConsumerLinkedObjectQueue();
  private SingleConsumerNonblockingProducerQueue<C> callTaskQueue = new SingleConsumerNonblockingProducerQueue<>(queue);

  private FiberTaskQueue() {

  }

  public static FiberTaskQueue build() {
    return Sigle.INSTANCE;
  }

  public void add(T task) {
    this.taskQueue.add(task);
  }
  public void addCall(C task) {
    this.callTaskQueue.add(task);
  }

  /**
   * 获取任务之后，从队列中删除当前任务
   *
   * @return
   */
  public T poll() {
    return this.taskQueue.poll();
  }

  public C pollCallTask() {
    return this.callTaskQueue.poll();
  }

  public boolean remove(T task) {
    return this.taskQueue.remove(task);
  }

  public boolean removeCallTask(T task) {
    return this.callTaskQueue.remove(task);
  }

  public int getTaskNum() {
    return this.taskQueue.size();
  }

  public int getCallTaskNum() {
    return this.callTaskQueue.size();
  }

  public int getTaskSize() {
    return this.taskQueue.size() + this.callTaskQueue.size();
  }

  public final static class Sigle {
    public final static FiberTaskQueue INSTANCE = new FiberTaskQueue();
  }
}
