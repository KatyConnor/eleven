package com.hx.vertx.file.monitor.log;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import com.hx.vertx.file.monitor.properties.ApplicationConfigProperties;
import com.hx.vertx.file.monitor.properties.FileMonitorProperties;
import com.hx.vertx.file.monitor.thread.LogFileMonitorThread;
import com.hx.vertx.file.monitor.thread.ThreadFactory;

/**
 * 日志线程初始化，启动配置加载
 *
 * @author wml
 * @date 2023-03-08
 */
public class HXLogInitialize {

  /**
   * 初始化日志线程
   */
  public static void initLog() {
    ApplicationConfigProperties configProperties = ApplicationContextContainer.build().getBean(ApplicationConfigProperties.class);
    FileMonitorProperties fileMonitorProperties = configProperties.getProperties(FileMonitorProperties.class);
    String logPath = fileMonitorProperties == null?String.valueOf(configProperties.get("file.logPath")):fileMonitorProperties.getLogPath();
    LogFileMonitorThread logThread = ThreadFactory.build().create("log_monitor", LogFileMonitorThread.class, true);
    logThread.setLogPath(logPath);
    logThread.start();
  }
}
