package hx.nine.eleven.vertx.web;

import hx.nine.eleven.core.WebApplicationMain;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.vertx.properties.VertxApplicationProperties;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 应用启动类
 * @author wml
 * @date 2023-03-06
 */
public class VertxWebApplicationRun extends WebApplicationMain {

  private final static Logger LOGGER = LoggerFactory.getLogger(VertxWebApplicationRun.class);

  /**
   * 启动服务
   * @param strings
   */
  @Override
  public void start(String[] strings) {
    LOGGER.info("启动 vertx 服务");
    VertxApplicationProperties properties = ElevenApplicationContextAware.getProperties(VertxApplicationProperties.class);
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorkerPoolSize(properties.getWorkerPoolSize());  // 默认线程
    deploymentOptions.setWorker(true);
    deploymentOptions.setMaxWorkerExecuteTime(properties.getMaxWorkerExecuteTime());
    deploymentOptions.setMaxWorkerExecuteTimeUnit(properties.getMaxWorkerExecuteTimeUnit());
    Vertx.vertx().deployVerticle(VertxWebApplicationVerticle.class, deploymentOptions);
    LOGGER.info("启动 vertx 服务成功");
  }

  @Override
  public void stop(String[] strings) {

  }

  @Override
  public void restart(String[] strings) {

  }
}
