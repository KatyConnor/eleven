package hx.nine.eleven.jobtask.constant;

import hx.nine.eleven.jobtask.enums.TaskProcessEnum;

/**
 * SQL中固定参数值
 */
public interface ScheduleValues {
	Object[] DO_QUERY_VALUES = new Object[] {"1","'"+ TaskProcessEnum.IN_PROCESS.getCode()+"'"};

	/**
	 * 查询可执行任务
	 */
	String TASK_QUERY_SQL = "TASK_QUERY_SQL";
	/**
	 * 修改任务执行状态
	 */
	String TASK_DO_UPDATE = "TASK_DO_UPDATE";
	/**
	 * 修改任务执行状态，完成时间
	 */
	String TASK_DONE_UPDATE = "TASK_DONE_UPDATE";
	/**
	 * 插入任务执行日志
	 */
	String TASK_LOG_INSERT = "TASK_LOG_INSERT";
	/**
	 * 修改任务执行日志执行结果
	 */
	String TASK_LOG_UPDATE = "TASK_LOG_UPDATE";

}
