package hx.nine.eleven.domain.web;

import hx.nine.eleven.core.web.http.HttpServletResponse;
import hx.nine.eleven.domain.BeanFactoryLocator;
import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.conver.BeanConvert;
import hx.nine.eleven.domain.threadevent.FileUploadThreadPoolEvent;
import hx.nine.eleven.domain.obj.dto.HeaderDTO;
import hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import hx.nine.eleven.domain.utils.MessageCodeUtils;
import hx.nine.eleven.domain.context.DomainServiceRouteSupport;
import hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import hx.nine.eleven.domain.obj.bo.BaseOrderBO;
import hx.nine.eleven.commons.entity.ValidationResultEntity;
import hx.nine.eleven.commons.utils.*;
import hx.nine.eleven.domain.context.DomainContextAware;
import hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import hx.nine.eleven.domain.request.WebHttpRequest;
import hx.nine.eleven.domain.response.ResponseEntity;
import hx.nine.eleven.domain.service.WebServiceFacade;
import hx.nine.eleven.domain.obj.vo.ErrorVO;
import hx.nine.eleven.thread.pool.executor.pool.ThreadPoolService;
import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
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
	public void doService(HttpServletResponse httpServletResponse, WebHttpRequest webHttpRequest) {
		ResponseEntity res = null;
		try {
			// 初始化上下文
			DomainContextAware.build().initDomainContext(webHttpRequest);
			HeaderDTO requestHeaderDTO = DomainContextAware.build().getDomainContext().getRequestHeaderDTO();
			// 获取交易码，交易码为空时则不进行后续操作直接退出
			String tradeCode = requestHeaderDTO.getTradeCode();
			WebServiceFacade serviceFacade = BeanFactoryLocator.getBean(tradeCode);
			if ((res = checkServiceFacade(tradeCode, serviceFacade, webHttpRequest)) != null) {
				httpServletResponse.send(res);
				return;
			}

			String subTradeCode = requestHeaderDTO.getSubTradeCode();
			// 异步处理文件,当接受到上传文件时实时同步到其他服务器
			DomainEventListenerHandlerProperties properties = ElevenApplicationContextAware
					.getProperties(DomainEventListenerHandlerProperties.class);
			if (!ObjectUtils.isEmpty(webHttpRequest.getFileUploadEntities())) {
				if (properties.getAutoSync()) {
					FileUploadThreadPoolEvent fileUploadThreadPoolEvent = new FileUploadThreadPoolEvent(
							webHttpRequest.getFileUploadEntities());
					ThreadPoolService.build().run(fileUploadThreadPoolEvent);
				} else {
					DomainContextAware.build().getDomainContext()
							.putDomainContext(WebHttpBodyConstant.FILE_UPLOAD_ENTITIES,
									webHttpRequest.getFileUploadEntities());
				}
			}

			// 是否开启 subTradeCode 子交易码匹配执行 method
			if (properties.getEnableDomainSupport() && StringUtils.isNotBlank(subTradeCode)) {
				res = (ResponseEntity) DomainServiceRouteSupport.invokDomainService(subTradeCode, serviceFacade);
			} else {
				res = serviceFacade.doService();
			}
			// 当返回的是文件流，则进行文件流返回处理
			Boolean isDownload = res.getFileStream();
			if (isDownload != null && isDownload) {
				httpServletResponse.addHeader(WebHttpBodyConstant.FILE_STREAM, String.valueOf(isDownload));
				httpServletResponse.addHeader(WebHttpBodyConstant.FILE_DOWNLOAD_PATH, res.getFileDownloadPath());
			}
			httpServletResponse.send(res);
		} catch (Throwable ex) {
			// 设置返回报文头参数
			if (!ObjectUtils.isEmpty(httpServletResponse)) {
				HeaderDTO requestHeaderDTO = DomainContextAware.build().getDomainContext().getRequestHeaderDTO();
				Object resHeader = BeanConvert.convert(requestHeaderDTO, null,
						requestHeaderDTO.getHeaderCode(), WebRouteParamsEnums.HEADER_VO.getName());
				ResponseEntity response = ResponseEntity.build().failure().setResponseHeader(resHeader);
				httpServletResponse.send(response);
			}
			throw ex;
		} finally {
			// 销毁上下文
			DomainContextAware.build().destroyContext();
		}
	}

	/**
	 * 内部调用使用
	 * 没有 domaincontext 上下文
	 * 不支持, HttpServletRequest request, HttpServletResponse response
	 *
	 * @param baseOrderBO
	 * @param <B>
	 * @return
	 */
	public <B extends BaseOrderBO> ResponseEntity route(B baseOrderBO) {
		ResponseEntity res = null;
		WebHttpRequest webHttpRequest = BeanUtils.copyProperties(baseOrderBO, WebHttpRequest.class);
		validation(webHttpRequest);
		HeaderDTO requestHeaderDTO = DomainContextAware.build().getDomainContext().getRequestHeaderDTO();
		// 获取交易码，交易码为空时则不进行后续操作直接退出
		String tradeCode = requestHeaderDTO.getTradeCode();
		if ((res = checkTradeCode(tradeCode)) != null) {
			return res;
		}
		WebServiceFacade serviceFacade = BeanFactoryLocator.getBean(tradeCode);
		if ((res = checkServiceFacade(tradeCode, serviceFacade, webHttpRequest)) != null) {
			return res;
		}
		String subTradeCode = requestHeaderDTO.getSubTradeCode();
		if (properties.getEnableDomainSupport() && StringUtils.isNotBlank(subTradeCode)) {
			res = (ResponseEntity) DomainServiceRouteSupport.invokDomainService(subTradeCode, serviceFacade);
		} else {
			res = serviceFacade.doService();
		}
		return res;
		// 调用之前进行非空验证
	}

	private ResponseEntity checkTradeCode(String tradeCode) {
		if (!Optional.ofNullable(tradeCode).isPresent()) {
			LOGGER.info("交易码 [{}] 为空", tradeCode);
			ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
					MessageCodeUtils.format(DomainApplicationSysCode.B0001000000, tradeCode)).build();
			return ResponseEntity.build().addData(errorVO).failure();
		}
		return null;
	}

	private ResponseEntity checkServiceFacade(String tradeCode, WebServiceFacade serviceFacade, WebHttpRequest webHttpRequest) {
		LOGGER.info("时间: [{}],进入[{}]交易, 交易入参：[{}]", DateUtils.getTimeStampAsString(), tradeCode, webHttpRequest);
		if (!Optional.ofNullable(serviceFacade).isPresent()) {
			LOGGER.info("不支持 [{}] 交易", tradeCode);
			ErrorVO errorVO = Builder.of(ErrorVO::new).with(ErrorVO::setErrorMessageCode,
					MessageCodeUtils.format(DomainApplicationSysCode.A0300000005, tradeCode)).build();
			return ResponseEntity.build().addData(errorVO).failure();
		}
		return null;
	}

	private void validation(Object obj) {
		Optional.ofNullable(obj).ifPresent(d -> {
			ValidationResultEntity result = ValidationUtils.validateEntity(obj);
			if (result.isHasErrors()) {
				throw new ParamsValidationExcetion(result.getErrorMsg());
			}
		});
	}

	public static WebServletRoutor build() {
		return ElevenApplicationContextAware.getBean(WebServletRoutor.class);
	}
}
