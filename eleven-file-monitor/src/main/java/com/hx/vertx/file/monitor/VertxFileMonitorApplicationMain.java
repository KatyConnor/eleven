package com.hx.vertx.file.monitor;


import com.hx.vertx.file.monitor.context.HXWebRunApplication;
import com.hx.vertx.file.monitor.log.HXLogger;

import java.nio.file.Paths;

public class VertxFileMonitorApplicationMain {

  // System.out.println(System.getProperty("java.library.path"));  查看Linux下java.library.path路径
  public static void main(String[] args) {
//    System.out.println(System.getProperty("user.dir"));
    long startTime = System.currentTimeMillis();
    HXWebRunApplication.run(VertxFileMonitorApplicationMain.class);
    HXLogger.build(VertxFileMonitorApplicationMain.class).info("启动耗时: {} 秒", (System.currentTimeMillis() - startTime) / 1000);
    System.out.println("启动耗时: " + (System.currentTimeMillis() - startTime) / 1000 + " 秒");
  }
}
