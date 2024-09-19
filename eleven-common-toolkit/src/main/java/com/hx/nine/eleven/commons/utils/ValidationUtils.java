package com.hx.nine.eleven.commons.utils;

import com.hx.nine.eleven.commons.entity.ValidationResultEntity;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 参数效验工具类
 * @Author wml
 * @Date 2019-02-18
 */
public class ValidationUtils {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 对 <T> 对象标注的属性进行参数验证，通过ValidationResultEntity 对象返回验证结果
     * @param obj    效验对象
     * @param groups 效验分组，如果指定groups，将会根据指定的groups 对实体参数进行效验，没指定采用默认方案验证
     * @param <T>    对象泛型
     * @return ValidationResultEntity：返回验证结果信息
     */
    public static <T> ValidationResultEntity validate(T obj, Class<?>[] groups ) {
        if ( groups == null || 0 == groups.length ) {
            groups = ArrayUtils.add( groups, Default.class );
        }
        Set<ConstraintViolation<T>> set = validator.validate( obj, groups);
        ValidationResultEntity resultEntity = ValidationResultEntity.build();
        if ( set.size() > 0 ) {
            StringBuilder result = new StringBuilder();
            for ( ConstraintViolation<T> cv : set ) {
                result.append( cv.getPropertyPath().toString() + " : " + cv.getMessage() + "," );
            }
            if (result.length() > 0){
                resultEntity.setHasErrors(true).setErrorMsg(result.substring(0,result.length()-1));
            }
        }
        return resultEntity;
    }

    /**
     * 对 <T> 对象标注的属性进行参数验证，验证不通过，直接抛出异常
     * @param obj    效验对象
     * @param groups 效验分组，如果指定groups，将会根据指定的groups 对实体参数进行效验，没指定采用默认方案验证
     * @param <T>    对象泛型
     */
    public static <T> void validateWithException( T obj, Class<?>[] groups ) throws IllegalArgumentException{
        ValidationResultEntity resultEntity = validate( obj, groups );
        if (resultEntity.isHasErrors()){
            throw new IllegalArgumentException(resultEntity.getErrorMsg());
        }
    }

    /**
     * 传入对象进行默认验证
     * @param obj 效验对象
     * @param <T> 对象泛型
     * @return ValidationResultEntity：返回验证结果信息
     */
    public static <T> ValidationResultEntity validateEntity(T obj) {
        Set<ConstraintViolation<T>> set = validator.validate(obj, Default.class);
        return isValidationError(set);
    }

    /**
     * 对 <T> 对象标注的属性进行参数验证，通过ValidationResultEntity 对象返回验证结果
     * @param obj 效验对象
     * @param groups 效验分组，如果指定groups，将会根据指定的groups 对实体参数进行效验，没指定采用默认方案验证
     * @param <T> 效验对象泛型
     * @return ValidationResultEntity：返回验证结果信息
     */
    public static <T> ValidationResultEntity validateEntity(T obj,Class<?>[] groups) {
        if ( groups == null || 0 == groups.length ) {
            groups = ArrayUtils.add( groups, Default.class );
        }
        Set<ConstraintViolation<T>> set = validator.validate(obj, groups);
        return isValidationError(set);
    }

    /**
     * 对 <T> 对象标注的属性进行参数验证，通过ValidationResultEntity 对象返回验证结果
     * @param obj 效验对象
     * @param propertyName 效验属性
     * @param <T> 效验对象泛型
     * @return ValidationResultEntity：返回验证结果信息
     */
    public static <T> ValidationResultEntity validateProperty(T obj, String propertyName) {
        Set<ConstraintViolation<T>> set = validator.validateProperty(obj, propertyName, Default.class);
        return isValidationError(set);
    }

    /**
     *  组装效验结果信息
     * @param set  效验结果集合
     * @param <T>  效验对象泛型
     * @return ValidationResultEntity：返回验证结果信息
     */
    private static <T> ValidationResultEntity isValidationError(Set<ConstraintViolation<T>> set){
        ValidationResultEntity result = ValidationResultEntity.build();
        Optional.ofNullable(set).ifPresent(res -> {
            if (res.size() <= 0){
                return;
            }
            result.setHasErrors(true);
            Map<String, String> errorMsg = new HashMap<>();
            StringBuilder resultStr = new StringBuilder();
            for (ConstraintViolation<T> cv : res) {
                errorMsg.put(cv.getPropertyPath().toString(), cv.getMessage());
                resultStr.append( cv.getPropertyPath().toString() + " : " + cv.getMessage() + "," );
            }
            result.setErrorMsgMap(errorMsg);
            result.setErrorMsg(resultStr.substring(0,resultStr.length()-1));
        });
        return result;
    }
}
