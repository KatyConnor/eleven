package hx.nine.eleven.core.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface ConditionalOnProperty {

	String[] value() default {};

	String prefix() default "";

	String[] name() default {};

	String havingValue() default "";

	Class<?> propertiesClass();
}
