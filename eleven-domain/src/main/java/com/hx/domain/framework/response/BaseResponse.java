package com.hx.domain.framework.response;

import com.hx.sys.message.code.code.ApplicationSysMessageCode;
import com.hx.sys.message.code.enums.SystemCode;
import java.io.Serializable;

/**
 * 交易返回对象
 * @Author wml
 * @Date 2019-02-18
 */
public class BaseResponse<R> implements Serializable {

    /**
     * 交易状态码
     */
    private String statusCode;
    /**
     * 业务编码
     */
    private String code;
    /**
     * 业务提示信息
     */
    private String message;
    /**
     * 返回实体数据
     */
    private R data;

    public BaseResponse(){
        successful();
    }

    public BaseResponse(String statusCode, String code, String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        successful();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public BaseResponse setStatusCode(String statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getCode() {
        return code;
    }

    public BaseResponse setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public BaseResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public R getData() {
        return data;
    }

    public void setData(R data) {
        this.data = data;
    }

    public BaseResponse successful(){
        this.statusCode = SystemCode.SUCCESS.getCode();
        this.code = ApplicationSysMessageCode.B0000000000.getCode();
        this.message = ApplicationSysMessageCode.B0000000000.getMessage();
        return this;
    }

    public BaseResponse failure(){
        this.statusCode = SystemCode.FAILED.getCode();
        this.code = ApplicationSysMessageCode.B0000000001.getCode();
        this.message = ApplicationSysMessageCode.B0000000001.getMessage();
        return this;
    }
}
