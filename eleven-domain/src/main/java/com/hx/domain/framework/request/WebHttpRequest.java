package com.hx.domain.framework.request;

import com.hx.domain.framework.constant.WebHttpBodyConstant;
import com.hx.domain.framework.enums.WebRouteParamsEnums;
import com.hx.lang.commons.utils.JSONObjectMapper;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.domain.framework.conver.BeanConvert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.vertx.boot.core.entity.FileUploadEntity;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * HTTP 报文传输数据
 *
 * @author wml
 * @date 2022-10-28
 */
public class WebHttpRequest implements Serializable {

	private final static Logger LOGGER = LoggerFactory.getLogger(WebHttpRequest.class);

	/**
	 * 请求报文头（header）
	 */
	@NotNull(message = "请求头[header]不能为空")
	private Object requestHeader;
	/**
	 * 请求报文体（body）
	 */
//	@NotNull(message = "报文体[body]不能为空")
	private Object requestBody;

	/**
	 * 上传文件列表
	 */
	private List<FileUploadEntity> fileUploadEntities;
	/**
	 * 数据流
	 */
	private InputStream inputStream;

	private Boolean openFiber = false;

	public Object getRequestHeader() {
		return requestHeader;
	}

	public void setRequestHeader(Object requestHeader) {
		if (!Optional.ofNullable(requestHeader).isPresent()) {
            LOGGER.warn("[{}] is null",requestHeader);
            return;
		}
		Map<String, Object> headerMap = null;
		if (requestHeader instanceof Map) {
			headerMap = (Map<String, Object>) requestHeader;
		} else {
			try {
				String headerStr = requestHeader instanceof String ? requestHeader.toString() : JSONObjectMapper.build().toJsonString(requestHeader);
				headerMap = JSONObjectMapper.build().readValue(headerStr, Map.class);
			} catch (JsonProcessingException e) {
				LOGGER.error("requestHeader json 转换异常: {}", e);
			}
		}
		String tradeCode = String.valueOf(headerMap.get(WebHttpBodyConstant.TRADE_CODE));
		String subTradeCode = String.valueOf(headerMap.get(WebHttpBodyConstant.SUB_TRADE_CODE));
		String headerCode = String.valueOf(headerMap.get(WebHttpBodyConstant.HEADER_CODE));
		this.requestHeader = BeanConvert.convert(headerMap, StringUtils.append(tradeCode,subTradeCode), headerCode, WebRouteParamsEnums.HEADER_FORM.getName());
	}

	public Object getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(Object requestBody) {
        if (!Optional.ofNullable(requestBody).isPresent()) {
            LOGGER.warn("[{}] is null",requestBody);
            return;
        }
		Map<String, Object> bodyMap = requestBody instanceof Map?(Map<String, Object>) requestBody:null;
		if (!Optional.ofNullable(bodyMap).isPresent()) {
			try {
				String bodyStr = requestBody instanceof String?requestBody.toString():JSONObjectMapper.build().
						toJsonString(requestBody);
				bodyMap = JSONObjectMapper.build().readValue(bodyStr, Map.class);
			} catch (JsonProcessingException e) {
				LOGGER.error("requestBody json 转换异常：{}", e);
			}
		}

		Object tradeCode = bodyMap.get(WebHttpBodyConstant.TRADE_CODE);
		if (!Optional.ofNullable(tradeCode).isPresent()){
			tradeCode = JsonObject.mapFrom(this.requestHeader).getString(WebHttpBodyConstant.TRADE_CODE);
		}
		tradeCode = Optional.ofNullable(tradeCode).isPresent()?tradeCode:"";
		Object subTradeCode = bodyMap.get(WebHttpBodyConstant.SUB_TRADE_CODE);
		if (!Optional.ofNullable(subTradeCode).isPresent()){
			subTradeCode = JsonObject.mapFrom(this.requestHeader).getString(WebHttpBodyConstant.SUB_TRADE_CODE);
		}
		subTradeCode = Optional.ofNullable(subTradeCode).isPresent()?subTradeCode:"";
		this.requestBody = BeanConvert.convert(bodyMap, StringUtils.append(tradeCode.toString(),subTradeCode.toString()), null, WebRouteParamsEnums.BODY_FORM.getName());
	}

	public List<FileUploadEntity> getFileUploadEntities() {
		return fileUploadEntities;
	}

	public void setFileUploadEntities(List<FileUploadEntity> fileUploadEntities) {
		this.fileUploadEntities = fileUploadEntities;
		if (!Optional.ofNullable(this.requestBody).isPresent() &&
				Optional.ofNullable(this.fileUploadEntities).isPresent() &&
				this.fileUploadEntities.size() > 0){
			this.requestBody = Optional.empty();
		}
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public Boolean getOpenFiber() {
		return openFiber;
	}

	public void setOpenFiber(Boolean openFiber) {
		this.openFiber = openFiber;
	}

	@Override
	public String toString() {
		return ObjectUtils.toString(this);
	}
}
