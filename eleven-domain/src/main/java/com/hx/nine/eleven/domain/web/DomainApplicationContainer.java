package com.hx.nine.eleven.domain.web;

import com.hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Domain容器
 * @author wml
 * @date 2022-03-10
 */
public class DomainApplicationContainer {
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainApplicationContainer.class);

    private static final ConcurrentMap<String, Object> beanMap = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, String> classMap = new ConcurrentHashMap<>();
    private static DomainApplicationContainer applicationcontainer;

    public static DomainApplicationContainer build(){
         applicationcontainer = ApplicationcontainerFactory.applicationcontainerFactory;
        return applicationcontainer;
    }

    public <T> void add(String name, T bean){
        StringBuilder namesb = null;
        if (name == null || name.length() <= 0){
            namesb = new StringBuilder();
            String className = bean.getClass().getSimpleName();
            namesb.append(className.substring(0,1).toLowerCase()).append(className.substring(1));
        }
        beanMap.put(namesb == null?name:namesb.toString(),bean);
    }

    public <T> void add(String name, Class<T> classz){
        StringBuilder namesb = null;
        if (name == null || name.length() <= 0){
            namesb = new StringBuilder();
            String className = classz.getSimpleName();
            namesb.append(className.substring(0,1).toLowerCase()).append(className.substring(1));
        }
        String key = namesb == null?name:namesb.toString();
        if (classMap.containsKey(key)){
            throw new ParamsValidationExcetion(DomainApplicationSysCode.A0400000001,name);
        }
        classMap.put(key,classz.getName());
    }

    public <T> T getBean(String name){
        return (T) beanMap.get(name);
    }

    public Class<?> getClass(String name){
        try {
            String className = classMap.get(name);
            if (StringUtils.isNotBlank(className)){
                return Class.forName(className);
            }
        } catch (ClassNotFoundException e) {
            LOGGER.error("{}, Class not found :{}",name,e);
        }
        return null;
    }

    private final static class ApplicationcontainerFactory{
        private static final DomainApplicationContainer applicationcontainerFactory = new DomainApplicationContainer();
    }
}
