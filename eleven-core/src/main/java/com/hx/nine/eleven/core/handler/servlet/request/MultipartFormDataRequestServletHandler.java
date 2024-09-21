package com.hx.nine.eleven.core.handler.servlet.request;

import com.hx.nine.eleven.commons.utils.*;
import com.hx.nine.eleven.core.constant.DefualtProperType;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import com.hx.nine.eleven.core.core.entity.FileUploadEntity;
import com.hx.nine.eleven.core.handler.WebRequestServiceHandler;
import com.hx.nine.eleven.core.handler.servlet.ServletHandler;
import com.hx.nine.eleven.core.handler.servlet.responese.AsyncFileResponse;
import com.hx.nine.eleven.core.utils.SystemUtils;
import com.hx.nine.eleven.core.web.DefaultHttpHeader;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 文件、表单传输处理
 */
public class MultipartFormDataRequestServletHandler implements ServletHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultipartFormDataRequestServletHandler.class);

	@Override
	public Object doRequest(RoutingContext context) {
		// 判断是否文件同步功能，如果是文件同步，直接将文件进行本地保存即可
		String asyncFile = context.request().getHeader(DefualtProperType.SYNC_FILE);
		if (StringUtils.isNotBlank(asyncFile)) {
			// 请求参数
			MultiMap multiMap = context.request().params();
			Object targetFileObj = multiMap.get(DefualtProperType.TARGET_FILE_PATH);
			Map<String,Object> targetFilePathMap = targetFileObj == null?null:targetFileObj instanceof Map?(Map<String, Object>)targetFileObj:
					targetFileObj instanceof String? JSONObjectMapper.parseObject(String.valueOf(targetFileObj),Map.class) : BeanMapUtil.beanToMap(targetFileObj);
			// 文件,默认处理
			List<FileUpload> fileUploads = context.fileUploads();
			try {
				if (!ObjectUtils.isEmpty(fileUploads)) {
					fileUploads.forEach(f -> {
						Object target = targetFilePathMap==null?null:targetFilePathMap.get(f.fileName());
						String targetFile = StringUtils.isNotEmpty(target)?target.toString():f.uploadedFileName().substring(0, f.uploadedFileName().
								lastIndexOf(SystemUtils.osName() == 1 ? "\\" : "/")) +
								(SystemUtils.osName() == 1 ? "\\" : "/") + f.fileName();
						if (!FileUploadEntity.build().uploadFile(f.uploadedFileName(), targetFile)){
							throw new RuntimeException(StringUtils.format("[{}] 文件保存失败",f.fileName()));
						}
						LOGGER.info("同步文件名称:[{}]", f.fileName());
					});
				}
			} catch (Exception ex) {
				LOGGER.error("文件同步保存失败");
				return Builder.of(AsyncFileResponse::new).build().failure();
			}
			LOGGER.info("文件同步保存成功，总文件数: [{}]", fileUploads.size());
			AsyncFileResponse response = Builder.of(AsyncFileResponse::new).build().successful();
			return response;
		}
		// 请求参数
		MultiMap multiMap = context.request().params();
		// 文件,默认处理
		List<FileUpload> fileUploads = context.fileUploads();
		// 调用路由
		WebRequestServiceHandler servletHandler = DefaultVertxApplicationContext.build().getBean(WebRequestServiceHandler.class);
		if(!Optional.of(servletHandler).isPresent()){
			throw new RuntimeException("could not find class implement WebRequestServiceHandler");
		}
		// 文件处理
		AtomicReference<Object> res = new AtomicReference<>();
		JsonObject jsonObject = new JsonObject();
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
				FileUploadEntity fileUploadEntity = FileUploadEntity.build().setFileUploads(f, targetFile);
				fileUploadEntities.add(fileUploadEntity);
			});
			res.set(servletHandler.doFileService(context, jsonObject, fileUploadEntities));
		} else {
			res.set(servletHandler.doService(context, jsonObject));
		}
		// 设置返回
		setHeader(context, DefaultHttpHeader.build().getHeaders());
		return res.get();
	}

	public static MultipartFormDataRequestServletHandler build() {
		return new MultipartFormDataRequestServletHandler();
	}


}
