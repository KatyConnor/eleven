package hx.nine.eleven.file.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class HttpClientExcutor {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientExcutor.class);

	/**
	 * 文件上传,POST请求
	 * @param requestEntityString
	 * @return
	 */
	public static String uploadFile(HttpRequestEntity requestEntityString) {
		LOGGER.info("开始上传文件");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		String result = "";
		try {
			HttpPost httpPost = new HttpPost(requestEntityString.getuRL());
			//设置请求头
			setHeader(httpPost,requestEntityString.getHeaders());
			// 设置文件
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			if (requestEntityString.getFile() != null && requestEntityString.getFile().exists()){
				addMultipartFile(builder,requestEntityString.getFile());
			}
			if (requestEntityString.getInputStream() != null){
				addMultipartFileInputStream(builder,requestEntityString.getInputStream(),"");
			}
			//设置请求报文的报文体
			Object body = requestEntityString.getBody();
			Optional.ofNullable(body).ifPresent(b ->{
				ContentType contentType = ContentType.create("application/json", Charset.forName("UTF-8"));
				if (b instanceof FileUploadBodyDTO){
					FileUploadBodyDTO requestBody = (FileUploadBodyDTO)b;
					if (Objects.nonNull(requestBody.getRequestHeader())) {
						builder.addTextBody("requestHeader", requestBody.getRequestHeader().toString(), contentType);// 类似浏览器表单提交，对应input的name和value
					}
					if (Objects.nonNull(requestBody.getRequestBody())) {
						builder.addTextBody("requestBody", requestBody.getRequestBody().toString(), contentType);// 类似浏览器表单提交，对应input的name和value
					}
				}
			});
			HttpEntity entity = builder.build();
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);// 执行提交
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				result = EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
				LOGGER.info("文件上传完成，返回结果: {}",result);
				System.out.println("文件上传完成，返回结果: {}"+result);
			}
		} catch (IOException e) {
			LOGGER.error("文件上传IO异常：",e);
		} catch (Exception e) {
			LOGGER.error("文件上传异常：",e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				LOGGER.error("关闭http连接异常：",e);
			}
		}
		return result;
	}

	/**
	 * 设置请求头
	 * @param httpPost
	 * @param header
	 */
	private static void setHeader(HttpPost httpPost,HttpHeader header){
		if (!Optional.ofNullable(header).isPresent()){
			return;
		}
		if (header.getHeader() !=null){
			httpPost.setHeader(header.getHeader());
		}
		if (header.getHeaderes() !=null && header.getHeaderes().length > 0){
			httpPost.setHeaders(header.getHeaderes());
		}

		if (header.getHeaders() != null && header.getHeaders().size() > 0) {
			for (Map.Entry<String, String> e : header.getHeaders().entrySet()) {
				String value = e.getValue();
				String key = e.getKey();
				if (value != null && !value.isEmpty()) {
					httpPost.setHeader(key, value);
				}
			}
		}
	}

	/**
	 * 上传文件
	 * @param builder
	 * @param file
	 */
	private static void addMultipartFile(MultipartEntityBuilder builder, File file){
		builder.setCharset(Charset.forName("utf-8"));
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
		if (file.isDirectory()){
			File[] files = file.listFiles();
			int i = 1;
			for (File file1 : files){
				FileBody fileBody = new FileBody(file1);
				builder.addPart("file"+i,fileBody);
				i++;
			}
		}
		if (file.isFile()){
			FileBody fileBody = new FileBody(file);
			builder.addPart("file",fileBody);
		}
	}

	/**
	 *  添加上传文件流
	 * @param builder
	 * @param inputStream
	 * @param fileParamName
	 */
	private static void addMultipartFileInputStream(MultipartEntityBuilder builder, InputStream inputStream,String fileParamName){
		builder.setCharset(Charset.forName("utf-8"));
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
		builder.addBinaryBody("file", inputStream,ContentType.DEFAULT_BINARY,fileParamName);// 文件流
	}
}
