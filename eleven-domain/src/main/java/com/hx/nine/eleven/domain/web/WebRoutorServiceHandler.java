package com.hx.nine.eleven.domain.web;

import com.hx.nine.eleven.commons.utils.BeanMapUtil;
import com.hx.nine.eleven.commons.utils.BeanUtils;
import com.hx.nine.eleven.commons.utils.CollectionUtils;
import com.hx.nine.eleven.core.entity.FileUploadEntity;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.core.web.DomainRouter;
import com.hx.nine.eleven.core.web.http.HttpServletRequest;
import com.hx.nine.eleven.core.web.http.HttpServletResponse;
import com.hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import com.hx.nine.eleven.domain.exception.DomainOperatorException;
import com.hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import com.hx.nine.eleven.domain.obj.vo.ErrorVO;
import com.hx.nine.eleven.domain.request.WebHttpRequest;
import com.hx.nine.eleven.domain.response.ResponseEntity;
import com.hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import com.hx.nine.eleven.domain.utils.MessageCodeUtils;
import com.hx.nine.eleven.commons.entity.ValidationResultEntity;
import com.hx.nine.eleven.commons.utils.Builder;
import com.hx.nine.eleven.commons.utils.ValidationUtils;
import com.hx.nine.eleven.core.constant.ConstantType;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * http request请求处理
 */
public class WebRoutorServiceHandler implements DomainRouter {

	@Override
	public void route(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		Map<String,Object> body = httpServletRequest.getBody();
		List<FileUploadEntity> list = httpServletRequest.getFileUploadEntities();
		if (CollectionUtils.isEmpty(list)){
			doService(httpServletResponse,body);
			return;
		}
		doFileService(httpServletResponse,body,list);
	}


	public Object doService(HttpServletResponse httpServletResponse,Map<String,Object> body) {
		checkRequestParams(httpServletResponse,body);
		WebHttpRequest webHttpRequest = BeanMapUtil.mapToBean(body,WebHttpRequest.class);
		validation(httpServletResponse,webHttpRequest);
		Object obj = WebServletRoutor.build().doService(httpServletResponse,webHttpRequest);
		if (obj == null){
			ElevenLoggerFactory.build(this).info("[WebRequestServiceHandler]: 业务处理返回对象为null。");
			return obj;
		}
		JsonObject jsonObj = JsonObject.mapFrom(obj);
		// 当返回的是文件流，则进行文件流返回处理
		Boolean isDownload = jsonObj.getBoolean(WebHttpBodyConstant.FILE_STREAM);
		if (isDownload != null && isDownload){
			httpServletResponse.setHeader(WebHttpBodyConstant.FILE_STREAM,String.valueOf(isDownload));
			httpServletResponse.setHeader(WebHttpBodyConstant.FILE_DOWNLOAD_PATH,jsonObj.getString(WebHttpBodyConstant.FILE_DOWNLOAD_PATH));
		}
		return obj;
	}

	/**
	 * 文件上传
	 * 如果没有指定body,则赋值Optional.empty()给requestBody,跳过NotNull验证
	 * @param httpServletResponse
	 * @param body
	 * @param list
	 * @return
	 */
	public Object doFileService(HttpServletResponse httpServletResponse,Map<String,Object> body, List<FileUploadEntity> list) {
		checkRequestParams(httpServletResponse,body);
		WebHttpRequest webHttpRequest =BeanMapUtil.mapToBean(body,WebHttpRequest.class);
		webHttpRequest.setFileUploadEntities(list);
		validation(httpServletResponse,webHttpRequest);
		return WebServletRoutor.build().doService(httpServletResponse,webHttpRequest);
	}

	/**
	 * 对参数进行注解校验
	 * @param dto
	 */
	private void validation(HttpServletResponse httpServletResponse,Object dto) {
		Optional.ofNullable(dto).ifPresent(d ->{
			ValidationResultEntity result = ValidationUtils.validateEntity(dto);
			if (result.isHasErrors()) {
				ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
						MessageCodeUtils.format(DomainApplicationSysCode.V0000010001,result.getErrorMsg())).build();
				ResponseEntity res = ResponseEntity.build().addData(errorVO).failure();
				httpServletResponse.send(res);
				throw new ParamsValidationExcetion(result.getErrorMsg());
			}
		});
	}

	private void checkRequestParams(HttpServletResponse httpServletResponse,Map<String,Object> body){
		if (!Optional.ofNullable(body).isPresent() || body.size() <= 0){
			ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
					MessageCodeUtils.format(DomainApplicationSysCode.A0300000004)).build();
			ResponseEntity res = ResponseEntity.build().addData(errorVO).failure();
			httpServletResponse.send(res);
			throw new DomainOperatorException(DomainApplicationSysCode.A0300000004);
		}
	}


}
