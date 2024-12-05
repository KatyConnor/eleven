package hx.nine.eleven.domain.web;

import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.commons.utils.CollectionUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.auth.UserAuthResponseBody;
import hx.nine.eleven.core.auth.UserAuthenticateProvider;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.entity.FileUploadEntity;
import hx.nine.eleven.core.env.Environment;
import hx.nine.eleven.core.web.DomainRouter;
import hx.nine.eleven.core.web.http.HttpResponse;
import hx.nine.eleven.core.web.http.HttpServletRequest;
import hx.nine.eleven.core.web.http.HttpServletResponse;
import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.exception.DomainOperatorException;
import hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import hx.nine.eleven.domain.obj.vo.ErrorVO;
import hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import hx.nine.eleven.domain.request.WebHttpRequest;
import hx.nine.eleven.domain.response.ResponseEntity;
import hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import hx.nine.eleven.domain.utils.MessageCodeUtils;
import hx.nine.eleven.commons.entity.ValidationResultEntity;
import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.commons.utils.ValidationUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * http request请求处理
 * @author wml
 * @date 2022-10-28
 */
public class WebRoutorServiceHandler implements DomainRouter {

	@Override
	public void route(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		Map<String,Object> body = httpServletRequest.getBody();
		List<FileUploadEntity> list = httpServletRequest.getFileUploadEntities();
		checkRequestParams(httpServletResponse,body);
		WebHttpRequest webHttpRequest = BeanMapUtil.mapToBean(body,WebHttpRequest.class);
		if (webHttpRequest.getRequestBody() == null){
			webHttpRequest.setRequestBody(body.get(WebHttpBodyConstant.REQUEST_BODY));
		}
		validation(httpServletResponse,webHttpRequest);
		if (CollectionUtils.isNotEmpty(list)){
			//文件上传,如果没有指定body,则赋值Optional.empty()给requestBody,跳过NotNull验证
			webHttpRequest.setFileUploadEntities(list);
		}
		DomainEventListenerHandlerProperties properties = ElevenApplicationContextAware.getProperties(DomainEventListenerHandlerProperties.class);
		boolean isLogin = false;
		if (!httpServletRequest.getAuthenticate()){
			String tradeCode = webHttpRequest.getRequestHeader().getTradeCode();
			String subTradeCode = webHttpRequest.getRequestHeader().getSubTradeCode();
			if (StringUtils.isEmpty(tradeCode)){
				ResponseEntity res = ResponseEntity.build().failure().addMessage("主交易码不能为空");
				httpServletResponse.send(res);
				return;
			}
			if (!tradeCode.equals(StringUtils.valueOf(properties.getLoginTradeCode()))){
				ResponseEntity res = ResponseEntity.build().failure().addMessage("该用户没有权限，请登录后操作");
				httpServletResponse.send(res);
				return;
			}

			if (StringUtils.isNotEmpty(properties.getLoginSubTradeCode()) && StringUtils.isEmpty(subTradeCode)){
				ResponseEntity res = ResponseEntity.build().failure().addMessage("该用户没有权限，请登录后操作");
				httpServletResponse.send(res);
				return;
			}

			if (StringUtils.isNotEmpty(subTradeCode) &&
					!subTradeCode.equals(StringUtils.valueOf(properties.getLoginSubTradeCode()))){
				ResponseEntity res = ResponseEntity.build().failure().addMessage("该用户没有权限，请登录后操作");
				httpServletResponse.send(res);
				return;
			}
			isLogin = true;
		}

		WebServletRoutor.build().doService(httpServletResponse,webHttpRequest);
		if (isLogin){
			HttpResponse response = httpServletResponse.httpResponse();
			ResponseEntity responseEntity = (ResponseEntity)response.getBody();
			Object user = responseEntity.getHttpResponseBody().getResponseBody().getData();
			Environment env = ElevenApplicationContextAware.getEnvironment();
			int expires = env.getIntProperty("eleven.boot.expires");
			UserAuthenticateProvider provider = ElevenApplicationContextAware.getSubTypesOfBean(UserAuthenticateProvider.class);
			String token = provider.generateToken(user,expires);
			// 1、默认设置token到header
			httpServletResponse.addHeader(ConstantType.AUTH_TOKEN,token);
			// 1、设置token到 body->data 中，需要用户自定义实现
			UserAuthResponseBody userAuthResponseBody = ElevenApplicationContextAware.getBean(UserAuthResponseBody.class);
			userAuthResponseBody.addAuthorizedTokenToBody(token,user);
		}
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
