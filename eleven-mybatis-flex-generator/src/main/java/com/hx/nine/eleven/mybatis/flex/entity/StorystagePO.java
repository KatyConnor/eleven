package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_storystage", schema = "zentao")
public class StorystagePO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer story;

    private Integer branch;

    private String stage;

    @Column("stagedBy")
    private String stagedBy;

    public Integer getStory() {
        return story;
    }

    public void setStory(Integer story) {
        this.story = story;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getStagedBy() {
        return stagedBy;
    }

    public void setStagedBy(String stagedBy) {
        this.stagedBy = stagedBy;
    }

}
