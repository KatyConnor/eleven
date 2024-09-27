package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_taskspec", schema = "zentao")
public class TaskspecPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer task;

    private Integer version;

    private String name;

    @Column("estStarted")
    private Date estStarted;

    private Date deadline;

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getEstStarted() {
        return estStarted;
    }

    public void setEstStarted(Date estStarted) {
        this.estStarted = estStarted;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

}
