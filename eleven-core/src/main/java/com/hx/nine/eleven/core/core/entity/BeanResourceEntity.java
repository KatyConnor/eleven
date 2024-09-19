package com.hx.nine.eleven.core.core.entity;

import java.lang.reflect.Field;

public class BeanResourceEntity {
	private Field field;
	private String obj;

	public BeanResourceEntity() {
	}

	public BeanResourceEntity(Field field, String obj) {
		this.field = field;
		this.obj = obj;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}
}
