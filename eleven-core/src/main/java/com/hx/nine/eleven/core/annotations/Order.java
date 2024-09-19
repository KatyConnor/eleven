package com.hx.nine.eleven.core.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Order {

  int order() default -1024;
}
