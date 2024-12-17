package hx.nine.eleven.httpclient;

import hx.nine.eleven.commons.utils.JSONObjectMapper;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.httpclient.exception.HttpCheckException;
import org.apache.hc.client5.http.ClientProtocolException;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Map;

public class ElevenHttpClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(ElevenHttpClient.class);

	private CloseableHttpClient httpClient;

	private String url;

	private String method;

	private Map<String, String> headers;

	private HttpEntity httpEntity;

	private Class<?> resClass;

	private File file;

	private String uploadFileName;

	public ElevenHttpClient build() {
		if (this.httpClient == null) {
			this.httpClient = ElevenApplicationContextAware.getBean(CloseableHttpClient.class);
		}
		return new ElevenHttpClient();
	}

	public String getUrl() {
		return url;
	}

	public ElevenHttpClient setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getMethod() {
		return method;
	}

	public ElevenHttpClient setMethod(String method) {
		this.method = method;
		return this;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public ElevenHttpClient setHeaders(Map<String, String> headers) {
		this.headers = headers;
		return this;
	}

	public HttpEntity getHttpEntity() {
		return httpEntity;
	}

	public ElevenHttpClient setHttpEntity(HttpEntity httpEntity) {
		this.httpEntity = httpEntity;
		return this;
	}

	public Class<?> getResClass() {
		return resClass;
	}

	public ElevenHttpClient setResClass(Class<?> resClass) {
		this.resClass = resClass;
		return this;
	}

	public File getFile() {
		return file;
	}

	public ElevenHttpClient setFile(File file) {
		this.file = file;
		return this;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getOrHead(String url, String method, Map<String, String> headers) throws IOException {
		HttpUriRequestBase request = new HttpUriRequestBase(method, URI.create(url));
		BasicHeader[] head = mapToHeaders(headers);
		request.setHeaders(head);
		return httpClient.execute(request, response -> {
			try {
				return EntityUtils.toString(response.getEntity());
			} catch (ParseException var3) {
				throw new ClientProtocolException(var3);
			}
		});
	}

	public <T> T post(HttpClientResponseHandler<? extends T> responseHandler){
		if (StringUtils.isEmpty(this.url)){
			throw new HttpCheckException("请求目的地址为空url:[{}]",this.url);
		}

		HttpPost request = new HttpPost(this.url);
		if (ObjectUtils.isNotEmpty(this.headers)){
			BasicHeader[] head = mapToHeaders(this.headers);
			request.setHeaders(head);
		}
		if (ObjectUtils.isEmpty(this.httpEntity)){
			LOGGER.info("请求body数据为空，请检查是否必传数据库结构，避免请求处理异常");
		}else {
			request.setEntity(this.httpEntity);
		}
		try {
			return httpClient.execute(request,responseHandler);
		} catch (Exception e) {
			LOGGER.error("请求处理异常",e);
		}
		return null;
	}

	public HttpResponse post(){
		if (StringUtils.isEmpty(this.url)){
			throw new HttpCheckException("请求目的地址为空url:[{}]",this.url);
		}
		HttpPost request = new HttpPost(this.url);
		if (ObjectUtils.isNotEmpty(this.headers)){
			BasicHeader[] head = mapToHeaders(this.headers);
			request.setHeaders(head);
		}
		if (ObjectUtils.isEmpty(this.httpEntity)){
			LOGGER.info("请求body数据为空，请检查是否必传数据库结构，避免请求处理异常");
		}else {
			request.setEntity(this.httpEntity);
		}

		try {
			return httpClient.execute(request,httpResponse ->{
				if (httpResponse.getCode() == HttpStatus.SC_OK) {
					HttpResponse response = HttpResponse.build().setCode(HttpStatus.SC_OK)
							.setMessage(httpResponse.getReasonPhrase());
					try {
						String resStr = EntityUtils.toString(httpResponse.getEntity());
						if (StringUtils.isNotEmpty(resStr)){
							Object resBody = JSONObjectMapper.parseObject(resStr,resClass);
							response.setBody(resBody);
							return response;
						}
						LOGGER.info("对方返回数据为空： code:{}, message:{}, body: [{}]", httpResponse.getCode(),
								httpResponse.getReasonPhrase(),resStr);
					} catch (Exception e) {
						LOGGER.error(StringUtils.format("处理 response 结果异常, code:{}, message:{},请检查对方是否正常返回数据",
								StringUtils.valueOf(httpResponse.getCode()),
								httpResponse.getReasonPhrase()),e);
					}
					return response;
				} else {
					LOGGER.error("请求处理失败, code:{}, message:{}", httpResponse.getCode(), httpResponse.getReasonPhrase());
					return HttpResponse.build().setCode(httpResponse.getCode())
							.setMessage(httpResponse.getReasonPhrase());
				}
			});
		} catch (Exception e) {
			LOGGER.error("请求处理异常");
		}
		return null;
	}

	public HttpResponse uploadFile() {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addBinaryBody("file", this.file, ContentType.MULTIPART_FORM_DATA, this.uploadFileName);
		HttpEntity multipartEntity = builder.build();
       return this.post();
	}

	public void sync(){
		CloseableHttpAsyncClient asyncClient = HttpAsyncClients.createDefault();
		asyncClient.start();
		SimpleHttpRequest httpRequest = SimpleHttpRequest.create("GET",this.url);
//		httpRequest.setBody();
		asyncClient.execute(httpRequest, new FutureCallback<SimpleHttpResponse>() {
			@Override
			public void completed(SimpleHttpResponse response) {
				System.out.println("Response received: " + response.getCode());
			}

			@Override
			public void failed(Exception ex) {
				System.out.println("Request failed: " + ex.getMessage());
			}

			@Override
			public void cancelled() {
				System.out.println("Request cancelled");
			}
		});
	}

	private static BasicHeader[] mapToHeaders(Map<String, String> map) {
		BasicHeader[] headers = new BasicHeader[map.size()];
		int i = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			headers[i++] = new BasicHeader(entry.getKey(), entry.getValue());
		}
		return headers;
	}

	public static void closeQuietly(Closeable is) {
		if (is != null) {
			try {
				is.close();
			} catch (Exception ex) {
//                log.error("Resources encounter an exception when closing，ex：{}", ex.getMessage());
			}
		}
	}
}
