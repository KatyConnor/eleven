package com.hx.nine.eleven.domain.factory;

import com.hx.nine.eleven.domain.annotations.domain.support.MapperMethod;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Method;

public abstract class DataMapperMethodSupportFactory {

    public static void execMapperFactoryMethod(Object obj,String subTradeCode,Object ... args){
        MethodAccess methodAccess = MethodAccess.get(obj.getClass());
        ConstructorAccess constructorAccess = ConstructorAccess.get(obj.getClass());
        Method[] methods = obj.getClass().getDeclaredMethods();
        for (Method method : methods){
            MapperMethod mapperMethod = method.getAnnotation(MapperMethod.class);
            if (mapperMethod != null && mapperMethod.subTradeCode().equals(subTradeCode)){
                methodAccess.invoke(obj,method.getName(),args);
                break;
            }
        }
    }
}
