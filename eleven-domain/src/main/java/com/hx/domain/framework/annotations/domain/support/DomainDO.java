package com.hx.domain.framework.annotations.domain.support;

import com.hx.domain.framework.conver.BeanConvert;

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
