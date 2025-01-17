package hx.nine.eleven.commons.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldList {

    Class<?> convertType();
}
