package hx.nine.eleven.domain.threadevent;

import co.paralleluniverse.fibers.SuspendExecution;
import hx.nine.eleven.core.entity.FileUploadEntity;
import hx.nine.eleven.domain.context.FileUploadExecuteProcess;
import hx.nine.eleven.domain.entity.SyncFileUploadEntity;
import hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 上传文件异步处理
 */
public class FileUploadThreadPoolEvent extends ThreadPoolEvent<List<FileUploadEntity>,Object> {

	private final static Logger LOGGER = LoggerFactory.getLogger(FileUploadThreadPoolEvent.class);

	public FileUploadThreadPoolEvent(List<FileUploadEntity> entity) {
		super(entity);
	}

	@Override
	public void executeEvent() {

	}

	@Override
	public Object executeCallEvent() {
		return null;
	}

	@Override
	public Object run() throws SuspendExecution, InterruptedException {
		FileUploadExecuteProcess fileUploadExecuteProcess = ElevenApplicationContextAware.getSubTypesOfBean(FileUploadExecuteProcess.class);
		DomainEventListenerHandlerProperties handlerProperties = ElevenApplicationContextAware.getProperties(DomainEventListenerHandlerProperties.class);
		if (this.checkSyncFileConfiguration(handlerProperties)){
			throw new RuntimeException("文件同步服务集群配置缺失，请配置");
		}
		this.setThreadGroupName(handlerProperties.getThreadGroupName());
		SyncFileUploadEntity entity = SyncFileUploadEntity.build()
				.setFileUploadEntityList(this.getEntity())
				.setApplicationCluster(handlerProperties.getApplicationCluster());
		fileUploadExecuteProcess.uploadFile(entity);
		return null;
	}

	/**
	 * 如果开启了系统自动文件同步功能，检查时候配置对应参数值
	 * @param handlerProperties
	 * @return
	 */
	private boolean checkSyncFileConfiguration(DomainEventListenerHandlerProperties handlerProperties){
		boolean result = true;
		if (ObjectUtils.isEmpty(handlerProperties.getApplicationCluster())){
			result = false;
			LOGGER.warn("--------文件同步应用服务集群IP地址不能为空，请配置！--------");
		}
		if (ObjectUtils.isEmpty(handlerProperties.getSyncPort())){
			result = false;
			LOGGER.warn("--------文件同步应用集群访问端口不能空，请配置！--------");
		}
		if (ObjectUtils.isEmpty(handlerProperties.getProtocol())){
			result = false;
			LOGGER.warn("--------文件同步应用服务集群访问网络协议不能空，请配置！--------");
		}
		return result;
	}
}
