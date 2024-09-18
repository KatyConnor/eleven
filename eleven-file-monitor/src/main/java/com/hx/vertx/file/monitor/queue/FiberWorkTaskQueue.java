package com.hx.vertx.file.monitor.queue;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 协程队列处理情况，采用线程安全ConcurrentMap，主键ID唯一，不存在数据安全风险
 * @author wml
 * @date 2023-03-06
 * @param
 */
public class FiberWorkTaskQueue<T> {

    // 异步处理完成，存储处理结果
    private final ConcurrentMap<String,T> worktaskMap = new ConcurrentHashMap<>();

    private FiberWorkTaskQueue(){
    }
    public static FiberWorkTaskQueue build(){
        return Sigle.INSTANCE;
    }

    public void put(String key,T task){
        this.worktaskMap.put(key,task);
    }

    /**
     * 获取任务之后，从队列中删除当前任务
     * @return
     */
    public T get(String key){
      T result = worktaskMap.get(key);
      this.worktaskMap.remove(key);
      return result;
    }

  /**
   * 清楚队列所有数据
   * @return
   */
    public void remove(){
      worktaskMap.clear();
    }

  /**
   * 获取当前队列任务总数
   * @return
   */
  public int size(){
        return this.worktaskMap.size();
    }

    public final static class Sigle{
        public final static FiberWorkTaskQueue INSTANCE = new FiberWorkTaskQueue();
    }
}
