package hx.nine.eleven.commons.entity;

import java.util.Map;

/**
 * 参数效验错误信息提示实体
 * @Author wml
 * @Date 2019-02-18
 */
public class ValidationResultEntity {

    /** 校验结果是否有错 */
    private boolean hasErrors;
    /** 校验错误信息, 每个属性单独设置 */
    private Map<String, String> errorMsgMap;
    /** 校验错误信息, 汇总统一提示 */
    private String errorMsg;

    private ValidationResultEntity(){}

    public static ValidationResultEntity build(){
        return new ValidationResultEntity();
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public ValidationResultEntity setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
        return this;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public ValidationResultEntity setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public ValidationResultEntity setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
