package com.hx.vertx.file.monitor.properties;

import com.hx.vertx.file.monitor.annotations.ConfigurationPropertisBind;

/**
 * @author wangml
 * @Date 2019-09-27
 */
@ConfigurationPropertisBind(prefix = "file.monitor.thread.pool")
public class ThreadPoolProperties {
    //线程组名称
    private String threadGroupName;
    //线程名称
    private String threadPoolName;
    //线程名称前缀
    private String threadNamePrefix;
    // 核心线程数
    private Integer corePoolSize;
    // 最大线程数
    private Integer maximumPoolSize;
    // 线程维持存活时间
    private long keepAliveTime;
    // 执行时间单位
    private String unit;
    // 线程队列数量
    private Integer workQueueNum = 50;
    // 线程跟踪唯一标识ID
    private Boolean traceId = false;

    public String getThreadGroupName() {
        return threadGroupName;
    }

    public void setThreadGroupName(String threadGroupName) {
        this.threadGroupName = threadGroupName;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

//    public void setKeepAliveTime(long keepAliveTime) {
//        this.keepAliveTime = keepAliveTime;
//    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = Long.valueOf(keepAliveTime.toString());
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getWorkQueueNum() {
        return workQueueNum;
    }

    public void setWorkQueueNum(Integer workQueueNum) {
        this.workQueueNum = workQueueNum;
    }

    public Boolean getTraceId() {
        return traceId;
    }

    public void setTraceId(Boolean traceId) {
        this.traceId = traceId;
    }
}
