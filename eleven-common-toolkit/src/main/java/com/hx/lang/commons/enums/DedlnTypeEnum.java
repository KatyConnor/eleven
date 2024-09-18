package com.hx.lang.commons.enums;

public enum DedlnTypeEnum {

	DEDLN_TYPE_000("000","日"),
	DEDLN_TYPE_001("001","月"),
	DEDLN_TYPE_002("002","年");

	private DedlnTypeEnum(String code, String type) {
		this.code = code;
		this.type =  type;
	}
	
	private String code;
	private String type;
	
	public String getCode() {
		return code;
	}
	
	public String getType() {
		return type;
	}
}
