package com.hx.domain.framework.annotations.domain.support;

import com.hx.vertx.boot.annotations.Component;

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
