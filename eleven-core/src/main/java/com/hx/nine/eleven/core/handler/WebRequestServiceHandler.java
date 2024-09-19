package com.hx.nine.eleven.core.handler;

import com.hx.nine.eleven.core.core.entity.FileUploadEntity;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * service处理
 *
 * @author wml
 * @date 2023-03-24
 */
public interface WebRequestServiceHandler {

	/**
	 * @param requestObj 入参
	 * @return
	 */
	Object doService(RoutingContext context,JsonObject requestObj);

	/**
	 * 文件处理
	 * @param requestObj
	 * @param fileUploads
	 * @return
	 */
	Object doFileService(RoutingContext context,JsonObject requestObj, List<FileUploadEntity> fileUploads);
}
