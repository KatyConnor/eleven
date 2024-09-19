package com.hx.nine.eleven.domain.obj.dto;

import com.hx.nine.eleven.commons.utils.ObjectUtils;
import java.io.Serializable;

/**
 * @Description 接口道领域对象（DO）的内部端数据传输对象，DATA TRAC  OBJE
 * @Author wml
 * @Date 2019-02-18
 */
public class BaseDTO implements Serializable {

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
