package hx.nine.eleven.domain.obj.vo;

import hx.nine.eleven.msg.code.code.MessageCode;

/**
 * 错误信息数据
 */
public class ErrorVO extends BaseVO {

    private String code;
    private String message;

    public ErrorVO(){}

    public ErrorVO(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void setErrorMessageCode(MessageCode messageCode){
        this.code = messageCode.getCode();
        this.message = messageCode.getMessage();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
