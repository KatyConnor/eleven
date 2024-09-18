package com.hx.domain.framework.obj.form;

import com.hx.lang.commons.utils.ObjectUtils;

import java.io.Serializable;

public abstract class BaseForm implements Serializable {

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
