package hx.nine.eleven.file.client;

import java.io.Serializable;

public class FileUploadeDTO implements Serializable {

	/**
	 * 数据文件来源
	 */
	private String fileSource;
	/**
	 * 文件名称
	 */
	private String fileName;

	public String getFileSource() {
		return fileSource;
	}

	public FileUploadeDTO setFileSource(String fileSource) {
		this.fileSource = fileSource;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public FileUploadeDTO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public static FileUploadeDTO build(){
		return new FileUploadeDTO();
	}

	@Override
	public String toString() {
		return JSONObjectMapper.toJsonString(this);
	}
}
