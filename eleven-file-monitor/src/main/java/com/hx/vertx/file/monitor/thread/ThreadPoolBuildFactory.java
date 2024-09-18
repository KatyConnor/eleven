package com.hx.vertx.file.monitor.thread;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import com.hx.vertx.file.monitor.enums.TimeUnitEnums;
import com.hx.vertx.file.monitor.fiber.FiberPoolExecutor;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.properties.ThreadPoolProperties;
import com.hx.vertx.file.monitor.thread.factory.ThreadPoolFactory;
import com.hx.vertx.file.monitor.thread.pool.ThreadPoolManageEntity;
import com.hx.vertx.file.monitor.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 线程池启动自动装配
 *
 * @Author wml
 * @Date 2018-01-11 17:11
 */
public class ThreadPoolBuildFactory {

  public static ThreadPoolBuildFactory build() {
    return new ThreadPoolBuildFactory();
  }

  public void initThreadPoolService() {
    ThreadPoolProperties threadPoolProperties = ApplicationContextContainer.build().getProperties().getProperties(ThreadPoolProperties.class);
    Optional.ofNullable(threadPoolProperties).ifPresent(properties -> {
      HXLogger.build(this).info("初始化线程池");
      if (StringUtils.isBlank(properties.getUnit())) {
        HXLogger.build(this).warn("thread Unit param is null, set defual SECONDS。");
      }
      ThreadPoolManageEntity entity = new ThreadPoolManageEntity(properties.getCorePoolSize(), properties.getMaximumPoolSize(),
        properties.getKeepAliveTime(), StringUtils.isBlank(properties.getUnit()) ? TimeUnit.SECONDS : TimeUnitEnums.getByCode(properties.getUnit()).getTimeUnit())
        .setThreadGroupName(StringUtils.isNotBlank(properties.getThreadGroupName()) ? properties.getThreadGroupName() : ("hx-thread-pool-" + new Random().nextInt(100)))
        .setThreadNamePrefix(StringUtils.isNotBlank(properties.getThreadNamePrefix()) ? properties.getThreadNamePrefix() : "zj-");
      ThreadPoolFactory.addThreadPool(entity);
      HXLogger.build(this).info("初始化线程池详细信息： [{}]", entity.getThreadGroupName() + entity.getCorePoolSize() + entity.getMaximumPoolSize() + entity.getKeepAliveTime() + entity.getWorkQueueNum());//JSONObjectMapper.build().toJsonString(entity)
    });
    ApplicationContextContainer.build().getBean(FiberPoolExecutor.class).init(threadPoolProperties.getThreadGroupName());
  }
}
