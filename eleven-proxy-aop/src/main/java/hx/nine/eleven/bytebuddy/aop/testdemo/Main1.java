package hx.nine.eleven.bytebuddy.aop.testdemo;

import hx.nine.eleven.bytebuddy.aop.creator.ByteBuddyCreator;
import hx.nine.eleven.bytebuddy.aop.creator.CglibProxyCreator;
import hx.nine.eleven.bytebuddy.aop.creator.JdkProxyCreator;
import hx.nine.eleven.bytebuddy.aop.exception.ProxyCreatorException;
import hx.nine.eleven.bytebuddy.aop.util.ProxyUtil;

public class Main1 {

	public static void main(String args[]) throws ProxyCreatorException {
		//1、测试
		byteBuddyProxy();
		System.out.println("-------------------------------------------");
		//2、jdk代理
		jdkProxy();
		System.out.println("-------------------------------------------");
		//3、cglib代理
		cglibProxy();

	}

	public static void byteBuddyProxy(){
		DataProducerExd dataProducer = ProxyUtil.getInstance(ByteBuddyCreator.class)
				.createInterceptorProxy(DataProducerExd.class,new ByteBuddyMethodInterceptor());
		dataProducer.create("测试");
	}

	public static void jdkProxy(){
		JdkMethodInterceptor methodInterceptor = new JdkMethodInterceptor();
		DataProducerFacade dataProducerExdProx = ProxyUtil.getInstance(JdkProxyCreator.class)
				 .createInterceptorProxy(JdkProducer.class,methodInterceptor);
		dataProducerExdProx.createTest("测试");
	}




	public static void cglibProxy(){
		CglibAOPMethodInterceptor methodInterceptor = new CglibAOPMethodInterceptor();
		DataProducerExd dataProducerExdProx = ProxyUtil.getInstance(CglibProxyCreator.class)
				.createInterceptorProxy(DataProducerExd.class,methodInterceptor);
		dataProducerExdProx.create("测试");
	}


}
