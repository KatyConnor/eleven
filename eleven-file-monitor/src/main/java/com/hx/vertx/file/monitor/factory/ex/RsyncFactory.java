package com.hx.vertx.file.monitor.factory.ex;

import com.hx.vertx.file.monitor.factory.StrategyFactory;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.queue.FileMonitorQueue;
import com.hx.vertx.file.monitor.shell.ShellRunTimeExec;

/**
 * rsync 脚本同步
 * @author wml
 * @Discription
 * @Date 2023-03-11
 */
public class RsyncFactory extends StrategyFactory {
  private String shell;

  public RsyncFactory(String shell) {
    this.shell = shell;
  }

  /**
   * 同步失败之后需要放入失败队列，等待失败任务重试上传，防止宕机数据丢失，需要将数据持久化到文件中
   */
  @Override
  public void runExe() {
    try {
      String monitorFile = FileMonitorQueue.build().getQueue();
      if (monitorFile == null || monitorFile.isEmpty() || "null".equals(monitorFile)) {
        Thread.sleep(1000L);
      }

      ShellRunTimeExec.runShell(this.shell);
      FileMonitorQueue.build().removeAll();
      HXLogger.build(this).info("同步文件完成");
    } catch (InterruptedException e) {
      HXLogger.build(this).error("{}", e);
    }
  }
}
