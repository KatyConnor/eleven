package com.hx.domain.framework.obj.vo;

import com.hx.lang.commons.utils.ObjectUtils;

import java.io.Serializable;

/**
 *  前后端接口交互数据对象
 * @Author wml
 * @Date 2019-02-19 view object
 */
public abstract class BaseVO implements Serializable {

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
