package com.hx.vertx.file.monitor.context;

import com.hx.vertx.file.monitor.db.H2DBServer;
import com.hx.vertx.file.monitor.jnotify.FileJnotifyListener;
import com.hx.vertx.file.monitor.file.FileMonitor;
import com.hx.vertx.file.monitor.WebApplicationVerticle;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.properties.FileMonitorProperties;
import com.hx.vertx.file.monitor.thread.ThreadPoolBuildFactory;
import com.hx.vertx.file.monitor.utils.StringUtils;
import com.hx.vertx.file.monitor.utils.YamlReadUtils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.h2.tools.Server;

import java.sql.SQLException;

/**
 * 应用启动类
 * @author wml
 * @date 2023-03-06
 */
public class HXWebRunApplication {

  /**
   * 启动服务
   * @param mainClass
   */
  public static void run(Class mainClass) {
    String scanPackage = mainClass.getName().substring(0, mainClass.getName().lastIndexOf("."));
    // 加载配置文件
    YamlReadUtils.build().readYamlConfiguration();
    // 初始化bean容器
    ApplicationBeanInitializer.initApplication(scanPackage);
    // 初始化日志，启动线程执行日志文件监控
//    HXLogInitialize.initLog();
    // 初始化H2数据库
    H2DBServer.initH2Db();
    try {
      Server.main("-web","-webPort","20187","-browser");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // 初始化线程池和协程
    ThreadPoolBuildFactory.build().initThreadPoolService();
    FileMonitorProperties fileMonitorProperties = ApplicationContextContainer.build().getProperties().getProperties(FileMonitorProperties.class);
    if (StringUtils.isEmpty(fileMonitorProperties.getMonitorDir())) {
      HXLogger.build(HXWebRunApplication.class).error("被监控文件目录为空，请检查配置");
      System.exit(0);  // 关闭应用
    }
    //
//    ThreadFactory.build().create("fileSyncFailedStorageThread",
//        FileSyncFailedStorageThread.class,
//        true)
//      .start();
    // 启动文件目录监听线程
    try {
      FileMonitor.build().setMonitorDir(fileMonitorProperties.getMonitorDir())
        .setListener(new FileJnotifyListener())
        .startWatch();
    } catch (Exception e) {
      HXLogger.build(HXWebRunApplication.class).error("{}",e);
      System.exit(0);
    }

    // 启动HTTP服务
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    VertxOptions options = new VertxOptions();
    options.setWorkerPoolSize(5);
    deploymentOptions.setWorkerPoolSize(5);
    Vertx.vertx(options).deployVerticle(WebApplicationVerticle.class, deploymentOptions);
    HXLogger.build(HXWebRunApplication.class).info("启动服务成功");
  }
}
