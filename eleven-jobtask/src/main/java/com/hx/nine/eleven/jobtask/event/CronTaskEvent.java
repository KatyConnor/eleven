package com.hx.nine.eleven.jobtask.event;

import co.paralleluniverse.fibers.SuspendExecution;
import com.github.f4b6a3.ulid.UlidCreator;
import com.hx.nine.eleven.commons.utils.DateUtils;
import com.hx.nine.eleven.jobtask.constant.ScheduleSQL;
import com.hx.nine.eleven.jobtask.entity.ScheduleTaskEntity;
import com.hx.nine.eleven.jobtask.enums.TaskProcessEnum;
import com.hx.nine.eleven.jobtask.mapperfactory.DomainOOPMapperFactory;
import com.hx.nine.eleven.jobtask.utils.WebIPToolUtils;
import com.hx.nine.eleven.logchain.toolkit.util.HXLogger;
import com.hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.nine.eleven.jobtask.cron.CronTaskFacade;

import java.util.LinkedList;
import java.util.List;

/**
 * 协程执行定时任务
 * @author wml
 * @Discription
 * @Date 2023-06-02
 */
public class CronTaskEvent extends ThreadPoolEvent<ScheduleTaskEntity,Boolean> {

    public CronTaskEvent(ScheduleTaskEntity entity) {
        super(entity);
    }

    @Override
    public void executeEvent() {

    }

    @Override
    public Boolean executeCallEvent() {
        return false;
    }

    @Override
    public Boolean run() throws SuspendExecution, InterruptedException {
        Boolean res = null;
        String jobId = null;
        try {
            Class<?> exeStatement = Class.forName(this.getEntity().getExeStatement());
            if (!CronTaskFacade.class.isAssignableFrom(exeStatement)){
                HXLogger.build(this).info("任务实现类必须继承com.cqrcb.fssm.platform.common.cron.CronTaskFacade接口，并实现execute方法");
                return false;
            }
            CronTaskFacade cronTaskFacade = (CronTaskFacade) VertxApplicationContextAware.getBean(exeStatement);
            jobId = addJobLog();
            res = cronTaskFacade.execute(); //@TODO 如果execute也执行了协程，可能结果不能及时返回来，需要多场景验证
            HXLogger.build(this).info("定时任务执行完成,返回结果：[{}]",res);
            return res;
        } catch (ClassNotFoundException e) {
            HXLogger.build(this).error("执行定时任务异常",e);
            res = false;
        }finally {
            // 任务执行完毕，修改最终的执行结果到 FS_SCHEDULE_TASK_LOG 表中
            if (res != null){
                HXLogger.build(this).info("定时任务执行完成,准备更新定时任务状态为[PROCESSED]");
                String jobName = this.getEntity().getName();
                Long version = this.getEntity().getVersion();
                Object[] values = new Object[0];
                try {
                    values = new Object[]{res == true?"成功":"失败",
                            "任务执行服务【"+ WebIPToolUtils.getLocalIP()+"】－执行任务名称【"+jobName+"】执行完成，处理结果为"+(res == true?"成功！":"失败！"),
                            DateUtils.getTimeStampAsString(),2,jobId};
                } catch (Exception e) {
                    HXLogger.build(CronTaskEvent.class).error("获取本机IP地址，更新到 FS_SCHEDULE_TASK_LOG 表异常；",e);
                }
                // 更新任务执行结果到执行日志记录中
                DomainOOPMapperFactory.executeUpdate(ScheduleSQL.TASK_LOG_UPDATE,values);
                // 执行定时任务执行完毕，更新SCHEDULE_JOB_TASK表状态
                Object[] vslues = new Object[] {TaskProcessEnum.PROCESSED.getCode(), DateUtils.getTimeStampAsString(),
                        DateUtils.getTimeStampAsString(),version+1,this.getEntity().getId(),version};
                DomainOOPMapperFactory.executeUpdate(ScheduleSQL.TASK_DONE_UPDATE,vslues);
            }
        }
        return res;
    }

    private String addJobLog(){
        // 记录一条执行日志
        List<Object> values = new LinkedList<>();
        String id = UlidCreator.getUlid().toString();
        values.add(id);
        values.add(this.getEntity().getName());
        values.add("处理中");
        values.add("");
        values.add(1l);
        values.add(DateUtils.getTimeStampAsString());
        values.add(DateUtils.getTimeStampAsString());
        values.add(true);
        //添加定时任务执行日志
        DomainOOPMapperFactory.execute(ScheduleSQL.TASK_LOG_INSERT,values.toArray());
        return id;
    }
}
