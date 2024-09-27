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
@Table(value = "zt_job", schema = "zentao")
public class JobPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String name;

    private Integer repo;

    private Integer product;

    private String frame;

    @Column("jkHost")
    private Integer jkHost;

    @Column("jkJob")
    private String jkJob;

    @Column("triggerType")
    private String triggerType;

    @Column("svnDir")
    private String svnDir;

    @Column("atDay")
    private String atDay;

    @Column("atTime")
    private String atTime;

    @Column("customParam")
    private String customParam;

    private String comment;

    @Column("createdBy")
    private String createdBy;

    @Column("createdDate")
    private LocalDateTime createdDate;

    @Column("editedBy")
    private String editedBy;

    @Column("editedDate")
    private LocalDateTime editedDate;

    @Column("lastExec")
    private LocalDateTime lastExec;

    @Column("lastStatus")
    private String lastStatus;

    @Column("lastTag")
    private String lastTag;

    private String deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRepo() {
        return repo;
    }

    public void setRepo(Integer repo) {
        this.repo = repo;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public Integer getJkHost() {
        return jkHost;
    }

    public void setJkHost(Integer jkHost) {
        this.jkHost = jkHost;
    }

    public String getJkJob() {
        return jkJob;
    }

    public void setJkJob(String jkJob) {
        this.jkJob = jkJob;
    }

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getSvnDir() {
        return svnDir;
    }

    public void setSvnDir(String svnDir) {
        this.svnDir = svnDir;
    }

    public String getAtDay() {
        return atDay;
    }

    public void setAtDay(String atDay) {
        this.atDay = atDay;
    }

    public String getAtTime() {
        return atTime;
    }

    public void setAtTime(String atTime) {
        this.atTime = atTime;
    }

    public String getCustomParam() {
        return customParam;
    }

    public void setCustomParam(String customParam) {
        this.customParam = customParam;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(String editedBy) {
        this.editedBy = editedBy;
    }

    public LocalDateTime getEditedDate() {
        return editedDate;
    }

    public void setEditedDate(LocalDateTime editedDate) {
        this.editedDate = editedDate;
    }

    public LocalDateTime getLastExec() {
        return lastExec;
    }

    public void setLastExec(LocalDateTime lastExec) {
        this.lastExec = lastExec;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(String lastStatus) {
        this.lastStatus = lastStatus;
    }

    public String getLastTag() {
        return lastTag;
    }

    public void setLastTag(String lastTag) {
        this.lastTag = lastTag;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
