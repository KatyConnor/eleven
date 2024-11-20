package hx.nine.eleven.file.client;

import java.io.Serializable;
import java.util.List;

public class RequestBodyDTO implements Serializable {

	private List<FileUploadeDTO> fileList;

	public List<FileUploadeDTO> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileUploadeDTO> fileList) {
		this.fileList = fileList;
	}

	@Override
	public String toString() {
		return JSONObjectMapper.toJsonString(this);
	}
}
