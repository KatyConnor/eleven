package hx.nine.eleven.core.entity;

import hx.nine.eleven.commons.utils.DateUtils;
import hx.nine.eleven.commons.utils.FileUtil;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.core.utils.SystemUtils;
import hx.nine.eleven.core.utils.UUIDGenerator;
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
	/**
	 * 重复上传文件是否覆盖或者替换，默认为false，不覆盖不替换重命名保存
	 */
	private Boolean whetherToOverwriteOrReplace = false;

	public static FileUploadEntity build() {
		return new FileUploadEntity();
	}

	/**
	 * 将临时文件转为目标文件
	 *
	 * @param targetFile 目标文件
	 * @return
	 */
	public FileUploadEntity setFileUploads(String name,String fileName,String fileFormat,String uploadedFileName,
										   String contentType,String contentTransferEncoding,String targetFile){
		this.name = name;
		this.fileName = fileName;
		this.fileFormat = fileFormat;
		this.uploadedFileName = uploadedFileName;
		this.contentType = contentType;
		this.contentTransferEncoding = contentTransferEncoding;
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
				// 不直接覆盖源文件
				if (!this.whetherToOverwriteOrReplace) {
					analysisTargetFile(targetFile);
					FileUtil.move(uploadedFileName,this.targetFile);
					LOGGER.info(StringUtils.format("[{}]文件已经存在, 文件重命名为[{}]保存 ",targetFile,this.targetFile));
					return true;
				}
				// 同步删掉文件
				FileUtil.delete(targetFilePath);
			}
			FileUtil.move(uploadedFileName,targetFile, FileUtil.DEFAULT_OPTIONS);
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

	public FileUploadEntity setUploadedFileName(String uploadedFileName) {
		this.uploadedFileName = uploadedFileName;
		return this;
	}

	public String getName() {
		return name;
	}

	public FileUploadEntity setName(String name) {
		this.name = name;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public FileUploadEntity setFileName(String fileName) {
		this.fileName = fileName;
		this.fileFormat = this.fileName.split(".")[1];
		return this;
	}

	public String getContentType() {
		return contentType;
	}

	public FileUploadEntity setContentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}

	public FileUploadEntity setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
		return this;
	}

	public String getTargetFile() {
		return targetFile;
	}

	public FileUploadEntity setTargetFile(String targetFile) {
		this.targetFile = targetFile;
		return this;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public FileUploadEntity setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
		return this;
	}

	public String getFileFormat() {
		return fileFormat;
	}

	public FileUploadEntity setFileFormat(String fileFormat) {
		this.fileFormat = fileFormat;
		return this;
	}

	private FileUploadEntity analysisTargetFile(String targetFile){
		int lastSpile = targetFile.lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/");
		this.targetFile = targetFile.substring(0, lastSpile) + (SystemUtils.osName() == 1 ? "\\" : "/") +
				UUIDGenerator.getUUID() +"-"+ targetFile.substring(lastSpile + 1);
		return this;
	}

	public long getFileSize() {
		return fileSize;
	}

	public FileUploadEntity setFileSize(long fileSize) {
		this.fileSize = fileSize;
		return this;
	}

	public boolean isFileComplete() {
		return fileComplete;
	}

	public FileUploadEntity setFileComplete(boolean fileComplete) {
		this.fileComplete = fileComplete;
		return this;
	}

	public Boolean getWhetherToOverwriteOrReplace() {
		return whetherToOverwriteOrReplace;
	}

	public FileUploadEntity setWhetherToOverwriteOrReplace(Boolean whetherToOverwriteOrReplace) {
		this.whetherToOverwriteOrReplace = whetherToOverwriteOrReplace;
		return this;
	}
}
