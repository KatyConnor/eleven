package com.hx.nine.eleven.thread.pool.executor;

import com.hx.nine.eleven.commons.annotation.FieldTypeConvert;
import com.hx.nine.eleven.thread.pool.executor.convert.FieldIntegerToLongConvert;
import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;

/**
 * 属性配置
 * @author wangml
 * @Date 2019-09-27
 */
@ConfigurationPropertiesBind(prefix = "eleven.boot.thread.pool")
public class ThreadPoolProperties {
    //线程组名称
    private String threadGroupName;
    //线程名称
    private String threadPoolName;
    //线程名称前缀
    private String threadNamePrefix;
    // 核心线程数
    private int corePoolSize;
    // 最大线程数
    private int maximumPoolSize;
    // 线程维持存活时间
    @FieldTypeConvert(using = FieldIntegerToLongConvert.class)
    private long keepAliveTime;
    // 执行时间单位
    private String unit;
    // 线程队列数量
    private int workQueueNum = 50;
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

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public long getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getWorkQueueNum() {
        return workQueueNum;
    }

    public void setWorkQueueNum(int workQueueNum) {
        this.workQueueNum = workQueueNum;
    }

    public Boolean getTraceId() {
        return traceId;
    }

    public void setTraceId(Boolean traceId) {
        this.traceId = traceId;
    }
}
