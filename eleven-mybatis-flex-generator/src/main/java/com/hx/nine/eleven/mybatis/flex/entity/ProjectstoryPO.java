package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_projectstory", schema = "zentao")
public class ProjectstoryPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer project;

    private Integer product;

    private Integer story;

    private Integer version;

    private Integer order;

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getStory() {
        return story;
    }

    public void setStory(Integer story) {
        this.story = story;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

}
