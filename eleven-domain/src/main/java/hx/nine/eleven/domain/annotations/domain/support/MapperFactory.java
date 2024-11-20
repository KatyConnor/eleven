package hx.nine.eleven.domain.annotations.domain.support;

import hx.nine.eleven.core.annotations.Component;
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
