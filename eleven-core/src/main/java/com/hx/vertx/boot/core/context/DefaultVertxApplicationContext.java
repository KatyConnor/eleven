package com.hx.vertx.boot.core.context;

import com.hx.vertx.boot.env.Environment;
import com.hx.vertx.boot.env.ApplicationEnvConfigProperty;
import com.hx.vertx.boot.exception.VertxApplicationRunException;

import java.lang.annotation.Annotation;
import java.util.Set;

public class DefaultVertxApplicationContext implements HXVertxApplicationContext {

    @Override
    public DefaultVertxApplicationContext addBean(Object value) {
        ApplicationContextContainer.build().addBean(value);
        return this;
    }

    @Override
    public DefaultVertxApplicationContext addBean(String beanName, Object value) {
        ApplicationContextContainer.build().addBean(beanName,value);
        return this;
    }

    @Override
    public DefaultVertxApplicationContext addBean(String beanName, Object value, boolean override) {
        ApplicationContextContainer.build().addBean(beanName,value,override);
        return this;
    }

    @Override
    public <A extends Annotation> A findAnnotationOnBean(String beanName, Class<A> aClass) {
        Object bean = this.getBean(beanName);
        return bean==null?null:bean.getClass().getAnnotation(aClass);
    }

    @Override
    public Object getBean(String name) {
        return ApplicationContextContainer.build().getBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> aClass) {
        Object obj = ApplicationContextContainer.build().getBean(name);
        if (aClass.isInstance(obj)){
           return  (T) obj;
        }
        throw new VertxApplicationRunException("{} 和 {} 类型不匹配",obj.getClass().getName(),aClass.getName());
    }

    @Override
    public <T> T getBean(Class<T> aClass) {
        T obj = ApplicationContextContainer.build().getBean(aClass);
        if (obj == null){
            obj = ApplicationContextContainer.build().getProperties(aClass);
        }
        if (obj == null){
            obj = ApplicationContextContainer.build().getSubTypesOfBean(aClass);
        }
        return obj;
    }

    @Override
    public <T> Set<Class<? extends T>> getSubTypesOfBeanClass(Class<T> tClass) {
        return ApplicationContextContainer.build().getSubTypesOfBeanClass(tClass);
    }

    @Override
    public <T> Set<T> getSubTypesOfBeans(Class<T> tClass) {
        return ApplicationContextContainer.build().getSubTypesOfBeans(tClass);
    }

    @Override
    public <T> T getSubTypesOfBean(Class<T> tClass) {
        return ApplicationContextContainer.build().getSubTypesOfBean(tClass);
    }

    @Override
    public <T extends HXVertxApplicationContext, R> T addSubTypesOfBeanClass(Class<R> tClass, Set<Class<? extends R>> subTypesOfBeanClass) {
        ApplicationContextContainer.build().addSubTypesOfBeanClass(tClass,subTypesOfBeanClass);
        return (T)this;
    }

    @Override
    public <T extends HXVertxApplicationContext, R> T addSubTypesOfBean(Class<R> tClass, Set<R> subTypesOfBean) {
        ApplicationContextContainer.build().addSubTypesOfBean(tClass,subTypesOfBean);
        return (T)this;
    }

    @Override
    public <T extends HXVertxApplicationContext, R> T addSubTypesOfBean(R subTypesOfBean) {
        ApplicationContextContainer.build().addSubTypesOfBean(subTypesOfBean);
        return (T)this;
    }

    @Override
    public boolean containsBean(String bean) {
        return ApplicationContextContainer.build().containsBean(bean);
    }

    @Override
    public ApplicationEnvConfigProperty getProperties() {
        return ApplicationContextContainer.build().getProperties();
    }

    @Override
    public <T> T getProperties(Class<T> properties) {
        return ApplicationContextContainer.build().getProperties(properties);
    }

    @Override
    public Environment getEnvironment() {
        return null;
    }

    public static DefaultVertxApplicationContext build(){
        return Sigle.INSTANCE;
    }

    private final static class Sigle{
        private final static DefaultVertxApplicationContext INSTANCE = new DefaultVertxApplicationContext();
    }


}
