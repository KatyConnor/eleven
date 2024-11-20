package hx.nine.eleven.domain.obj.vo;

import hx.nine.eleven.commons.utils.ObjectUtils;

import javax.validation.constraints.NotEmpty;

public class ResponseHeaderVO extends HeaderVO {

    @NotEmpty(message = "交易请求流水号不能为空")
    private String reqNo;
    @NotEmpty(message = "交易开始时间不能为空")
    private String tradeTime;
    @NotEmpty(message = "交易完成时间时间不能为空")
    private String tradeEndTime;
    @NotEmpty(message = "渠道编号不能为空")
    private String channelNo;
    /**
     * 交易跟踪ID
     */
    private String traceId;

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getTradeEndTime() {
        return tradeEndTime;
    }

    public void setTradeEndTime(String tradeEndTime) {
        this.tradeEndTime = tradeEndTime;
    }

    public String getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(String channelNo) {
        this.channelNo = channelNo;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    @Override
    public String toString() {
        return ObjectUtils.toString(this);
    }
}
