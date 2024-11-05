package com.hx.nine.eleven.bytebuddy.aop.testdemo;

public class DataProducer {

	public void create(String data) {
		if (data == null){
			throw new IllegalArgumentException("Data cannot be null");
		}
		System.out.println("create data:" + data);
	}
}
