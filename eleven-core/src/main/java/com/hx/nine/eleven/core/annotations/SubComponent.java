package com.hx.nine.eleven.core.annotations;

import java.lang.annotation.*;
import java.util.Optional;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SubComponent {
	String value() default "";

	Class<?> interfaces();

	String init() default "";
}
