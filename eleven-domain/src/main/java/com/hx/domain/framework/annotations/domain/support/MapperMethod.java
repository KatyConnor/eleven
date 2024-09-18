package com.hx.domain.framework.annotations.domain.support;

import java.lang.annotation.*;

/**
 * 数据库领域数据操作细分
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MapperMethod {

    String subTradeCode() default "";

    String mapperFactoryMethod() default "";
}
