package hx.nine.eleven.jobtask.entity;

/**
 * 执行任务详细配置实体
 * @author wml
 * @Discription
 * @Date 2023-06-05
 */
public class ScheduleTaskEntity extends TaskEntity{

    // 主键ID
    private String id;
    // 任务ID
    private String jobLogId;
    // 任务名称
    private String name;
    // 任务执行状态
    private String status;
    // 任务状态，任务是否启用
    private Boolean taskStatus;
    // 任务执行时间表达式
    private String cron;
    // 任务执行关联实体
    private String execStatement;
    //  任务执行触发方法
    private String execMethod;
    // 执行时间
    private String complete;
    // 创建时间
    private String createTime;
    // 修改时间
    private String updateTime;
    // 版本号
    private Long version;
    // 数据是否有效
    private Boolean effective;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobLogId() {
        return jobLogId;
    }

    public void setJobLogId(String jobLogId) {
        this.jobLogId = jobLogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getExecStatement() {
        return execStatement;
    }

    public void setExecStatement(String execStatement) {
        this.execStatement = execStatement;
    }

    public String getExecMethod() {
        return execMethod;
    }

    public void setExecMethod(String execMethod) {
        this.execMethod = execMethod;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getEffective() {
        return effective;
    }

    public void setEffective(Boolean effective) {
        this.effective = effective;
    }
}
