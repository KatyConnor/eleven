package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_planstory", schema = "zentao")
public class PlanstoryPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer plan;

    private Integer story;

    private Integer order;

    public Integer getPlan() {
        return plan;
    }

    public void setPlan(Integer plan) {
        this.plan = plan;
    }

    public Integer getStory() {
        return story;
    }

    public void setStory(Integer story) {
        this.story = story;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
