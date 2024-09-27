package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_userview", schema = "zentao")
public class UserviewPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String account;

    private String programs;

    private String products;

    private String projects;

    private String sprints;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPrograms() {
        return programs;
    }

    public void setPrograms(String programs) {
        this.programs = programs;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getSprints() {
        return sprints;
    }

    public void setSprints(String sprints) {
        this.sprints = sprints;
    }

}
