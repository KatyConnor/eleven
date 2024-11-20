package hx.nine.eleven.core.annotations;

import java.lang.annotation.*;

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
