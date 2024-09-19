package com.hx.nine.eleven.commons;

import com.fasterxml.jackson.core.type.TypeReference;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * JSON转换，泛型处理
 * @author wml
 * @Description
 * @data 2022-06-09
 */
public class JacksonTypeReference<T> extends TypeReference<T> {

    private Map<String,Class<?>> classzzList;

    protected JacksonTypeReference() {
        super();
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return super.compareTo(o);
    }

    public static JacksonTypeReference build(){
        return new JacksonTypeReference();
    }

    public JacksonTypeReference addType(String name,Class<?> classzz){
        classzzList.put(name,classzz);
        return this;
    }

    public Map<String, Class<?>> getClasszzList() {
        return classzzList;
    }

    public JacksonTypeReference setClasszzList(Map<String, Class<?>> classzzList) {
        this.classzzList = classzzList;
        return this;
    }
}
