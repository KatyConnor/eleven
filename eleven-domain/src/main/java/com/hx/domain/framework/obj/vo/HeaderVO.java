package com.hx.domain.framework.obj.vo;

import javax.validation.constraints.NotEmpty;

public class HeaderVO extends BaseVO{

    /**
     * 主交易码
     */
    @NotEmpty(message = "交易码不能为空")
    private String tradeCode;

    /**
     * 交易报文头
     */
    private String headerCode;

    /**
     * 子交易码
     */
    private String subTradeCode;

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getHeaderCode() {
        return headerCode;
    }

    public void setHeaderCode(String headerCode) {
        this.headerCode = headerCode;
    }

    public String getSubTradeCode() {
        return subTradeCode;
    }

    public void setSubTradeCode(String subTradeCode) {
        this.subTradeCode = subTradeCode;
    }
}
