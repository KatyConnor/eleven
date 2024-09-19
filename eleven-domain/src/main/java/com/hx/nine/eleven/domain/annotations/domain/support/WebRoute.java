package com.hx.nine.eleven.domain.annotations.domain.support;

import com.hx.nine.eleven.domain.conver.BeanPropertiesConvert;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface WebRoute {

    String tradeCode() default "";

    String subTradeCode() default "";

    Class<? extends BeanPropertiesConvert> convert() default BeanPropertiesConvert.class;
}
