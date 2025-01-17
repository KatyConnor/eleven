package hx.nine.eleven.domain.web;

import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.commons.utils.CollectionUtils;
import hx.nine.eleven.core.entity.FileUploadEntity;
import hx.nine.eleven.core.web.DomainRouter;
import hx.nine.eleven.core.web.http.HttpServletRequest;
import hx.nine.eleven.core.web.http.HttpServletResponse;
import hx.nine.eleven.domain.exception.DomainOperatorException;
import hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import hx.nine.eleven.domain.obj.vo.ErrorVO;
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
		if (CollectionUtils.isEmpty(list)){
			//文件上传,如果没有指定body,则赋值Optional.empty()给requestBody,跳过NotNull验证
			webHttpRequest.setFileUploadEntities(list);
		}
		validation(httpServletResponse,webHttpRequest);
		WebServletRoutor.build().doService(httpServletResponse,webHttpRequest);
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
