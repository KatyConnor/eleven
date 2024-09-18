package com.hx.bytebuddy.aop.testdemo;

import com.hx.bytebuddy.aop.creator.ByteBuddyCreator;
import com.hx.bytebuddy.aop.util.ProxyUtil;

public class Main1 {

	public static void main(String args[]){
		//测试
		DataProducer dataProducer = ProxyUtil.getInstance(ByteBuddyCreator.class)
				.createInterceptorProxy(DataProducer.class,new ByteBuddyMethodInterceptor());
		dataProducer.create(null);
	}


}
