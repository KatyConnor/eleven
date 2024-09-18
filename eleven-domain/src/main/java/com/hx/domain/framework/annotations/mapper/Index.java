package com.hx.domain.framework.annotations.mapper;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wml
 * 2022-05-15
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface Index {

    String name() default "";

    String columnList() default "";

    boolean unique() default false;

    String[] columnExpression() default {};
}
