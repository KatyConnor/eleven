package hx.nine.eleven.domain.annotations.domain.support;

import hx.nine.eleven.domain.conver.BeanPropertiesConvert;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WebRoute
public @interface WebBodyForm {

    String tradeCode() default "";

    String subTradeCode() default "";

    Class<? extends BeanPropertiesConvert> convert() default BeanPropertiesConvert.class;
}
