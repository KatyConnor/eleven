package hx.nine.eleven.file.client;

import java.io.Serializable;

public class FileUploadBodyDTO implements Serializable {

	private RequestHeaderDTO requestHeader;

	private RequestBodyDTO requestBody;

	public RequestHeaderDTO getRequestHeader() {
		return requestHeader;
	}

	public FileUploadBodyDTO setRequestHeader(RequestHeaderDTO requestHeader) {
		this.requestHeader = requestHeader;
		return this;
	}

	public RequestBodyDTO getRequestBody() {
		return requestBody;
	}

	public FileUploadBodyDTO setRequestBody(RequestBodyDTO requestBody) {
		this.requestBody = requestBody;
		return this;
	}

	public static FileUploadBodyDTO build(){
		return new FileUploadBodyDTO();
	}

	@Override
	public String toString() {
		return JSONObjectMapper.toJsonString(this);
	}
}
