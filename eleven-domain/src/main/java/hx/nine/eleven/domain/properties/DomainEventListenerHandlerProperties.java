package hx.nine.eleven.domain.properties;

import hx.nine.eleven.domain.conver.FieldStringToArraysDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

@ConfigurationPropertiesBind(prefix = "eleven.boot.domain.watcher")
public class DomainEventListenerHandlerProperties {

	/**
	 * 是否开启线程池处理
	 */
	private Boolean enabled = true;

	/**
	 * 处理队列所使用线程池
	 */
	private String threadGroupName;

	/**
	 * 工作队列最大处理数量
	 */
	private int maxWorkQueueSize;

	/**
	 * 是否开启自动切换子领域交易执行或者mapperFactory执行方法自动切换
	 */
	private Boolean enableDomainSupport = true;

	/**
	 * 是否自动同步文件
	 */
	private Boolean autoSync = false;

	/**
	 * 文件同步应用集群IP地址配置
	 */
	@JsonDeserialize(using = FieldStringToArraysDeserializer.class)
	private String[] applicationCluster;
	/**
	 * 文件同步应用集群访问端口
	 */
	private String syncPort;

	/**
	 * 应用访问path
	 */
	private String urlPath;

	/**
	 * 访问网络协议
	 */
	private String protocol = "http://";

	/**
	 * 默认开启fiber
	 */
	private Boolean openFiber = true;

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getThreadGroupName() {
		return threadGroupName;
	}

	public void setThreadGroupName(String threadGroupName) {
		this.threadGroupName = threadGroupName;
	}

	public int getMaxWorkQueueSize() {
		return maxWorkQueueSize;
	}

	public void setMaxWorkQueueSize(int maxWorkQueueSize) {
		this.maxWorkQueueSize = maxWorkQueueSize;
	}

	public Boolean getEnableDomainSupport() {
		return enableDomainSupport;
	}

	public void setEnableDomainSupport(Boolean enableDomainSupport) {
		this.enableDomainSupport = enableDomainSupport;
	}

	public Boolean getAutoSync() {
		return autoSync;
	}

	public void setAutoSync(Boolean autoSync) {
		this.autoSync = autoSync;
	}

	public String[] getApplicationCluster() {
		return applicationCluster;
	}

	public void setApplicationCluster(String[] applicationCluster) {
		this.applicationCluster = applicationCluster;
	}

	public String getSyncPort() {
		return syncPort;
	}

	public void setSyncPort(String syncPort) {
		this.syncPort = syncPort;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public Boolean getOpenFiber() {
		return openFiber;
	}

	public void setOpenFiber(Boolean openFiber) {
		this.openFiber = openFiber;
	}
}
