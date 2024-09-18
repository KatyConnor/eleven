package com.hx.domain.framework.entity;

public class RouterMethodEntity {

	private String subTradeCode;
	private String mapperFactoryMethod;
	private String methodName;

	public String getSubTradeCode() {
		return subTradeCode;
	}

	public RouterMethodEntity setSubTradeCode(String subTradeCode) {
		this.subTradeCode = subTradeCode;
		return this;
	}

	public String getMapperFactoryMethod() {
		return mapperFactoryMethod;
	}

	public RouterMethodEntity setMapperFactoryMethod(String mapperFactoryMethod) {
		this.mapperFactoryMethod = mapperFactoryMethod;
		return this;
	}

	public String getMethodName() {
		return methodName;
	}

	public RouterMethodEntity setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}
}
