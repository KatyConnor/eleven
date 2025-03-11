package hx.nine.eleven.commons.annotation;

import hx.nine.eleven.commons.json.convert.ObjectConvert;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMapper {

    String name();

    Class<? extends ObjectConvert> using();
}
