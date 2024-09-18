package com.hx.domain.framework.annotations.domain.support;

import com.hx.domain.framework.conver.BeanPropertiesConvert;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebHeaderDTO {

    String value() default "";

    Class<? extends BeanPropertiesConvert> convert() default BeanPropertiesConvert.class;
}
