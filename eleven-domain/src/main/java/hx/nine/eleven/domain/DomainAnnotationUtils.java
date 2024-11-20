package hx.nine.eleven.domain;

import hx.nine.eleven.domain.annotations.domain.support.DomainDO;
import hx.nine.eleven.domain.annotations.domain.support.DomainServiceMethod;
import hx.nine.eleven.domain.annotations.domain.support.MapperFactory;
import hx.nine.eleven.domain.annotations.domain.support.MapperMethod;
import hx.nine.eleven.domain.annotations.domain.support.WebBodyDTO;
import hx.nine.eleven.domain.annotations.domain.support.WebBodyForm;
import hx.nine.eleven.domain.annotations.domain.support.WebBodyParam;
import hx.nine.eleven.domain.annotations.domain.support.WebHeaderDTO;
import hx.nine.eleven.domain.annotations.domain.support.WebHeaderForm;
import hx.nine.eleven.domain.annotations.domain.support.WebHeaderParam;
import hx.nine.eleven.domain.annotations.domain.support.WebHeaderVO;
import hx.nine.eleven.domain.annotations.domain.support.WebRoute;

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
