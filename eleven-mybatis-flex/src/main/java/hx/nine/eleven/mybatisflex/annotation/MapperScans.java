package hx.nine.eleven.mybatisflex.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @auth wml
 * @date 2024/9/25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface MapperScans {

	String[] basePackages() default {};

	Class<?>[] basePackageClasses() default {};
}
