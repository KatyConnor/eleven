package hx.nine.eleven.jobtask.event;

import co.paralleluniverse.fibers.SuspendExecution;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.jobtask.ScheduleSQL;
import hx.nine.eleven.jobtask.constant.ScheduleValues;
import hx.nine.eleven.jobtask.entity.ScheduleTaskEntity;
import hx.nine.eleven.jobtask.mapperfactory.DomainOOPMapperFactory;
import hx.nine.eleven.jobtask.utils.ScheduleExe;
import hx.nine.eleven.logchain.toolkit.util.HXLogger;
import hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;
import hx.nine.eleven.jobtask.cron.CronTaskFacade;

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
        ScheduleSQL scheduleSQL = ScheduleExe.getScheduleSQL();
        Boolean res = null;
        String logId = null;
        try {
            Class<?> exeStatement = Class.forName(this.getEntity().getExecStatement());
            if (!CronTaskFacade.class.isAssignableFrom(exeStatement)){
                HXLogger.build(this).info("任务实现类必须继承 hx.nine.eleven.jobtask.cron.CronTaskFacade 接口，并实现execute方法");
                return false;
            }
            CronTaskFacade cronTaskFacade = (CronTaskFacade) ElevenApplicationContextAware.getBean(exeStatement);
            logId = addJobLog(scheduleSQL);
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
                    values = scheduleSQL.initResLog(res,logId,jobName);
                } catch (Exception e) {
                    HXLogger.build(CronTaskEvent.class).error("获取本机IP地址，更新到 FS_SCHEDULE_TASK_LOG 表异常；",e);
                }
                // 更新任务执行结果到执行日志记录中
                DomainOOPMapperFactory.executeUpdate(scheduleSQL.getScheduleSql(ScheduleValues.TASK_LOG_UPDATE),values);
                // 执行定时任务执行完毕，更新SCHEDULE_JOB_TASK表状态
                values = scheduleSQL.initScheduleResValue(version,this.getEntity().getId());
                DomainOOPMapperFactory.executeUpdate(scheduleSQL.getScheduleSql(ScheduleValues.TASK_DONE_UPDATE),values);
            }
        }
        return res;
    }

    private String addJobLog(ScheduleSQL scheduleSQL){
        //添加定时任务执行日志
        Object[] values =scheduleSQL.initLog(this.getEntity().getName());
        DomainOOPMapperFactory.execute(scheduleSQL.getScheduleSql(ScheduleValues.TASK_LOG_INSERT), values);
        return StringUtils.valueOf(values[0]);
    }
}
