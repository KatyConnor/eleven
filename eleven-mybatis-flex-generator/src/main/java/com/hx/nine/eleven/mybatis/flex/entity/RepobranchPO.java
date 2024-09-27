package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_repobranch", schema = "zentao")
public class RepobranchPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer repo;

    private Integer revision;

    private String branch;

    public Integer getRepo() {
        return repo;
    }

    public void setRepo(Integer repo) {
        this.repo = repo;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

}
