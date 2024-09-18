package com.hx.domain.framework.annotations.domain.support;

import com.hx.vertx.boot.annotations.Component;
import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MapperFactory {

//    @AliasFor(annotation = Component.class)
    String value() default "";
    String init() default "";
}
