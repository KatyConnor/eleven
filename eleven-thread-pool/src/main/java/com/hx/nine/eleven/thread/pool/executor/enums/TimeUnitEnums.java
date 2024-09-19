package com.hx.nine.eleven.thread.pool.executor.enums;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public enum TimeUnitEnums{

    NANOSECONDS("NANOSECONDS",TimeUnit.NANOSECONDS),
    MICROSECONDS("MICROSECONDS",TimeUnit.MICROSECONDS),
    MILLISECONDS("MILLISECONDS",TimeUnit.MILLISECONDS),
    SECONDS("SECONDS",TimeUnit.SECONDS),
    MINUTES("MINUTES",TimeUnit.MINUTES),
    HOURS("HOURS",TimeUnit.HOURS),
    DAYS("DAYS",TimeUnit.DAYS);

    private String code;
    private TimeUnit timeUnit;

    TimeUnitEnums(String code, TimeUnit timeUnit) {
        this.code = code;
        this.timeUnit = timeUnit;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public static TimeUnitEnums getByCode(String code){
        return Arrays.stream(TimeUnitEnums.values()).filter(e -> e.getCode().equals(code)).findFirst().orElseGet(null);
    }
}
