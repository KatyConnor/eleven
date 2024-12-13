package hx.nine.eleven.jobtask;

import hx.nine.eleven.jobtask.entity.ScheduleTaskEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * 执行SQL
 */
public abstract class ScheduleSQL {

	private Class<? extends ScheduleTaskEntity> scheduleTaskEntity;

	private Map<String,String> sqlMap = new HashMap<>();

	private Map<String,Object[]> sqlValueMap = new HashMap<>();

	private Map<String, Class<?>> resultMap = new HashMap<>();

	public abstract void initSchedule();

	/**
	 * 任务执行前，修改任务执行状态
	 * @param updateVersion  数据变更，版本号在原基础上加 1
	 * @param jobId          任务表中任务的主键ID
	 * @param version        任务的原版本号
	 * @return
	 */
	public abstract Object[] initScheduleValue(long updateVersion,String jobId,long version);

	/**
	 *
	 * @param version  任务原版本号，数据修改后，需要对原版本号加 1 写入
	 * @param jobId    任务主键ID
	 * @return
	 */
	public abstract Object[] initScheduleResValue(long version,String jobId);

	/**
	 * 初始化任务执行时最初始的日志记录数据
	 * @param jobName  执行任务名称
	 * @return
	 */
	public abstract Object[] initLog(String jobName);

	/**
	 * 任务执行完毕后，将处理结果更新的日志操作结果中
	 * @param res       任务处理结果; true: 成功， false: 失败
	 * @param logId     日志表主键ID
	 * @param jobName   任务名称
	 * @return
	 */
	public abstract Object[] initResLog(boolean res,String logId,String jobName);

	public void addSql(String sqlId,String sql){
		this.sqlMap.put(sqlId,sql);
	}

	public void addSqlValue(String sqlId,Object[] values){
		this.sqlValueMap.put(sqlId, values);
	}

	public void initResultMap(String filed,Class dataType){
		this.resultMap.put(filed,dataType);
	}

	public String getScheduleSql(String sqlId){
		return this.sqlMap.get(sqlId);
	}

	public Object[] getScheduleSqlValue(String sqlId){
		return this.sqlValueMap.get(sqlId);
	}

	public Map<String, Class<?>> resultMap(){
		return this.resultMap;
	}

	public Class<? extends ScheduleTaskEntity> getScheduleTaskEntity() {
		return scheduleTaskEntity;
	}

	public void setScheduleTaskEntity(Class<? extends ScheduleTaskEntity> scheduleTaskEntity) {
		this.scheduleTaskEntity = scheduleTaskEntity;
	}
}
