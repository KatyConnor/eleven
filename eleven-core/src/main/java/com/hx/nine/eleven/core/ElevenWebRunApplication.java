package com.hx.nine.eleven.core;

import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.core.constant.DefualtProperType;
import com.hx.nine.eleven.core.core.context.ClassPathBeanDefinitionScanner;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.env.ElevenYamlReadUtils;
import com.hx.nine.eleven.core.properties.ElevenBootApplicationProperties;
import io.vertx.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 应用启动类
 * @author wml
 * @date 2023-03-06
 */
public class ElevenWebRunApplication {

  private final static Logger LOGGER = LoggerFactory.getLogger(ElevenWebRunApplication.class);

  /**
   * 启动服务
   */
  public static void run(String[] args) {
    String properties = null;

    if (args != null && args.length > 0){
      for (String arg : args){
        LOGGER.info("入参：{}！", arg);
        if (arg.startsWith("-D"+DefualtProperType.CONFIG_PATH) || arg.startsWith(DefualtProperType.CONFIG_PATH)){
          properties = arg;
          break;
        }
      }
    }
    // 1、加载配置文件，初始化环境变量
    if (StringUtils.isNotEmpty(properties)){
      String[] prop = properties.split("=");
      if (prop[0].startsWith("-D")){
        prop[0] = prop[0].substring(2);
      }
      ElevenYamlReadUtils.build().addProperties(prop[0],prop[1]).readYamlConfiguration(prop[1]);
    }else {
      ElevenYamlReadUtils.build().readYamlConfiguration(null);
    }
    // 初始化bean容器，和上下文HXVertxApplicationContext
    ClassPathBeanDefinitionScanner.build().initClass();
    LOGGER.info("容器初始化完成！");
    ElevenBootApplicationProperties elevenBootApplicationProperties = DefaultElevenApplicationContext.build().getProperties(ElevenBootApplicationProperties.class);
    DeploymentOptions deploymentOptions = new DeploymentOptions();
    deploymentOptions.setWorkerPoolSize(elevenBootApplicationProperties.getWorkerPoolSize());  // 默认线程
    deploymentOptions.setWorker(true);
    deploymentOptions.setMaxWorkerExecuteTime(30);
    deploymentOptions.setMaxWorkerExecuteTimeUnit(TimeUnit.SECONDS);
    Vertx.vertx().deployVerticle(WebApplicationVerticle.class, deploymentOptions);
    LOGGER.info("启动服务成功");
  }
}
