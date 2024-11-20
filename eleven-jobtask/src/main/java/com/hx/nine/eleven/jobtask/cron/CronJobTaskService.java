package com.hx.nine.eleven.jobtask.cron;


import com.hx.lang.commons.utils.*;
import com.hx.nine.eleven.logchain.toolkit.util.HXLogger;
import com.hx.nine.eleven.jobtask.constant.ScheduleSQL;
import com.hx.nine.eleven.jobtask.constant.ScheduleValues;
import com.hx.nine.eleven.jobtask.entity.ScheduleTaskEntity;
import com.hx.nine.eleven.jobtask.enums.TaskProcessEnum;
import com.hx.nine.eleven.jobtask.event.CronTaskEvent;
import com.hx.nine.eleven.jobtask.mapperfactory.DomainOOPMapperFactory;
import com.hx.nine.eleven.jobtask.utils.DateUtil;
import com.hx.nine.eleven.thread.pool.executor.pool.ThreadPoolService;
import com.hx.vertx.boot.annotations.SubComponent;
import com.hx.vertx.boot.task.HXVertxScheduledTask;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现HXVertxScheduledTask，定时触发任务,
 * @author wml
 * @Discription
 * @Date 2023-06-02
 */
@SubComponent(interfaces = HXVertxScheduledTask.class)
public class CronJobTaskService extends HXVertxScheduledTask {
    private static final Map<String, Class<?>> resultMap = new HashMap<>();
    private static final String threadGroupName = "cron-task";

    static {
        resultMap.put("id",String.class);
        resultMap.put("name",String.class);
        resultMap.put("status",String.class);
        resultMap.put("cron",String.class);
        resultMap.put("exe_statement",String.class);
        resultMap.put("version",Long.class);
        resultMap.put("create_time",String.class);
        resultMap.put("update_time",String.class);
        resultMap.put("task_status",Boolean.class);
        resultMap.put("method",String.class);
        resultMap.put("effective",Boolean.class);
        resultMap.put("complete",String.class);
    }

    @Override
    public void runScheduleTask() {
        //执行数据库查询,获取可执行的定时任务清单列表
        List<Map<String,Object>> resultList = DomainOOPMapperFactory
                .executeQuery(ScheduleSQL.TASK_QUERY_SQL, ScheduleValues.DO_QUERY_VALUES,resultMap);
        List<ScheduleTaskEntity> taskEntityList = BeanMapUtil.listMapsToBeans(resultList,ScheduleTaskEntity.class);
        runTask(taskEntityList);
    }

    /**
     * 执行任务，将满足条件的任务丢入线程池执行
     * @param taskEntityList 任务清单列表
     */
    public void runTask(List<ScheduleTaskEntity> taskEntityList) {
        //没有满足条件的可执行任务
        if (ObjectUtils.isEmpty(taskEntityList)) {
            return;
        }

        for (ScheduleTaskEntity p : taskEntityList) {
            // 检查执行时间是否变化，如果变化则更新时间
            if (ScheduleCron.build().notHave(p.getId())) {
                CronKit cron = new CronKit(p.getCron());
                ScheduleCron.build().addCron(p.getId(), cron);
            }
            //判断当前时间是否更新
            CronKit cronKit = ScheduleCron.build().getCronKit(p.getId());
            if (!p.getCron().equals(cronKit.getCron())) {
                CronKit cron = new CronKit(p.getCron());
                ScheduleCron.build().addCron(p.getId(), cron);
            }
            // 满足可执行条件的定时任务
            if (!ScheduleCron.build().haveNext(p.getId(), new Date(), DateUtil.parseStrToDate(p.getComplete(),"yyyy-MM-dd HH:mm:ss"))) {
                continue;
            }

            p.setStatus(TaskProcessEnum.IN_PROCESS.getCode());
            long version = p.getVersion() + 1;
            Object[] vslues = new Object[] {TaskProcessEnum.IN_PROCESS.getCode(), DateUtils.getTimeStampAsString(),
                    version,p.getId(),p.getVersion()};
            int count = DomainOOPMapperFactory.executeUpdate(ScheduleSQL.TASK_DO_UPDATE,vslues);
            //---CronTask:定时任务已被其他服务执行，本服务跳过
            if (count != 1) {
                continue;
            }
            p.setVersion(version);
            //任务丢入线程池，让线程池执行任务
            try {
                HXLogger.build(this).info("CronTask:执行任务[{}],任务执行规则[{}]", p.getName(), p.getCron());
                ScheduleTaskEntity entity = BeanUtils.copyProperties(p, ScheduleTaskEntity.class);
                doCronTask(entity);
            } catch (Exception e) {
                // 如果抛出异常，回滚状态
                HXLogger.build(this).error("定时任务执行异常，更新定时任务状态为[PROCESSED]", e);
            }
        }
    }

    private Boolean doCronTask(ScheduleTaskEntity eventEntity) {
        CronTaskEvent task = new CronTaskEvent(eventEntity);
        task.setThreadGroupName(threadGroupName);
        return ThreadPoolService.build().run(task);
    }
}
