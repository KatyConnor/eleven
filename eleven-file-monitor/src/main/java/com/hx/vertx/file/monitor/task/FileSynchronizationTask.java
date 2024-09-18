package com.hx.vertx.file.monitor.task;

import co.paralleluniverse.fibers.SuspendExecution;
import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import com.hx.vertx.file.monitor.enums.TransmissionModeEnums;
import com.hx.vertx.file.monitor.factory.ExecuteDecisionFactory;
import com.hx.vertx.file.monitor.factory.ex.FtpSyncFactory;
import com.hx.vertx.file.monitor.factory.ex.HttpSyncFactory;
import com.hx.vertx.file.monitor.factory.ex.RsyncFactory;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.properties.FileMonitorProperties;

/**
 * 文件同步任务实现类
 *
 * @author wml
 * @Discription
 * @Date 2023-03-08
 */
public class FileSynchronizationTask extends FiberExecuteTask {

  private String shell;
  private TransmissionModeEnums transmissionMode;

  public FileSynchronizationTask() {
    FileMonitorProperties fileMonitorProperties = ApplicationContextContainer.build().getProperties().getProperties(FileMonitorProperties.class);
    this.shell = fileMonitorProperties.getShell();
    this.transmissionMode = TransmissionModeEnums.getByCode(fileMonitorProperties.getTransmissionMode());
  }

  @Override
  public void run() throws SuspendExecution, InterruptedException {
    switch (this.transmissionMode) {
      case RSYNC:
        // 采用命脚本令进行同步
        ExecuteDecisionFactory.strategy(new RsyncFactory(this.shell));
        break;
      case FTP:
        // 采用FTP进行同步
        ExecuteDecisionFactory.strategy(new FtpSyncFactory());
        break;
      case HTTP:
        // 采用HTTP进行同步
        ExecuteDecisionFactory.strategy(new HttpSyncFactory());
        break;
      default:
        HXLogger.build(this).warn("暂不支持的文件传输类型");
    }
  }
}
