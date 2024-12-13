package hx.nine.eleven.http.client.response;

import hx.nine.eleven.http.client.enums.HttpStatus;

import java.io.Serializable;

/**
 * @Author mingliang
 * @Date 2018-08-06 17:12
 */
public class HttpResponse<T> implements Serializable {

    /** 状态码 */
    private HttpStatus code;
    /** 业务状态码 */
    private String statusCode;
    /** 通信状态 */
    private boolean success;
    /** 业务状态 */
    private boolean process;
    /** 返回数据 */
    private T data;
    /** 返回描述 */
    private String message;
    /** 授权token */
    private String authorizedToken;
    /** 错误码 */
    private Object errorCode;
    /** 错误描述 */
    private String errorMessage;

    public HttpResponse(){
        successful();
    }

    public HttpStatus getCode() {
        return code;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isProcess() {
        return process;
    }

    public void setProcess(boolean process) {
        this.process = process;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAuthorizedToken() {
        return authorizedToken;
    }

    public void setAuthorizedToken(String authorizedToken) {
        this.authorizedToken = authorizedToken;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void successful(){
        this.code = HttpStatus.OK;
        this.success = true;
        this.process=true;
        this.message = "调用成功";
    }

    public void failure(){
        this.success = true;
        this.process=false;
        this.message = "调用失败";
    }
}
