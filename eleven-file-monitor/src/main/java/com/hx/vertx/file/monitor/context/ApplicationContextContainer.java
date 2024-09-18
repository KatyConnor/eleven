package com.hx.vertx.file.monitor.context;

import com.hx.vertx.file.monitor.exception.ApplicationInitialzerException;
import com.hx.vertx.file.monitor.properties.ApplicationConfigProperties;
import org.jooq.impl.DefaultDSLContext;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储实例化bean容器
 * @author wml
 * @date 2023-03-09
 */
public class ApplicationContextContainer {

    private final static ConcurrentHashMap<String,Object> beanContainer = new ConcurrentHashMap<>();

    public void addBean(Object value){
        if (beanContainer.containsKey(value.getClass().getName())){
            throw new ApplicationInitialzerException("容器已经注册[{}]对象,需要保存注册对象唯一",value.getClass().getName());
        }
        beanContainer.put(value.getClass().getName(), value);
    }

    public void addBean(String beanName,Object value){
        if (beanContainer.containsKey(beanName)){
            throw new ApplicationInitialzerException("容器已经注册[{}]对象,需要保存注册对象唯一",beanName);
        }
        beanContainer.put(beanName, value);
    }

    public Object getBean(String name){
        return beanContainer.get(name);
    }

    public <T> T getBean(Class<T> className){
        return (T)beanContainer.get(className.getName());
    }

    public ApplicationConfigProperties getProperties(){
      return ApplicationContextContainer.build().getBean(ApplicationConfigProperties.class);
    }

  public <T> T getProperties(Class<T> properties){
    return ApplicationContextContainer.build().getBean(ApplicationConfigProperties.class).getProperties(properties);
  }

  public DefaultDSLContext getDSLContext() {
    return ApplicationContextContainer.build().getBean(DefaultDSLContext.class);
  }
    private ApplicationContextContainer(){

    }

    public static ApplicationContextContainer build(){
        return Single.NEWSTANCE;
    }

    private static final class Single{
        private static final ApplicationContextContainer NEWSTANCE = new ApplicationContextContainer();
    }
}
