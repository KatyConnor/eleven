package com.hx.nine.eleven.core.core.entity;

import com.hx.nine.eleven.commons.utils.DateUtils;
import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.properties.ElevenBootApplicationProperties;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.core.utils.SystemUtils;
import com.hx.nine.eleven.core.utils.UUIDGenerator;
import com.hx.nine.eleven.core.utils.ElevenObjectUtils;
import io.vertx.ext.web.FileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件处理实体类
 * 将临时文件还原成目标源文件
 *
 * @author wml
 * @date 2023-01-03
 */
public class FileUploadEntity {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadEntity.class);

	/**
	 * 上传文件临时文件名称
	 */
	private String uploadedFileName;
	/**
	 * 接受文件的参数名称
	 */
	private String name;
	/**
	 * 文件名称
	 */
	private String fileName;
	/**
	 * 请求类型
	 */
	private String contentType;
	private String contentTransferEncoding;
	/**
	 * 上传时间
	 */
	private String uploadTime = DateUtils.getTimeStampAsString();

	/**
	 * 文件格式
	 */
	private String fileFormat;
	/**
	 * 保存的目标文件全路径
	 */
	private String targetFile;

	/**
	 * 文件大小
	 */
	private long fileSize;

	/**
	 * 文件是否处理完毕
	 */
	private boolean fileComplete;

	public static FileUploadEntity build() {
		return new FileUploadEntity();
	}

	/**
	 * 将临时文件转为目标文件
	 *
	 * @param fileUpload 上传的文件
	 * @param targetFile 目标文件
	 * @return
	 */
	public FileUploadEntity setFileUploads(FileUpload fileUpload, String targetFile){
		this.name = fileUpload.name();
		this.fileName = fileUpload.fileName();
		this.fileFormat = fileUpload.fileName().substring(fileUpload.fileName().lastIndexOf("."));
		this.uploadedFileName = fileUpload.uploadedFileName();
		this.contentType = fileUpload.contentType();
		this.contentTransferEncoding = fileUpload.contentTransferEncoding();
		this.targetFile = targetFile;
		try {
			this.fileSize = Files.size(Paths.get(uploadedFileName));
		} catch (IOException e) {
			ElevenLoggerFactory.build(this).error("文件大小获取异常",e);
		}
		if (!uploadFile(this.uploadedFileName,targetFile)){
			throw new RuntimeException(StringUtils.format("[{}] 文件保存失败",this.fileName));
		}
		return this;
	}

	public boolean uploadFile(String uploadedFileName, String targetFile){
		try {
			//检查文件是否存在,如果重复上传,方案一:直接覆盖,方案二：不覆盖重命名保存
			Path targetFilePath = Paths.get(targetFile);
			if (Files.exists(targetFilePath)) {
				ElevenBootApplicationProperties properties = ElevenApplicationContextAware.getProperties(ElevenBootApplicationProperties.class);
				// 不直接覆盖源文件
				if (!properties.getWhetherToOverwriteOrReplace()) {
					analysisTargetFile(targetFile);
					ElevenObjectUtils.build().move(uploadedFileName,this.targetFile);
					LOGGER.info(StringUtils.format("[{}]文件已经存在, 文件重命名为[{}]保存 ",targetFile,this.targetFile));
					return true;
				}
				// 同步删掉文件
				ElevenObjectUtils.build().delete(targetFilePath);
			}
			ElevenObjectUtils.build().move(uploadedFileName,targetFile, ElevenObjectUtils.DEFAULT_OPTIONS);
			LOGGER.info(StringUtils.format("文件保存成功,保存地址 [{}]",targetFile));
		} catch (Exception e) {
			LOGGER.error("文件上传失败,[]: ",e.getMessage(),e);
			return false;
		}
		return true;
	}

	public String getUploadedFileName() {
		return uploadedFileName;
	}

	public void setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
		this.fileFormat = this.fileName.split(".")[1];
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}

	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}

	public String getTargetFile() {
		return targetFile;
	}

	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public void setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
	}

	private void analysisTargetFile(String targetFile){
		int lastSpile = targetFile.lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/");
		this.targetFile = targetFile.substring(0, lastSpile) + (SystemUtils.osName() == 1 ? "\\" : "/") +
				UUIDGenerator.getUUID() +"-"+ targetFile.substring(lastSpile + 1);
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public boolean isFileComplete() {
		return fileComplete;
	}

	public void setFileComplete(boolean fileComplete) {
		this.fileComplete = fileComplete;
	}
}
