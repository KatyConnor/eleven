package hx.nine.eleven.file.client;

//import io.vertx.core.Vertx;
//import io.vertx.core.buffer.Buffer;
//import io.vertx.core.file.AsyncFile;
//import io.vertx.core.file.OpenOptions;
//import io.vertx.core.file.impl.AsyncFileImpl;
//import io.vertx.core.http.HttpClient;
//import io.vertx.core.streams.ReadStream;
//import io.vertx.ext.web.client.WebClient;
//import io.vertx.ext.web.client.WebClientOptions;
//import io.vertx.ext.web.multipart.MultipartForm;
//import io.vertx.ext.web.multipart.impl.MultipartFormImpl;
//import net.sf.saxon.instruct.ForEach;

//import org.apache.commons.httpclient.Header;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.HttpStatus;
//import org.apache.commons.httpclient.methods.PostMethod;
//import org.apache.commons.httpclient.methods.multipart.FilePart;
//import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
//import org.apache.commons.httpclient.methods.multipart.Part;
//import org.apache.http.client.HttpClient;
//import org.apache.http.entity.InputStreamEntity;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class HtppFileClient {

	private final static Logger LOGGER = LoggerFactory.getLogger(HtppFileClient.class);

	private static final String SYNC_FILE = "SYNC_FILE";

	public static void main(String[] args) throws FileNotFoundException {
//		uploadFile("http://127.0.0.1:20188/fssm","D:\\qycache\\temp_cache\\EasyConnectInstaller.exe");
//		File file = new File("D:\\qycache\\hx-vertx-file-monitor.zip");
//		InputStream reader = new FileInputStream(file);
		long start = System.currentTimeMillis();
		uploadParamFileTest();
//		uploadFileTest();
		System.out.println((System.currentTimeMillis() - start) / 1000 +"/s");
	}

	/**
	 * 文件上传测试
	 */
	public static void uploadParamFileTest(){
		HttpHeader header = HttpHeader.build(); // 请求报文头设置
		RequestHeaderDTO requestHeaderDTO = RequestHeaderDTO.build()
				.setReqNo(getUUID()) //请求流水号
				.setTradeCode("FSSM06500201FMUDM1O1") // 主交易码
				.setSubTradeCode("FSSM06500201FMUDM1O1SUB1UOO1"); // 子交易码
		List<FileUploadeDTO> fileUploadeDTOS = new ArrayList<>();
		FileUploadeDTO fileUploadeDTO = FileUploadeDTO.build().setFileName("edrawmax-cn-9.3.exe").setFileSource("CND");
		FileUploadeDTO fileUploadeDTO1 = FileUploadeDTO.build().setFileName("win64_11gR2_client.zip").setFileSource("CND");
		fileUploadeDTOS.add(fileUploadeDTO);
		fileUploadeDTOS.add(fileUploadeDTO1);
		RequestBodyDTO requestBodyDTO = new RequestBodyDTO();
		requestBodyDTO.setFileList(fileUploadeDTOS);
		FileUploadBodyDTO fileUploadBodyDTO = FileUploadBodyDTO.build().setRequestHeader(requestHeaderDTO).setRequestBody(requestBodyDTO);
		HttpRequestEntity<String,FileUploadBodyDTO> entity = HttpRequestEntity.build()
				.setFile(new File("D:\\qycache\\file-test"))
				.setBody(fileUploadBodyDTO)
				.setHeaders(header)
				.setHost("http://127.0.0.1:20188")
				.setUrlPath("/fssm");
		HttpClientExcutor.uploadFile(entity);
	}

	/**
	 * 不带任何参数上传文件
	 */
	public static void uploadFileTest(){
		HttpHeader header = HttpHeader.build().defaultFileHeader(); // 请求报文头设置
		HttpRequestEntity<String,FileUploadBodyDTO> entity = HttpRequestEntity.build()
				.setFile(new File("D:\\qycache\\hx-vertx-file-monitor.zip"))
				.setHeaders(header)
				.setHost("http://127.0.0.1:20188")
				.setUrlPath("/fssm");
		HttpClientExcutor.uploadFile(entity);
	}

	private static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String temp = uuid.toString().replaceAll("-", "");
		return temp;
	}
}
