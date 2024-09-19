package com.hx.nine.eleven.domain.annotations.domain.support;

import com.hx.nine.eleven.domain.conver.BeanConvert;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainDO {

    String tradeCode() default "";

    String[] subTradeCode() default {};

    String value() default "";

    /**
     * 字段类型
     * @return
     */
    String fieldType() default "";

    /**
     * 转换目标类型
     * @return
     */
    Class<?> convertType() default BeanConvert.class;
}
