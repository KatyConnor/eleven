package com.hx.vertx.file.monitor.queue;

import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.task.FileSynchronizationTask;
import com.hx.vertx.file.monitor.thread.ThreadPoolService;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 文件监控处理队列，监控发生变化的文件放入此队列，等待文件同步线程从队列中获取并消费
 *
 * @author wangml
 * @date 2022-09-06
 */
public class FileMonitorQueue {

  private final static String MONITOR_FILE = "MONITOR_FILE";

  private FileMonitorQueue() {
    HashSet<String> monitorFile = new HashSet(128);
    monitorFileMap.put(MONITOR_FILE, monitorFile);
  }

  public static FileMonitorQueue build() {
    return NewinstanceFactory.NEW_INSTANCE;
  }

  private final ConcurrentHashMap<String, HashSet> monitorFileMap = new ConcurrentHashMap(1);
  public final BlockingQueue<String> queue = new LinkedBlockingQueue(); // 按照顺序队列取数据

  public void add(String fileName) {
    if (monitorFileMap.get(MONITOR_FILE).contains(fileName)) {
      return;
    }
    //监控到目录发生任何变化，直接标记队列文件发生变化
    monitorFileMap.get(MONITOR_FILE).add(fileName);
    queue.add(fileName);
    ThreadPoolService.build().addQueue(new FileSynchronizationTask()).execute("file-monitor-server-group");
  }

  public String getQueue() {
    String monitorFile = String.valueOf(queue.poll());
    if (monitorFile == null || monitorFile.isEmpty() || "null".equals(monitorFile)) {
      return null;
    }
    HXLogger.build(this).info(monitorFile + "发生变化，同步变更文件服务器");
    return monitorFile;
  }

  public boolean remove(String monitorFile) {
    return monitorFileMap.get(MONITOR_FILE).remove(monitorFile);
  }

  public void removeAll() {
    monitorFileMap.get(MONITOR_FILE).clear();
  }

  public static final class NewinstanceFactory {
    public static final FileMonitorQueue NEW_INSTANCE = new FileMonitorQueue();
  }
}
