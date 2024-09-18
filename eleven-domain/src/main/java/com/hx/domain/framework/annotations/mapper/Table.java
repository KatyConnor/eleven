package com.hx.domain.framework.annotations.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wml
 * 2022-05-15
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    String name() default "";

    String catalog() default "";

    String schema() default "";

    PrimaryKey primaryKey() default @PrimaryKey;

    UniqueConstraint[] uniqueConstraints() default {};

    Index[] indexes() default {};

    String comment() default "";
}
