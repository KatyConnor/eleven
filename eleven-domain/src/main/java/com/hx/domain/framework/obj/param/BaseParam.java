package com.hx.domain.framework.obj.param;

import com.hx.lang.commons.utils.ObjectUtils;
import java.io.Serializable;

/**
 * @Description 前端访问后台接口数据结构对象
 * @Author wangml
 * @Date 2017-12-12
 */
public class BaseParam implements Serializable {

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
