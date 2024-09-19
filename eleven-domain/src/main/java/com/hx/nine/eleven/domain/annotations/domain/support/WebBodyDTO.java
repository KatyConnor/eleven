package com.hx.nine.eleven.domain.annotations.domain.support;

import com.hx.nine.eleven.domain.conver.BeanPropertiesConvert;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebBodyDTO {

    String tradeCode() default "";

    String subTradeCode() default "";

    Class<? extends BeanPropertiesConvert> convert() default BeanPropertiesConvert.class;
}
