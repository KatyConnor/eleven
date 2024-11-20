package hx.nine.eleven.domain.context;

import com.github.f4b6a3.ulid.UlidCreator;

/**
 * 上下文领域数据传递驱动事件对象
 * @author wml
 * @date 2022-12-05
 */
public class DomainContextEventEntity {

    /**
     * 任务编号，唯一性
     */
    private String eventProccessId;
    /**
     * 上下文信息
     */
    private DomainContext context;

    /**
     * 是否异步无返回结果集
     */
    private Boolean  asynNoCall = true;
    private Boolean enableDomainSupport;

    public DomainContextEventEntity() {
        this.eventProccessId = UlidCreator.getUlid().toString();
    }

    public String getEventProccessId() {
        return eventProccessId;
    }

    public DomainContext getContext() {
        return context;
    }

    public void setContext(DomainContext context) {
        this.context = context;
    }

    public Boolean getAsynNoCall() {
        return asynNoCall;
    }

    public void setAsynNoCall(Boolean asynNoCall) {
        this.asynNoCall = asynNoCall;
    }

    public Boolean getEnableDomainSupport() {
        return enableDomainSupport;
    }

    public void setEnableDomainSupport(Boolean enableDomainSupport) {
        this.enableDomainSupport = enableDomainSupport;
    }
}
