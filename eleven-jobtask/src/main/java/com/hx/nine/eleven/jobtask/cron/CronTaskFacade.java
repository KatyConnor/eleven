package com.hx.nine.eleven.jobtask.cron;

/**
 * 定时任务执行类
 * @author wml
 * @Discription
 * @Date 2023-06-09
 */
public interface CronTaskFacade {

    //执行任完成返回true/false标志
    boolean execute();

}
