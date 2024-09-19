package com.hx.nine.eleven.domain.web;

import com.hx.nine.eleven.domain.BeanFactoryLocator;
import com.hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import com.hx.nine.eleven.domain.conver.BeanConvert;
import com.hx.nine.eleven.domain.threadevent.FileUploadThreadPoolEvent;
import com.hx.nine.eleven.domain.obj.dto.HeaderDTO;
import com.hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import com.hx.nine.eleven.domain.utils.MessageCodeUtils;
import com.hx.nine.eleven.domain.context.DomainServiceRouteSupport;
import com.hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import com.hx.nine.eleven.domain.obj.bo.BaseOrderBO;
import com.hx.nine.eleven.commons.entity.ValidationResultEntity;
import com.hx.nine.eleven.commons.utils.*;
import com.hx.nine.eleven.domain.context.DomainContextAware;
import com.hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import com.hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import com.hx.nine.eleven.domain.request.WebHttpRequest;
import com.hx.nine.eleven.domain.response.ResponseEntity;
import com.hx.nine.eleven.domain.service.WebServiceFacade;
import com.hx.nine.eleven.domain.obj.vo.ErrorVO;
import com.hx.thread.pool.executor.pool.ThreadPoolService;
import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.VertxApplicationContextAware;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Optional;

@Component
public class WebServletRoutor {

	@Resource
	private DomainEventListenerHandlerProperties properties;

	private final static Logger LOGGER = LoggerFactory.getLogger(WebServletRoutor.class);

	/**
	 * HttpServletRequest request, HttpServletResponse response
	 *
	 * @param webHttpRequest
	 * @return
	 */
	public ResponseEntity doService(RoutingContext context, WebHttpRequest webHttpRequest) {
		ResponseEntity res = null;
		try {
			// 初始化上下文
			DomainContextAware.build().initDomainContext(webHttpRequest);
			HeaderDTO requestHeaderDTO = DomainContextAware.build().getDomainContext().getRequestHeaderDTO();
			// 获取交易码，交易码为空时则不进行后续操作直接退出
			String tradeCode = requestHeaderDTO.getTradeCode();
			if (!Optional.ofNullable(tradeCode).isPresent()) {
				LOGGER.info("交易码 [{}] 为空", tradeCode);
				ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
						MessageCodeUtils.format(DomainApplicationSysCode.B0001000000, tradeCode)).build();
				return ResponseEntity.build().addData(errorVO).failure();
			}

			LOGGER.info("时间: [{}],进入[{}]交易, 交易入参：[{}]", DateUtils.getTimeStampAsString(), tradeCode, webHttpRequest);
			WebServiceFacade serviceFacade = BeanFactoryLocator.getBean(tradeCode);
			if (!Optional.ofNullable(serviceFacade).isPresent()) {
				LOGGER.info("不支持 [{}] 交易", tradeCode);
				ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
						MessageCodeUtils.format(DomainApplicationSysCode.A0300000005, tradeCode)).build();
				return ResponseEntity.build().addData(errorVO).failure();
			}

			String subTradeCode = requestHeaderDTO.getSubTradeCode();
			// 异步处理文件,当接受到上传文件时实时同步到其他服务器
			DomainEventListenerHandlerProperties properties = VertxApplicationContextAware.getProperties(DomainEventListenerHandlerProperties.class);
			if (!ObjectUtils.isEmpty(webHttpRequest.getFileUploadEntities()) && properties.getAutoSync()){
				FileUploadThreadPoolEvent fileUploadThreadPoolEvent = new FileUploadThreadPoolEvent(webHttpRequest.getFileUploadEntities());
				ThreadPoolService.build().run(fileUploadThreadPoolEvent);
			}else if (!ObjectUtils.isEmpty(webHttpRequest.getFileUploadEntities())){
				DomainContextAware.build().getDomainContext().putDomainContext(WebHttpBodyConstant.FILE_UPLOAD_ENTITIES,webHttpRequest.getFileUploadEntities());
			}
			if (properties.getEnableDomainSupport() && StringUtils.isNotBlank(subTradeCode)) {
				res = (ResponseEntity) DomainServiceRouteSupport.invokDomainService(subTradeCode, serviceFacade);
			} else {
				res = serviceFacade.doService();
			}
		} catch (Throwable ex) {
			// 设置返回报文头参数
			if (!ObjectUtils.isEmpty(context)){
				HeaderDTO requestHeaderDTO = DomainContextAware.build().getDomainContext().getRequestHeaderDTO();
				Object resHeader = BeanConvert.convert(requestHeaderDTO, StringUtils.append(requestHeaderDTO.getTradeCode(),
						requestHeaderDTO.getSubTradeCode()), requestHeaderDTO.getHeaderCode(), WebRouteParamsEnums.HEADER_VO.getName());
				ResponseEntity response = ResponseEntity.build().failure().setResponseHeader(resHeader);
				context.put(ConstantType.RESPONSE_BODY,response);
			}
			throw ex;
		} finally {
			// 销毁上下文
			DomainContextAware.build().destroyContext();
		}
		return res;
	}

	/**
	 * 没有domaincontext上下文
	 * 不支持, HttpServletRequest request, HttpServletResponse response
	 *
	 * @param baseOrderBO
	 * @param <B>
	 * @return
	 */
	public <B extends BaseOrderBO> ResponseEntity route(B baseOrderBO) {
		WebHttpRequest webHttpRequest = BeanUtils.copyProperties(baseOrderBO, WebHttpRequest.class);
		validation(webHttpRequest);
		return this.doService(null,webHttpRequest);
//		StringBuilder tradeStr = new StringBuilder(WebRouteParamsEnums.HEADER_FORM.getName());
//		tradeStr.append(baseOrderBO.getTradeCode());
//		tradeStr.append(baseOrderBO.getSubTradeCode());
//		Class<?> formClasszz = DomainApplicationContainer.build().getClass(tradeStr.toString());
//		if (!Optional.ofNullable(formClasszz).isPresent()) {
//			//不能为空
//		}
//		Object headerForm = convertObject(baseOrderBO, formClasszz);
//		WebHttpRequest webHttpRequest = Builder.of(WebHttpRequest::new).with(WebHttpRequest::setRequestHeader, headerForm).build();
//
//		tradeStr.delete(0, tradeStr.length());
//		tradeStr.append(WebRouteParamsEnums.BODY_FORM.getName());
//		tradeStr.append(baseOrderBO.getTradeCode());
//		tradeStr.append(baseOrderBO.getSubTradeCode());
//		Class<?> bodyClasszz = DomainApplicationContainer.build().getClass(tradeStr.toString());
//		if (Optional.ofNullable(bodyClasszz).isPresent()) {
//
//		}
//
//		Object bodyForm = convertObject(baseOrderBO, bodyClasszz);
//		webHttpRequest.setRequestBody(bodyForm);
		// 调用之前进行非空验证
	}

//	private <B extends BaseOrderBO> Object convertObject(B baseOrderBO, Class<?> classzz) {
//		Object instance = BeanUtils.newInstance(classzz);
//		return BeanUtils.copyProperties(baseOrderBO, instance);
//	}

	private void validation(Object obj) {
		Optional.ofNullable(obj).ifPresent(d -> {
			ValidationResultEntity result = ValidationUtils.validateEntity(obj);
			if (result.isHasErrors()) {
				throw new ParamsValidationExcetion(result.getErrorMsg());
			}
		});
	}

	public static WebServletRoutor build() {
		return VertxApplicationContextAware.getBean(WebServletRoutor.class);
	}
}
