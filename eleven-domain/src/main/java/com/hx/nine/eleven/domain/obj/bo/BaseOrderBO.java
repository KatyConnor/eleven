package com.hx.nine.eleven.domain.obj.bo;

import com.hx.nine.eleven.commons.utils.ObjectUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 *  business Object
 * @Description  Controller 接受数据后到 Domain之间的业务数据处理，应用于对接口入参进行必要规则验证
 * @Author wml
 * @Date 2019-02-18
 */
public class BaseOrderBO implements Serializable {

    private Boolean openFiber = false;

    public Boolean getOpenFiber() {
        return openFiber;
    }

    public void setOpenFiber(Boolean openFiber) {
        this.openFiber = openFiber;
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
