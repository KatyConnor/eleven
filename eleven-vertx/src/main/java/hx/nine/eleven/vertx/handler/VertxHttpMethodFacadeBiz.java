package hx.nine.eleven.vertx.handler;

import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.commons.utils.JSONObjectMapper;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.entity.FileUploadEntity;
import hx.nine.eleven.core.enums.ContentTypeEnums;
import hx.nine.eleven.core.enums.HttpMethodEnum;
import hx.nine.eleven.core.exception.ElevenApplicationRunException;
import hx.nine.eleven.core.exception.ServletException;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.core.utils.SystemUtils;
import hx.nine.eleven.core.web.http.DefaultHttpHeader;
import hx.nine.eleven.core.web.http.HttpMethodFacade;
import hx.nine.eleven.core.web.http.HttpResponse;
import hx.nine.eleven.core.web.http.HttpServletRequest;
import hx.nine.eleven.core.web.http.HttpServletResponse;
import hx.nine.eleven.vertx.constant.VertxConstantType;
import hx.nine.eleven.vertx.constant.DefaultVertxProperType;
import hx.nine.eleven.vertx.constant.VertxHttpMethod;
import hx.nine.eleven.vertx.properties.VertxApplicationProperties;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * http 请求具体方法实现
 * @auth wml
 * @date 2024/11/7
 */
