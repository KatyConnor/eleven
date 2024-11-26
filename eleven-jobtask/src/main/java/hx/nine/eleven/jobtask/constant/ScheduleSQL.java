package hx.nine.eleven.jobtask.constant;

/**
 * 执行SQL
 */
public interface ScheduleSQL {

	/**
	 * 查询可执行任务
	 */
	String TASK_QUERY_SQL = "SELECT * FROM SCHEDULE_JOB_TASK WHERE TASK_STATUS = ? AND STATUS != ?";
	/**
	 * 修改任务执行状态
	 */
	String TASK_DO_UPDATE = "UPDATE SCHEDULE_JOB_TASK SET STATUS = ?,UPDATE_TIME = ?,VERSION = ? WHERE ID = ? AND VERSION = ?";
	/**
	 * 修改任务执行状态，完成时间
	 */
	String TASK_DONE_UPDATE = "UPDATE SCHEDULE_JOB_TASK SET STATUS = ?,COMPLETE = ?,UPDATE_TIME = ?,VERSION = ? WHERE ID = ? AND VERSION = ?";
	/**
	 * 插入任务执行日志
	 */
	String TASK_LOG_INSERT = "INSERT INTO FS_SCHEDULE_TASK_LOG (ID,JOB_NAME,STATUS,MSG,VERSION,CREATE_TIME,UPDATE_TIME,EFFECTIVE) " +
			"VALUES (?,?,?,?,?,?,?,?)";
	/**
	 * 修改任务执行日志执行结果
	 */
	String TASK_LOG_UPDATE = "UPDATE FS_SCHEDULE_TASK_LOG SET STATUS = ?,MSG = ?,UPDATE_TIME = ?,VERSION = ? WHERE ID = ?";
}
