package com.hx.vertx.file.monitor.queue;

import co.paralleluniverse.common.util.ConcurrentSet;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储同步失败的文件路径，每一分钟将数据持久化到文件中一次
 * @author wml
 * @Discription
 * @Date 2023-03-11
 */
public class FileSyncFailedQueue<T> {

  // 同步失败文件
  private final ConcurrentSet<String> worktaskMap = new ConcurrentSet<>(new ConcurrentHashMap<>());

  private FileSyncFailedQueue(){
  }

  public static FileSyncFailedQueue build(){
    return Sigle.INSTANCE;
  }

  public void add(String filePath){
    this.worktaskMap.add(filePath);
  }

  public Set<String> getAll(){
    Set<String> storageSet = new HashSet<>();
    worktaskMap.forEach(v->{
      storageSet.add(v);
    });
    return storageSet;
  }

  /**
   * 删除数据
   * @return
   */
  public boolean remove(String key){
    return worktaskMap.remove(key);
  }

  /**
   * 清楚队列所有数据
   * @return
   */
  public void clear(){
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
    public final static FileSyncFailedQueue INSTANCE = new FileSyncFailedQueue();
  }
}
