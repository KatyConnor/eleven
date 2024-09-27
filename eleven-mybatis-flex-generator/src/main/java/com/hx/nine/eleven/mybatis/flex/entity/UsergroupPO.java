package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_usergroup", schema = "zentao")
public class UsergroupPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String account;

    private Integer group;

    private String project;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

}
