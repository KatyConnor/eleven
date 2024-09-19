package com.hx.nine.eleven.domain.annotations.domain.support;

import com.hx.nine.eleven.core.annotations.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Component
public @interface DomainService {

    String value() default "";

    String init() default "";
}