public class VertxHttpMethodFacadeBiz implements HttpMethodFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(VertxHttpMethodFacadeBiz.class);

	private VertxApplicationProperties properties;

	@Override
	public void preService(HttpServletRequest request) {
		if (this.properties == null){
			this.properties = ElevenApplicationContextAware.getProperties(VertxApplicationProperties.class);
		}
		// 是否放权，如果是登录，则验证登录地址
		RoutingContext context = (RoutingContext)request.getAttribute(DefaultVertxProperType.VERTX_CONTEXT);
		String contentType = context.request().getHeader(VertxConstantType.CONTENT_TYPE);
		// 如果不带contentType，默认走APPLICATION_JSON
		if (StringUtils.isEmpty(contentType)) {
			contentType = ContentTypeEnums.APPLICATION_JSON.getCode();
		}
		HttpMethod httpMethod = context.request().method();
		request.setContentType(contentType);
		// contentType
//		ContentTypeEnums contentTypeEnums = ContentTypeEnums.getByCode(contentType.split(";")[0]);
		switch (httpMethod.name()) {
			// 文件传输
			case VertxHttpMethod.GET:
				request.setHttpMethod(HttpMethodEnum.METHOD_GET);
				break;
			case VertxHttpMethod.POST:
				request.setHttpMethod(HttpMethodEnum.METHOD_POST);
				break;
			default:
				//暂不支持的类型
				throw new ElevenApplicationRunException(httpMethod.name()+": 暂不支持的 http method 类型");
		}
	}

	/**
	 * 只需要处理get请求参数，统一放入 httpServletRequest ，后续路由的调用在
	 * {@like hx.nine.eleven.core.web.http.HttpServlet.service} 中已经触发
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		RoutingContext context = (RoutingContext)httpServletRequest.getAttribute(DefaultVertxProperType.VERTX_CONTEXT);
		JsonObject jsonObject = new JsonObject();
		MultiMap multiMap = context.request().params();
		if (Optional.ofNullable(multiMap).isPresent() && multiMap.entries().size() > 0) {
			multiMap.entries().forEach(e -> {
				jsonObject.put(e.getKey(), e.getValue());
			});
		}
		// 设置http post 请求报文的body数据
		httpServletRequest.addBody(jsonObject);
	}

	@Override
	public void doHead(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	}

	/**
	 * 只需要处理get请求参数，统一放入 httpServletRequest ，后续路由的调用在
	 * {@like hx.nine.eleven.core.web.http.HttpServlet.service} 中已经触发
	 * @param httpServletRequest
	 * @param httpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
		RoutingContext context = (RoutingContext)httpServletRequest.getAttribute(DefaultVertxProperType.VERTX_CONTEXT);
		// 读取Body数据
		Buffer bodyBuffer = context.getBody();
		JsonObject jsonObject = null;
		if (!Optional.ofNullable(bodyBuffer).isPresent()) {
			try {
				AtomicReference<JsonObject> jsonObjectBuff = new AtomicReference<>();
				context.request().bodyHandler((buff) -> {
					jsonObjectBuff.set(buff.toJsonObject());
				});
				jsonObject = jsonObjectBuff.get();
			} catch (Exception exception) {
				// body数据为空
				ElevenLoggerFactory.build(this).error("request body is null,or Request has already been read");
			}
		} else {
			jsonObject = bodyBuffer.toJsonObject();
		}
		// content-type:multipart/form-data,且可能存在文件处理
		initMultipartHttp(jsonObject,httpServletRequest,context);
		// 设置http post 请求报文的body数据
		httpServletRequest.addBody(jsonObject);
	}

	@Override
	public void doPut(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	}

	@Override
	public void doDelete(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	}

	@Override
	public void doOptions(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	}

	@Override
	public void doTrace(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {

	}

	@Override
	public void afterService(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
		// 设置返回信息
		RoutingContext context = (RoutingContext)servletRequest.getAttribute(DefaultVertxProperType.VERTX_CONTEXT);
		HttpResponse response = servletResponse.httpResponse();
		if (StringUtils.isEmpty(response.getCode()) || StringUtils.isEmpty(response.getMessage())){
			servletResponse.send(DefaultVertxProperType.RESPONSE_CODE, DefaultVertxProperType.RESPONSE_MSG);
		}
		setHeader(context, DefaultHttpHeader.build().setApplicationJsonResHeader().getHeaders());
	}

	private void setHeader(RoutingContext context, Map<String, String> headers) {
		HttpServerResponse response = context.response();
		if (headers != null && headers.size() > 0) {
			headers.forEach((k, v) -> {
				response.putHeader(k, v);
			});
		}
	}

	private AsyncFileResponse initMultipartHttp(JsonObject jsonObject,HttpServletRequest httpServletRequest,RoutingContext context) {
		// 判断是否文件同步功能，如果是文件同步，直接将文件进行本地保存即可
		String asyncFile = context.request().getHeader(DefaultVertxProperType.SYNC_FILE);
		if (StringUtils.isNotBlank(asyncFile)) {
			AsyncFileResponse response = asyncFile(context);
			return response;
		}
		// 请求参数
		MultiMap multiMap = context.request().params();
		// 文件,默认处理
		List<FileUpload> fileUploads = context.fileUploads();

		if (Optional.ofNullable(multiMap).isPresent() && multiMap.entries().size() > 0) {
			multiMap.entries().forEach(e -> {
				jsonObject.put(e.getKey(), e.getValue());
			});
		}
		MultiMap attributes = context.request().formAttributes();
		if (Optional.ofNullable(attributes).isPresent() && attributes.entries().size() > 0) {
			attributes.entries().forEach(e -> {
				jsonObject.put(e.getKey(), e.getValue());
			});
		}

		if (!ObjectUtils.isEmpty(fileUploads)) {
			final List<FileUploadEntity> fileUploadEntities = new ArrayList<>(fileUploads.size());
			fileUploads.forEach(f -> {
				String targetFile = f.uploadedFileName().substring(0, f.uploadedFileName().
						lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/")) +
						(SystemUtils.osName() == 1 ? "\\" : "/") + f.fileName();
				String fileFormat = f.fileName().substring(f.fileName().lastIndexOf("."));
				FileUploadEntity fileUploadEntity = FileUploadEntity.build()
						.setFileUploads(f.name(),f.fileName(),fileFormat,f.uploadedFileName(),
								f.contentType(),f.contentTransferEncoding(), targetFile);
				fileUploadEntities.add(fileUploadEntity);
			});
			httpServletRequest.setFileUploadEntities(fileUploadEntities);
		}
		// 设置返回
		setHeader(context, DefaultHttpHeader.build().getHeaders());
		return null;
	}

	/**
	 * 文件同步处理
	 */
	private AsyncFileResponse asyncFile(RoutingContext context) {
		// 请求参数
		AsyncFileResponse response;
		MultiMap multiMap = context.request().params();
		Object targetFileObj = multiMap.get(DefaultVertxProperType.TARGET_FILE_PATH);
		Map<String, Object> targetFilePathMap = targetFileObj == null ? null : targetFileObj instanceof Map ? (Map<String, Object>) targetFileObj :
				targetFileObj instanceof String ? JSONObjectMapper.parseObject(String.valueOf(targetFileObj), Map.class) : BeanMapUtil.beanToMap(targetFileObj);
		// 文件,默认处理
		List<FileUpload> fileUploads = context.fileUploads();
		try {
			if (!ObjectUtils.isEmpty(fileUploads)) {
				fileUploads.forEach(f -> {
					Object target = targetFilePathMap == null ? null : targetFilePathMap.get(f.fileName());
					String targetFile = StringUtils.isNotEmpty(target) ? target.toString() : f.uploadedFileName().substring(0, f.uploadedFileName().
							lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/")) +
							(SystemUtils.osName() == 1 ? "\\" : "/") + f.fileName();
					if (!FileUploadEntity.build()
							.setWhetherToOverwriteOrReplace(this.properties.getWhetherToOverwriteOrReplace())
							.uploadFile(f.uploadedFileName(), targetFile)) {
						throw new RuntimeException(StringUtils.format("[{}] 文件保存失败", f.fileName()));
					}
					LOGGER.info("同步文件名称:[{}]", f.fileName());
				});
			}
		} catch (Exception ex) {
			LOGGER.error("文件同步保存失败");
			response = Builder.of(AsyncFileResponse::new).build().failure();
		}
		LOGGER.info("文件同步保存成功，总文件数: [{}]", fileUploads.size());
		response = Builder.of(AsyncFileResponse::new).build().successful();
		return response;
	}
}
