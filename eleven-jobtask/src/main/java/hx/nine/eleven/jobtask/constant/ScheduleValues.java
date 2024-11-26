package hx.nine.eleven.jobtask.constant;

import hx.nine.eleven.jobtask.enums.TaskProcessEnum;

/**
 * SQL中固定参数值
 */
public interface ScheduleValues {
	Object[] DO_QUERY_VALUES = new Object[] {"1","'"+ TaskProcessEnum.IN_PROCESS.getCode()+"'"};
}
