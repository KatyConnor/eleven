package hx.nine.eleven.domain.conver;

import hx.nine.eleven.commons.json.convert.FieldConvert;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * @auth wml
 * @date 2024/11/28
 */
public class WebRequestBodyFieldConvert extends FieldConvert {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebRequestBodyFieldConvert.class);

	@Override
	public Object convert(Object obj) {
		if (!Optional.ofNullable(obj).isPresent()) {
			LOGGER.warn("[{}] is null", obj);
			return null;
		}
//
//		Object tradeCode = this.requestHeader.getTradeCode();
//		Object subTradeCode = this.requestHeader.getSubTradeCode();
//		tradeCode = Optional.ofNullable(tradeCode).isPresent() ? tradeCode : "";
//		subTradeCode = Optional.ofNullable(subTradeCode).isPresent() ? subTradeCode : "";
//		return BeanConvert.convert(requestBody, StringUtils.append(tradeCode.toString(), subTradeCode.toString()),
//				null, WebRouteParamsEnums.BODY_FORM.getName());
		return null;
	}
}
