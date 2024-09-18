package com.hx.vertx.boot.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConditionalOnBean {

	Class<?>[] value() default {};

	String[] bean() default {};
}
