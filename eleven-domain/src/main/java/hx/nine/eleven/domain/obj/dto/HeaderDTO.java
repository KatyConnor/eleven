package hx.nine.eleven.domain.obj.dto;

import hx.nine.eleven.domain.constant.WebHttpBodyConstant;

import javax.validation.constraints.NotBlank;

public class HeaderDTO extends BaseDTO{

    /**
     * 主交易码
     */
    @NotBlank(message = "主交易码不能为空")
    private String tradeCode;

    /**
     * 子交易码
     */
    private String subTradeCode;

    /**
     * 交易报文头,可自定义请求报文头
     */
    private String headerCode = WebHttpBodyConstant.DEFAULT_HEADER;

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
