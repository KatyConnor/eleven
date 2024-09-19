package com.hx.nine.eleven.domain.annotations.domain.support;

import java.lang.annotation.*;

/**
 * domain领域内部执行
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DomainMethod {

	int order() default 0;

	String subTradeCode() default "";
}
