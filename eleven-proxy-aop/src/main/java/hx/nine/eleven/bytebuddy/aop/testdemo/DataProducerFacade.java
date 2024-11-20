package hx.nine.eleven.bytebuddy.aop.testdemo;

import hx.nine.eleven.bytebuddy.aop.annotation.HXDataSource;

public interface DataProducerFacade {

    @HXDataSource("dsJdk")
    void createTest(String data);
}
