package hx.nine.eleven.domain.obj.form;

import hx.nine.eleven.commons.utils.ObjectUtils;

import java.io.Serializable;

public abstract class BaseForm implements Serializable {

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
