package hx.nine.eleven.file.client;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.protocol.HttpClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

public class HttpRequestEntity<REQ,BODY>{

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestEntity.class);

	private RequestMethodEnum method;
	private String host;
	private String urlPath;
	private HttpHeader headers;
	private BODY body;
	private REQ requestParam;
	private RequestConfig requestConfig;
	private boolean form;
	private ContentType contentType;
	private boolean cookieStore;
	private HttpClientContext httpClientContext;
	private InputStream inputStream;
	private File file;


	private HttpRequestEntity(){}

	public static HttpRequestEntity build(){
		return new HttpRequestEntity();
	}

	public RequestMethodEnum getMethod() {
		return method;
	}

	public HttpRequestEntity setMethod(RequestMethodEnum method) {
		this.method = method;
		return this;
	}

	public String getHost() {
		return host;
	}

	public HttpRequestEntity setHost(String host) {
		this.host = host;
		return this;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public HttpRequestEntity setUrlPath(String urlPath) {
		this.urlPath = urlPath;
		return this;
	}

	public HttpHeader getHeaders() {
		return headers;
	}

	public HttpRequestEntity setHeaders(HttpHeader headers) {
		this.headers = headers;
		return this;
	}

	public BODY getBody() {
		return this.body;
	}

	public HttpRequestEntity setBody(BODY body) {
		this.body = body;
		return this;
	}


	public REQ getRequestParam() {
		return requestParam;
	}

	public HttpRequestEntity setRequestParam(REQ requestParam) {
		this.requestParam = requestParam;
		return this;
	}

	public RequestConfig getRequestConfig() {
		return requestConfig;
	}

	public HttpRequestEntity setRequestConfig(RequestConfig requestConfig) {
		this.requestConfig = requestConfig;
		return this;
	}

	public ContentType getContentType() {
		return contentType;
	}

	public HttpRequestEntity setContentType(ContentType contentType) {
		this.contentType = contentType;
		return this;

	}

	public boolean isForm() {
		return form;
	}

	public HttpRequestEntity setForm(boolean form) {
		this.form = form;
		return this;
	}

	public boolean isCookieStore() {
		return cookieStore;
	}

	public HttpRequestEntity setCookieStore(boolean cookieStore) {
		this.cookieStore = cookieStore;
		return this;
	}

	public HttpClientContext getHttpClientContext() {
		return httpClientContext;
	}

	public HttpRequestEntity setHttpClientContext(HttpClientContext httpClientContext) {
		this.httpClientContext = httpClientContext;
		return this;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public HttpRequestEntity setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public File getFile() {
		return file;
	}

	public HttpRequestEntity setFile(File file) {
		this.file = file;
		return this;
	}

	public String getuRL(){
		return this.host+this.urlPath;
	}
}
