package com.hx.nine.eleven.vertx.properties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.hx.nine.eleven.commons.annotation.FieldTypeConvert;
import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import com.hx.nine.eleven.core.convert.FieldIntegerToLongConvert;
import com.hx.nine.eleven.core.convert.FieldStringToArraysDeserializer;
import com.hx.nine.eleven.vertx.convert.FieldStringToTimeUnitConvert;

import java.util.concurrent.TimeUnit;

/**
 * @auth wml
 * @date 2024/11/5
 */
@ConfigurationPropertiesBind(prefix = "eleven.boot.vertx")
public class VertxApplicationProperties {

	/**
	 * 工作线程池大小
	 */
	private Integer workerPoolSize;

	/**
	 * 执行等待最大时间，单位默认是秒，默认最大执行等待时间是20秒
	 */
	@FieldTypeConvert(using = FieldIntegerToLongConvert.class)
	private Long maxWorkerExecuteTime = 20L;

	/**
	 * 执行时间单位
	 */
	@FieldTypeConvert(using = FieldStringToTimeUnitConvert.class)
	private String maxWorkerExecuteTimeUnit = TimeUnit.SECONDS.toString();

	/**
	 * body传输内容大小
	 */
	@FieldTypeConvert(using = FieldIntegerToLongConvert.class)
	private Long bodyLimit;

	/**
	 * 文件是否下载
	 */
	private Boolean handleFileUploads = true;

	/**
	 *
	 */
	private Boolean preallocateBodyBuffer;

	/**
	 * 文件上传本地目录
	 */
	private String uploadsDirectory;

	/**
	 * 合并
	 */
	private Boolean mergeFormAttributes = true;

	/**
	 * 是否删除临时文件
	 */
	private Boolean deleteUploadedFilesOnEnd = false;

	/**
	 * 重复上传文件是否覆盖或者替换，默认为false，不覆盖不替换重命名保存
	 */
	private Boolean whetherToOverwriteOrReplace = false;

	private String doGetUrlPath;

	/**
	 * 鉴权拦截根地址
	 */
	private String authInterceptPath;

	/**
	 * 会话过期时长
	 */
	private int expires;

	/**
	 * 是否对请求进行真实性鉴权
	 */
	private Boolean authentication = true;

	/**
	 * 忽略鉴权
	 */
	@JsonDeserialize(using = FieldStringToArraysDeserializer.class)
	private String[] ignoreAuthentication;

	/**
	 * 加密方式
	 */
	private String algorithm;

	/**
	 * 公钥
	 */
	private String jwtPublicKey;

	/**
	 * 密钥
	 */
	private String jwtSecretKey;

	/**
	 * 密码加密密钥
	 */
	private String passwordSecretKey;

	public Integer getWorkerPoolSize() {
		return workerPoolSize;
	}

	public void setWorkerPoolSize(Integer workerPoolSize) {
		this.workerPoolSize = workerPoolSize;
	}

	public Long getMaxWorkerExecuteTime() {
		return maxWorkerExecuteTime;
	}

	public void setMaxWorkerExecuteTime(Long maxWorkerExecuteTime) {
		this.maxWorkerExecuteTime = maxWorkerExecuteTime;
	}

	public TimeUnit getMaxWorkerExecuteTimeUnit() {
		return TimeUnit.valueOf(maxWorkerExecuteTimeUnit);
	}

	public void setMaxWorkerExecuteTimeUnit(String maxWorkerExecuteTimeUnit) {
		this.maxWorkerExecuteTimeUnit = maxWorkerExecuteTimeUnit;
	}

	public Long getBodyLimit() {
		return bodyLimit;
	}

	public void setBodyLimit(Long bodyLimit) {
		this.bodyLimit = bodyLimit;
	}

	public Boolean getHandleFileUploads() {
		return handleFileUploads;
	}

	public void setHandleFileUploads(Boolean handleFileUploads) {
		this.handleFileUploads = handleFileUploads;
	}

	public Boolean getPreallocateBodyBuffer() {
		return preallocateBodyBuffer;
	}

	public void setPreallocateBodyBuffer(Boolean preallocateBodyBuffer) {
		this.preallocateBodyBuffer = preallocateBodyBuffer;
	}

	public String getUploadsDirectory() {
		return uploadsDirectory;
	}

	public void setUploadsDirectory(String uploadsDirectory) {
		this.uploadsDirectory = uploadsDirectory;
	}

	public Boolean getMergeFormAttributes() {
		return mergeFormAttributes;
	}

	public void setMergeFormAttributes(Boolean mergeFormAttributes) {
		this.mergeFormAttributes = mergeFormAttributes;
	}

	public Boolean getDeleteUploadedFilesOnEnd() {
		return deleteUploadedFilesOnEnd;
	}

	public void setDeleteUploadedFilesOnEnd(Boolean deleteUploadedFilesOnEnd) {
		this.deleteUploadedFilesOnEnd = deleteUploadedFilesOnEnd;
	}

	public Boolean getWhetherToOverwriteOrReplace() {
		return whetherToOverwriteOrReplace;
	}

	public void setWhetherToOverwriteOrReplace(Boolean whetherToOverwriteOrReplace) {
		this.whetherToOverwriteOrReplace = whetherToOverwriteOrReplace;
	}

	public String getDoGetUrlPath() {
		return doGetUrlPath;
	}

	public void setDoGetUrlPath(String doGetUrlPath) {
		this.doGetUrlPath = doGetUrlPath;
	}

	public String getAuthInterceptPath() {
		return authInterceptPath;
	}

	public void setAuthInterceptPath(String authInterceptPath) {
		this.authInterceptPath = authInterceptPath;
	}

	public int getExpires() {
		return expires;
	}

	public void setExpires(int expires) {
		this.expires = expires;
	}

	public Boolean getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Boolean authentication) {
		this.authentication = authentication;
	}

	public String[] getIgnoreAuthentication() {
		return ignoreAuthentication;
	}

	public void setIgnoreAuthentication(String[] ignoreAuthentication) {
		this.ignoreAuthentication = ignoreAuthentication;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getJwtPublicKey() {
		return jwtPublicKey;
	}

	public void setJwtPublicKey(String jwtPublicKey) {
		this.jwtPublicKey = jwtPublicKey;
	}

	public String getJwtSecretKey() {
		return jwtSecretKey;
	}

	public void setJwtSecretKey(String jwtSecretKey) {
		this.jwtSecretKey = jwtSecretKey;
	}

	public String getPasswordSecretKey() {
		return passwordSecretKey;
	}

	public void setPasswordSecretKey(String passwordSecretKey) {
		this.passwordSecretKey = passwordSecretKey;
	}

}
