package hx.nine.eleven.jobtask;

import com.github.f4b6a3.ulid.UlidCreator;
import hx.nine.eleven.commons.utils.DateUtils;
import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.jobtask.constant.ScheduleValues;
import hx.nine.eleven.jobtask.entity.ScheduleTaskEntity;
import hx.nine.eleven.jobtask.enums.TaskProcessEnum;
import hx.nine.eleven.jobtask.utils.WebIPToolUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @auth wml
 * @date 2024/12/13
 */
@Component(init = "initSchedule")
public class DefaultScheduleSql extends ScheduleSQL {

	@Override
	public void initSchedule() {
		addSql(ScheduleValues.TASK_QUERY_SQL,"SELECT * FROM NE_SCHEDULE_JOB_TASK WHERE TASK_STATUS = ? AND STATUS != ?");
		addSql(ScheduleValues.TASK_DO_UPDATE,"UPDATE NE_SCHEDULE_JOB_TASK SET STATUS = ?,UPDATE_TIME = ?,VERSION = ? WHERE ID = ? AND VERSION = ?");
		addSql(ScheduleValues.TASK_DONE_UPDATE,"UPDATE NE_SCHEDULE_JOB_TASK SET STATUS = ?,COMPLETE = ?,UPDATE_TIME = ?,VERSION = ? WHERE ID = ? AND VERSION = ?");
		addSql(ScheduleValues.TASK_LOG_INSERT,"INSERT INTO NE_SCHEDULE_TASK_LOG (ID,JOB_NAME,STATUS,MSG,VERSION,CREATE_TIME,UPDATE_TIME,EFFECTIVE) " +
				"VALUES (?,?,?,?,?,?,?,?)");
		addSql(ScheduleValues.TASK_LOG_UPDATE,"UPDATE NE_SCHEDULE_TASK_LOG SET STATUS = ?,MSG = ?,UPDATE_TIME = ?,VERSION = ? WHERE ID = ?");

		addSqlValue(ScheduleValues.TASK_QUERY_SQL,ScheduleValues.DO_QUERY_VALUES);

		initResultMap("id",String.class);
		initResultMap("name",String.class);
		initResultMap("status",String.class);
		initResultMap("task_status",Boolean.class);
		initResultMap("cron",String.class);
		initResultMap("exec_statement",String.class);
		initResultMap("exec_method",String.class);
		initResultMap("complete",String.class);
		initResultMap("create_time",String.class);
		initResultMap("update_time",String.class);
		initResultMap("version",Long.class);
		initResultMap("effective",Boolean.class);

		setScheduleTaskEntity(ScheduleTaskEntity.class);
	}

	@Override
	public Object[] initScheduleValue(long updateVersion,String jobId,long version) {
		Object[] values = new Object[] {TaskProcessEnum.IN_PROCESS.getCode(), DateUtils.getTimeStampAsString(),
				updateVersion,jobId,version};
		return values;
	}

	@Override
	public Object[] initScheduleResValue(long version, String jobId) {
		Object[] values = new Object[] {TaskProcessEnum.PROCESSED.getCode(), DateUtils.getTimeStampAsString(),
				DateUtils.getTimeStampAsString(),version+1,jobId,version};
		return values;
	}

	@Override
	public Object[] initLog(String jobName) {
		// 记录一条执行日志
		List<Object> values = new LinkedList<>();
		String id = UlidCreator.getUlid().toString();
		values.add(id);
		values.add(jobName);
		values.add("处理中");
		values.add("");
		values.add(1l);
		values.add(DateUtils.getTimeStampAsString());
		values.add(DateUtils.getTimeStampAsString());
		values.add(true);
		return values.toArray();
	}

	@Override
	public Object[] initResLog(boolean res,String logId,String jobName) {
		Object[] values = new Object[] {res == true?"成功":"失败",
				"任务执行服务【"+ WebIPToolUtils.getLocalIP()+"】－执行任务名称【"+jobName+"】执行完成，处理结果为["+(res == true?"成功] ！":"失败] ！"),
				DateUtils.getTimeStampAsString(),2,logId};
		return values;
	}
}
