package hx.nine.eleven.jobtask.utils;

import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.jobtask.DefaultScheduleSql;
import hx.nine.eleven.jobtask.ElevenScheduleExeException;
import hx.nine.eleven.jobtask.ScheduleSQL;

/**
 * @auth wml
 * @date 2024/12/13
 */
public class ScheduleExe {

	public static ScheduleSQL getScheduleSQL(){
		ScheduleSQL scheduleSQL = ElevenApplicationContextAware.getBean(ScheduleSQL.class);
		if (scheduleSQL == null){
			scheduleSQL = ElevenApplicationContextAware.getBean(DefaultScheduleSql.class);
		}
		if (scheduleSQL == null){
			throw new ElevenScheduleExeException("没有查找到调度任务数据库操作可执行对象[ScheduleSQL]," +
					"请集成抽象类[hx.nine.eleven.jobtask.ScheduleSQL],完成可执行SQL初始话");
		}
		return scheduleSQL;
	}
}
