package com.hx.nine.eleven.bytebuddy.aop.testdemo;

import com.hx.nine.eleven.bytebuddy.aop.DynamicObjectProxy;
import com.hx.nine.eleven.bytebuddy.aop.creator.ByteBuddyCreator;
import com.hx.nine.eleven.bytebuddy.aop.creator.JdkProxyCreator;
import com.hx.nine.eleven.bytebuddy.aop.exception.ProxyCreatorException;
import com.hx.nine.eleven.bytebuddy.aop.util.ProxyUtil;

public class Main1 {

	public static void main(String args[]) throws ProxyCreatorException {
		//1、测试
		DataProducerExd dataProducer = ProxyUtil.getInstance(ByteBuddyCreator.class)
				.createInterceptorProxy(DataProducerExd.class,new ByteBuddyMethodInterceptor());
		dataProducer.create("测试");


		//2、jdk代理
		DataProducerExd dataProducerExdProx = ProxyUtil.getInstance(JdkProxyCreator.class)
				.createInterceptorProxy(DataProducerExd.class,new ByteBuddyMethodInterceptor());
		dataProducer.create("测试");
	}


}
