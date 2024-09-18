package com.hx.vertx.file.monitor.annotations;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConfigurationPropertisBind {

    String prefix();
}
