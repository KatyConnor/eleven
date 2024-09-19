package com.hx.nine.eleven.domain.context.thread;

import com.hx.nine.eleven.domain.context.DomainContextEvent;
import com.hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import com.github.f4b6a3.ulid.UlidCreator;
//import com.hx.thread.pool.executor.fiber.task.FiberCallExecuteTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 领域服务，领域之间通讯调用
 *
 * @author wml
 * @date 2022-12-10
 */
//@Component(init = "start")
//@ConditionalOnClass(value = ThreadPoolService.class)
//@ConditionalOnProperty(prefix = "vertx.hx.domain.watcher", name = "enabled", havingValue = "true",
//		propertiesClass = DomainEventListenerHandlerProperties.class)
@Deprecated
public class DomainContextListenerHandlerMain {

	private final static Logger LOGGER = LoggerFactory.getLogger(DomainContextListenerHandlerMain.class);
	//待处理任务监听服务状态
//    private boolean pendingWatcherServerStatus = true;
	//处理成功任务监听服务状态
	private boolean successWatcherServerStatus = true;
	private final static AtomicInteger proccessCount = new AtomicInteger();
	// 使用线程池组
	private String threadGroupName;
	//处理成功任务监听守护线程
	private DomainEventSuccessListenerHandlerWatcher successWatcher;

	@Resource
	private DomainEventListenerHandlerProperties handlerProperties;
	@Resource
	private DomainContextEvent domainContextEvent;

	public void start() {
		LOGGER.info("启动扫描 DomainContext 上下文监听服务");
		this.threadGroupName = handlerProperties.getThreadGroupName();
		if (this.successWatcherServerStatus && this.successWatcher == null) {
			this.successWatcher = new DomainEventSuccessListenerHandlerWatcher();
			this.successWatcher.setName("process_success_watcher");
			this.successWatcher.setDaemon(true);
			this.successWatcher.start();
		}
	}

	/**
	 * 监听FutureTaskEntity处理完成的数据
	 */
	private class DomainEventSuccessListenerHandlerWatcher extends Thread {

		//@TODO 待考虑是否丢入到线程池处理，如果单线程处理性能高就保持现状
		@Override
		public void run() {
			//@TODO 添加日志链路跟踪唯一标识ID
			MDC.put("TRACE_ID", UlidCreator.getUlid().toString());
			try {
				LOGGER.info("[{}]启动领域上下文任务处理成功监听服务", this.getName());
				while (successWatcherServerStatus) {

				}
			} catch (Exception ex) {
				LOGGER.info("[{}]领域上下文任务处理成功监听服务已关闭。", this.getName());
			} finally {
				MDC.remove("TRACE_ID");
			}
		}
	}

    public void stopSuccessWatcher() {
        this.successWatcherServerStatus = false;
        this.successWatcher = null;
    }

    public void restartSuccessWatcher() {
        this.successWatcherServerStatus = true;
        if (this.successWatcher == null) {
            this.start();
        }
    }


}
