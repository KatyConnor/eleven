package hx.nine.eleven.commons.json;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Confusion {

    ConfusedFieldTypeEnum type() default ConfusedFieldTypeEnum.NOT_CONFUSED;

    boolean turnOn() default true;
}
