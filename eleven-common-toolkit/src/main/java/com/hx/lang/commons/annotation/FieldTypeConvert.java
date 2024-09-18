package com.hx.lang.commons.annotation;

import com.hx.lang.commons.json.convert.FieldConvert;

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
