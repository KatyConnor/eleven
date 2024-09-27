package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.time.LocalDateTime;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_testrun", schema = "zentao")
public class TestrunPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer task;

    private Integer cases;

    private Integer version;

    @Column("assignedTo")
    private String assignedTo;

    @Column("lastRunner")
    private String lastRunner;

    @Column("lastRunDate")
    private LocalDateTime lastRunDate;

    @Column("lastRunResult")
    private String lastRunResult;

    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getCases() {
        return cases;
    }

    public void setCases(Integer cases) {
        this.cases = cases;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getLastRunner() {
        return lastRunner;
    }

    public void setLastRunner(String lastRunner) {
        this.lastRunner = lastRunner;
    }

    public LocalDateTime getLastRunDate() {
        return lastRunDate;
    }

    public void setLastRunDate(LocalDateTime lastRunDate) {
        this.lastRunDate = lastRunDate;
    }

    public String getLastRunResult() {
        return lastRunResult;
    }

    public void setLastRunResult(String lastRunResult) {
        this.lastRunResult = lastRunResult;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
