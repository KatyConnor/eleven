package com.hx.domain.framework.web;

import com.hx.domain.framework.constant.WebHttpBodyConstant;
import com.hx.domain.framework.exception.DomainOperatorException;
import com.hx.domain.framework.exception.ParamsValidationExcetion;
import com.hx.domain.framework.obj.vo.ErrorVO;
import com.hx.domain.framework.request.WebHttpRequest;
import com.hx.domain.framework.response.ResponseEntity;
import com.hx.domain.framework.syscode.DomainApplicationSysCode;
import com.hx.domain.framework.utils.MessageCodeUtils;
import com.hx.lang.commons.entity.ValidationResultEntity;
import com.hx.lang.commons.utils.Builder;
import com.hx.lang.commons.utils.ValidationUtils;
import com.hx.vertx.boot.constant.ConstantType;
import com.hx.vertx.boot.core.entity.FileUploadEntity;
import com.hx.vertx.boot.handler.WebRequestServiceHandler;
import com.hx.vertx.boot.utils.HXLogger;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;
import java.util.Optional;

/**
 * http request请求处理
 */
public class WebRoutorServiceHandler implements WebRequestServiceHandler {

	@Override
	public Object doService(RoutingContext context, JsonObject jsonObject) {
		checkRequestParams(context,jsonObject);
		WebHttpRequest webHttpRequest = jsonObject.mapTo(WebHttpRequest.class);
		validation(context,webHttpRequest);
		Object obj = WebServletRoutor.build().doService(context,webHttpRequest);
		if (obj == null){
			HXLogger.build(this).info("[WebRequestServiceHandler]: 业务处理返回对象为null。");
			return obj;
		}
		JsonObject jsonObj = JsonObject.mapFrom(obj);
		// 当返回的是文件流，则进行文件流返回处理
		Boolean isDownload = jsonObj.getBoolean(WebHttpBodyConstant.FILE_STREAM);
		if (isDownload != null && isDownload){
			context.response()
					.putHeader(WebHttpBodyConstant.FILE_STREAM,String.valueOf(isDownload))
					.putHeader(WebHttpBodyConstant.FILE_DOWNLOAD_PATH,jsonObj.getString(WebHttpBodyConstant.FILE_DOWNLOAD_PATH));
		}
		return obj;
	}

	/**
	 * 文件上传
	 * 如果没有指定body,则赋值Optional.empty()给requestBody,跳过NotNull验证
	 * @param jsonObject
	 * @param list
	 * @return
	 */
	@Override
	public Object doFileService(RoutingContext context,JsonObject jsonObject, List<FileUploadEntity> list) {
		checkRequestParams(context,jsonObject);
		WebHttpRequest webHttpRequest = jsonObject.mapTo(WebHttpRequest.class);
		webHttpRequest.setFileUploadEntities(list);
		validation(context,webHttpRequest);
		return WebServletRoutor.build().doService(context,webHttpRequest);
	}

	/**
	 * 对参数进行注解校验
	 * @param dto
	 */
	private void validation(RoutingContext context,Object dto) {
		Optional.ofNullable(dto).ifPresent(d ->{
			ValidationResultEntity result = ValidationUtils.validateEntity(dto);
			if (result.isHasErrors()) {
				ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
						MessageCodeUtils.format(DomainApplicationSysCode.V0000010001,result.getErrorMsg())).build();
				ResponseEntity res = ResponseEntity.build().addData(errorVO).failure();
				context.put(ConstantType.RESPONSE_BODY,res);
				throw new ParamsValidationExcetion(result.getErrorMsg());
			}
		});
	}

	private void checkRequestParams(RoutingContext context,JsonObject jsonObject){
		if (!Optional.ofNullable(jsonObject).isPresent() || jsonObject.size() <= 0){
			ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
					MessageCodeUtils.format(DomainApplicationSysCode.A0300000004)).build();
			ResponseEntity res = ResponseEntity.build().addData(errorVO).failure();
			context.put(ConstantType.RESPONSE_BODY,res);
			throw new DomainOperatorException(DomainApplicationSysCode.A0300000004);
		}
	}
}
