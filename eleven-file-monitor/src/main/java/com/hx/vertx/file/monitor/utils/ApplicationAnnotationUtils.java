package com.hx.vertx.file.monitor.utils;

import com.hx.vertx.file.monitor.annotations.Component;
import com.hx.vertx.file.monitor.annotations.ConfigurationPropertisBind;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class ApplicationAnnotationUtils {

    private final static Class<Annotation>[] annotations = new Class[]{Component.class, ConfigurationPropertisBind.class};

    public static <T extends Annotation> T getAnnotations(Annotation ann,Class<T> annotationType){
        return values().stream().filter(antype ->antype.getName().equals(annotationType.getName()) && antype.isInstance(ann)).findFirst().orElse(null)!=null?(T) ann:null;
    }

    public static List<Class<Annotation>> values(){
       return Arrays.asList(annotations);
    }
}
