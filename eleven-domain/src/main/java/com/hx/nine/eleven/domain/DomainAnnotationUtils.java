package com.hx.nine.eleven.domain;

import com.hx.nine.eleven.domain.annotations.domain.support.DomainDO;
import com.hx.nine.eleven.domain.annotations.domain.support.DomainServiceMethod;
import com.hx.nine.eleven.domain.annotations.domain.support.MapperFactory;
import com.hx.nine.eleven.domain.annotations.domain.support.MapperMethod;
import com.hx.nine.eleven.domain.annotations.domain.support.WebBodyDTO;
import com.hx.nine.eleven.domain.annotations.domain.support.WebBodyForm;
import com.hx.nine.eleven.domain.annotations.domain.support.WebBodyParam;
import com.hx.nine.eleven.domain.annotations.domain.support.WebHeaderDTO;
import com.hx.nine.eleven.domain.annotations.domain.support.WebHeaderForm;
import com.hx.nine.eleven.domain.annotations.domain.support.WebHeaderParam;
import com.hx.nine.eleven.domain.annotations.domain.support.WebHeaderVO;
import com.hx.nine.eleven.domain.annotations.domain.support.WebRoute;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

public class DomainAnnotationUtils {

    private final static Class<Annotation>[] annotations = new Class[]{WebRoute.class, WebHeaderForm.class,
            WebHeaderDTO.class, WebHeaderParam.class, WebBodyForm.class, WebBodyDTO.class, WebBodyParam.class,
            DomainDO.class, DomainServiceMethod.class, MapperMethod.class, MapperFactory.class, WebHeaderVO.class};

    public static <T extends Annotation> T getAnnotations(Annotation ann,Class<T> annotationType){
        return values().stream().filter(antype ->antype.getName().equals(annotationType.getName()) && antype.isInstance(ann)).findFirst().orElse(null)!=null?(T) ann:null;
    }

    public static List<Class<Annotation>> values(){
       return Arrays.asList(annotations);
    }
}
