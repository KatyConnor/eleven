package hx.nine.eleven.domain.entity;

import hx.nine.eleven.core.entity.FileUploadEntity;

import java.util.List;

public class SyncFileUploadEntity {

	/**
	 * 上传文件列表信息
	 */
	private List<FileUploadEntity> fileUploadEntityList;

	/**
	 * 服务器集群
	 */
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

	public List<FileUploadEntity> getFileUploadEntityList() {
		return fileUploadEntityList;
	}

	public SyncFileUploadEntity setFileUploadEntityList(List<FileUploadEntity> fileUploadEntityList) {
		this.fileUploadEntityList = fileUploadEntityList;
		return this;
	}

	public String[] getApplicationCluster() {
		return applicationCluster;
	}

	public SyncFileUploadEntity setApplicationCluster(String[] applicationCluster) {
		this.applicationCluster = applicationCluster;
		return this;
	}

	public String getSyncPort() {
		return syncPort;
	}

	public void setSyncPort(String syncPort) {
		this.syncPort = syncPort;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public static SyncFileUploadEntity build(){
		return new SyncFileUploadEntity();
	}
}
