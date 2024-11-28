package hx.nine.eleven.domain.conver;

import com.fasterxml.jackson.core.JsonProcessingException;
import hx.nine.eleven.commons.json.convert.FieldConvert;
import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.commons.utils.JSONObjectMapper;
import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import hx.nine.eleven.domain.obj.form.HeaderForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * requestHeader 转换
 * @auth wml
 * @date 2024/11/28
 */
public class WebRequestHeaderFieldConvert extends FieldConvert {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebRequestHeaderFieldConvert.class);

	@Override
	public Object convert(Object obj) {
		if (!Optional.ofNullable(obj).isPresent()) {
			LOGGER.warn("[{}] is null", obj);
			return null;
		}
		Map<String, Object> headerMap = null;
		if (obj instanceof Map) {
			headerMap = (Map<String, Object>) obj;
		} else if (obj instanceof String) {
			try {
				headerMap = JSONObjectMapper.build().readValue(obj.toString(), Map.class);
			} catch (JsonProcessingException e) {
				LOGGER.error("requestHeader json 转换异常: {}", e);
			}
		} else {
			headerMap = BeanMapUtil.beanToMap(obj);
		}
		String headerCode = String.valueOf(headerMap.get(WebHttpBodyConstant.HEADER_CODE));
		return BeanConvert.convert(headerMap, null, headerCode,
				WebRouteParamsEnums.HEADER_FORM.getName());
	}
}
