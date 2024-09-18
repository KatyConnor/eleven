package com.hx.vertx.file.monitor.thread;

import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.log.HXLogFileAppender;
import com.hx.vertx.file.monitor.utils.DateUtil;

import java.util.Date;

/**
 * 日志监听线程
 * @author wml
 * @date 2023-03-09
 */
public class LogFileMonitorThread extends Thread{

  private  String logPath;

  @Override
  public void run() {
    Date nowDate = DateUtil.parseDate(DateUtil.formatDate(new Date()));
    HXLogFileAppender.initLogPath(logPath+"/log");
    HXLogFileAppender.makeLogFileName(DateUtil.parseDate(DateUtil.formatDate(new Date())),1);
    while (true) {
      try {
        Date nextDate = DateUtil.parseDate(DateUtil.formatDate(new Date()));
        if (nowDate.before(nextDate)){
          nowDate = nextDate;
          HXLogFileAppender.initLogPath(logPath+"/log");
          HXLogFileAppender.makeLogFileName(nextDate,1);
        }
        // 判断时差
        long sub = DateUtil.calNowTimeDiff();
        if (sub / 1000 > 1){
          Thread.sleep((sub / 1000) * 1000);
        }
      } catch (InterruptedException e) {
        HXLogger.build(this).error("{}",e);
        Thread.currentThread().interrupt();
      }
    }
  }

  public String getLogPath() {
    return logPath;
  }

  public void setLogPath(String logPath) {
    this.logPath = logPath;
  }
}
