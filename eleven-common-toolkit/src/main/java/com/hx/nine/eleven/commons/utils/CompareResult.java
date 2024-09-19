package com.hx.nine.eleven.commons.utils;

public class CompareResult {
	/**字段名**/
	private String beanField;
	/**原值**/
	private String orginValue;
	
	private String value;
	/**字段中文名**/
	private String desc;

	public String getBeanField() {
		return beanField;
	}

	public void setBeanField(String beanField) {
		this.beanField = beanField;
	}

	public String getOrginValue() {
		return orginValue;
	}

	public void setOrginValue(String orginValue) {
		this.orginValue = orginValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "CompareResult [beanField=" + beanField + ", orginValue=" + orginValue + ", value=" + value + "]";
	}
	
	
	
	

}
