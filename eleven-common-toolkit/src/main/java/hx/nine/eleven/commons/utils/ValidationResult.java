package hx.nine.eleven.commons.utils;

import java.util.Map;

/**
 * @Author wml
 * @Date 2019-02-18
 */
public class ValidationResult {

    // 校验结果是否有错
    private boolean             hasErrors;

    // 校验错误信息
    private Map<String, String> errorMsg;

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Map<String, String> errorMsg) {
        this.errorMsg = errorMsg;
    }
}
