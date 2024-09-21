package com.hx.nine.eleven.jooq.jdbc;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Transaction {

	Class<Throwable> rollback() default Throwable.class;
}
