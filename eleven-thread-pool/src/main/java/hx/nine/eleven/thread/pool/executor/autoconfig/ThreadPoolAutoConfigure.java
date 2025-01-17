package hx.nine.eleven.thread.pool.executor.autoconfig;

import hx.nine.eleven.commons.utils.CollectionUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.thread.pool.executor.ThreadPoolProperties;
import hx.nine.eleven.thread.pool.executor.enums.TimeUnitEnums;
import hx.nine.eleven.thread.pool.executor.factory.ThreadPoolFactory;
import hx.nine.eleven.thread.pool.executor.pool.ThreadPoolManageEntity;
import hx.nine.eleven.core.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 线程池启动自动装配
 *
 * @Author wml
 * @Date 2018-01-11 17:11
 */
@Component(init = "initThreadPoolService")
public class ThreadPoolAutoConfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolAutoConfigure.class);

    public ThreadPoolAutoConfigure() {
        initThreadPoolService();
    }

    public void initThreadPoolService() {
        LOGGER.info("初始化线程池");
        List<ThreadPoolProperties> list = DefaultElevenApplicationContext.build().getProperties().getProperties(ThreadPoolProperties.class);
        if (!CollectionUtils.isNotEmpty(list)){
            LOGGER.warn("没有配置线程池相关参数，跳过初始化线程池，请检查，否则无法正常使用线程池功能!");
        }
        for (int i = 0; i < list.size(); ++i) {
            ThreadPoolProperties properties = list.get(i);
            if (StringUtils.isBlank(properties.getUnit())) {
                LOGGER.warn("thread Unit param is null, set defual SECONDS。");
            }
            ThreadPoolManageEntity entity = new ThreadPoolManageEntity(properties.getCorePoolSize(), properties.getMaximumPoolSize(),
                    properties.getKeepAliveTime(), StringUtils.isBlank(properties.getUnit()) ? TimeUnit.SECONDS : TimeUnitEnums.getByCode(properties.getUnit()).getTimeUnit())
                    .setThreadGroupName(StringUtils.isNotBlank(properties.getThreadGroupName()) ? properties.getThreadGroupName() : ("hx-thread-pool-" + i))
                    .setThreadNamePrefix(StringUtils.isNotBlank(properties.getThreadNamePrefix()) ? properties.getThreadNamePrefix() : "hx-");
            ThreadPoolFactory.addThreadPool(entity);
            LOGGER.info("初始化线程池详细信息： [{}]", entity.getThreadGroupName() + entity.getCorePoolSize() + entity.getMaximumPoolSize() + entity.getKeepAliveTime() + entity.getWorkQueueNum());//JSONObjectMapper.build().toJsonString(entity)
        }
    }
}
