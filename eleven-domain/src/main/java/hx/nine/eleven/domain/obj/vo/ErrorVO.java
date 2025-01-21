package hx.nine.eleven.domain.obj.vo;

import hx.nine.eleven.msg.code.code.MessageCode;
import hx.nine.eleven.msg.code.enums.SystemCode;

/**
 * 错误信息数据
 */
public class ErrorVO extends BaseVO {
    /**
     * 交易状态码
     */
    private String statusCode = SystemCode.FAILED.getCode();
    /**
     * 业务编码
     */
    private String code;
    /**
     * 业务提示信息
     */
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
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
