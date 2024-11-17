package com.hx.nine.eleven.bytebuddy.aop.testdemo;

import com.hx.nine.eleven.bytebuddy.aop.annotation.HXDataSource;

public class JdkProducer implements DataProducerFacade{

    @HXDataSource("dsJdk")
    @Override
    public void createTest(String data) {
        if (data == null){
            throw new IllegalArgumentException("Data cannot be null");
        }
        System.out.println("create data:" + data);
    }
}
