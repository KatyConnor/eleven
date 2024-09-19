package com.hx.nine.eleven.domain.response;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class WebHttpResponse<H,E> implements Serializable {

    /**
     * 返回报文头（header）
     */
    @NotNull(message = "返回头[header]不能为空")
    private H responseHeader;

    /**
     * 返回报文体（body）
     */
    private BaseResponse<E> responseBody;

    public WebHttpResponse(){
        this.responseBody = new BaseResponse<E>();
    }

    public H getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(H responseHeader) {
        this.responseHeader = responseHeader;
    }

    public BaseResponse<E> getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(E data) {
        this.responseBody.setData(data);
    }
}
