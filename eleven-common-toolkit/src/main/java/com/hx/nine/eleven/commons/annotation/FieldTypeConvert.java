package com.hx.nine.eleven.commons.annotation;

import com.hx.nine.eleven.commons.json.convert.FieldConvert;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wml
 * @Description
 * @data 2022-06-20
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldTypeConvert {

     Class<? extends FieldConvert> using();
}
