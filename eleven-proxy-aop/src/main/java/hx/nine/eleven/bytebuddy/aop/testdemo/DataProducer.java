package hx.nine.eleven.bytebuddy.aop.testdemo;

import hx.nine.eleven.bytebuddy.aop.annotation.HXDataSource;

public class DataProducer{


	@HXDataSource("ds")
	public void create(String data) {
		if (data == null){
			throw new IllegalArgumentException("Data cannot be null");
		}
		System.out.println("create data:" + data);
	}
}
