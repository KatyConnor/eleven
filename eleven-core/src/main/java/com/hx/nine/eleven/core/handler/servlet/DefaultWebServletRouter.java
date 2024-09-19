package com.hx.nine.eleven.core.handler.servlet;

import com.hx.lang.commons.utils.StringUtils;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.enums.ContentTypeEnums;
import com.hx.nine.eleven.core.exception.VertxApplicationRunException;
import com.hx.nine.eleven.core.handler.WebServletRouter;
import com.hx.nine.eleven.core.handler.servlet.request.ApplicationJsonRequestServletHandler;
import com.hx.nine.eleven.core.handler.servlet.request.MultipartFormDataRequestServletHandler;
import io.vertx.ext.web.RoutingContext;

/**
 * web访问路由
 * 根据不同的content type进行不同的处理，并且对参数进行验证处理
 * @author wml
 * @date 2023-03-24
 */
public class DefaultWebServletRouter implements WebServletRouter {

	@Override
	public Object router(RoutingContext context) {
		String contentType = context.request().getHeader(ConstantType.CONTENT_TYPE);
		// 如果不带contentType，默认走APPLICATION_JSON
		if (StringUtils.isEmpty(contentType)){
			contentType = ContentTypeEnums.APPLICATION_JSON.getCode();
		}
		// contentType
		ContentTypeEnums contentTypeEnums = ContentTypeEnums.getByCode(contentType.split(";")[0]);
		switch (contentTypeEnums){
			// 文件传输
			case MULTIPART_FORM_DATA:
				return MultipartFormDataRequestServletHandler.build().doRequest(context);
			case APPLICATION_JSON:
				return ApplicationJsonRequestServletHandler.build().doRequest(context);
			default:
				//暂不支持的类型
				throw new VertxApplicationRunException("暂不支持的content-type类型");
		}
	}

	public static DefaultWebServletRouter build(){
		return Signle.INSTANCE;
	}

	private final static class Signle {
		private final static DefaultWebServletRouter INSTANCE = new DefaultWebServletRouter();
	}
}
