package com.hx.domain.framework.context;

import com.hx.domain.framework.constant.WebHttpBodyConstant;
import com.hx.lang.commons.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

public class DomainContextAsynRequestBuilder {
	private final static Logger LOGGER = LoggerFactory.getLogger(DomainContextAsynRequestBuilder.class);

	private Map<String, Object> domainContext;

	private DomainContextAsynRequestBuilder(){
		domainContext = new HashMap<>();
	}

	public DomainContextAsynRequestBuilder putRequestHeader(String key,Object value){
		Object header = this.domainContext.get(WebHttpBodyConstant.ASYN_HEADER_DTO);
		if (ObjectUtils.isEmpty(header)){
			Map<String,Object> headerMap = new HashMap<>();
			headerMap.put(key,value);
			this.domainContext.put(WebHttpBodyConstant.ASYN_HEADER_DTO,headerMap);
			return this;
		}
		if (header instanceof Map){
			Map<String,Object> headerMap = (Map)header;
			headerMap.put(key,value);
			this.domainContext.put(WebHttpBodyConstant.ASYN_HEADER_DTO,headerMap);
		}else {
			throw new RuntimeException("当前存在body对象非Map类型，不能使用本方法设置属性值");
		}
		return this;
	}

	public DomainContextAsynRequestBuilder putRequestBody(String key,Object value){
		Object body = this.domainContext.get(WebHttpBodyConstant.ASYN_BODY_DTO);
		if (ObjectUtils.isEmpty(body)){
			Map<String,Object> bodyMap = new HashMap<>();
			bodyMap.put(key,value);
			this.domainContext.put(WebHttpBodyConstant.ASYN_BODY_DTO,bodyMap);
			return this;
		}
		if (body instanceof Map){
			Map<String,Object> bodyMap = (Map)body;
			bodyMap.put(key,value);
			this.domainContext.put(WebHttpBodyConstant.ASYN_BODY_DTO,bodyMap);
		}else {
			throw new RuntimeException("当前存在body对象非Map类型，不能使用本方法设置属性值");
		}
		return this;
	}

	public DomainContextAsynRequestBuilder buildRequestHeader(Object requestHeader){
		if (ObjectUtils.isEmpty(requestHeader)){
			LOGGER.warn("传入参数为null");
			return this;
		}
		this.domainContext.put(WebHttpBodyConstant.ASYN_HEADER_DTO,requestHeader);
		return this;
	}

	public DomainContextAsynRequestBuilder buildRequestBody(Object requestBody){
		if (ObjectUtils.isEmpty(requestBody)){
			LOGGER.warn("传入参数为null");
			return this;
		}
		this.domainContext.put(WebHttpBodyConstant.ASYN_BODY_DTO,requestBody);
		return this;
	}

	public Map<String, Object> getDomainContext(){
		return this.domainContext;
	}

	public static DomainContextAsynRequestBuilder build(){
		return new DomainContextAsynRequestBuilder();
	}
}
