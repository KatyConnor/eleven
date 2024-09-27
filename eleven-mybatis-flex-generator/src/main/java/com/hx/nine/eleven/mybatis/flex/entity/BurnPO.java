package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_burn", schema = "zentao")
public class BurnPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer execution;

    private Integer product;

    @Id
    private Integer task;

    @Id
    private Date date;

    private Float estimate;

    private Float left;

    private Float consumed;

    @Column("storyPoint")
    private Float storyPoint;

    public Integer getExecution() {
        return execution;
    }

    public void setExecution(Integer execution) {
        this.execution = execution;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getEstimate() {
        return estimate;
    }

    public void setEstimate(Float estimate) {
        this.estimate = estimate;
    }

    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Float getConsumed() {
        return consumed;
    }

    public void setConsumed(Float consumed) {
        this.consumed = consumed;
    }

    public Float getStoryPoint() {
        return storyPoint;
    }

    public void setStoryPoint(Float storyPoint) {
        this.storyPoint = storyPoint;
    }

}
