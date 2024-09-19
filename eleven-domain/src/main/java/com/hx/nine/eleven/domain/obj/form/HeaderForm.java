package com.hx.nine.eleven.domain.obj.form;

import javax.validation.constraints.NotEmpty;

public class HeaderForm extends BaseForm{

    /**
     * 主交易码
     */
    @NotEmpty(message = "主交易码不能为空")
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
