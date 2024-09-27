package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_case", schema = "zentao")
public class CasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer project;

    private Integer product;

    private Integer execution;

    private Integer branch;

    private Integer lib;

    private Integer module;

    private Integer path;

    private Integer story;

    @Column("storyVersion")
    private Integer storyVersion;

    private String title;

    private String precondition;

    private String keywords;

    private Integer pri;

    private String type;

    private String auto;

    private String frame;

    private String stage;

    @Column("howRun")
    private String howRun;

    @Column("scriptedBy")
    private String scriptedBy;

    @Column("scriptedDate")
    private Date scriptedDate;

    @Column("scriptStatus")
    private String scriptStatus;

    @Column("scriptLocation")
    private String scriptLocation;

    private String status;

    @Column("subStatus")
    private String subStatus;

    private String color;

    private String frequency;

    private Integer order;

    @Column("openedBy")
    private String openedBy;

    @Column("openedDate")
    private LocalDateTime openedDate;

    @Column("reviewedBy")
    private String reviewedBy;

    @Column("reviewedDate")
    private Date reviewedDate;

    @Column("lastEditedBy")
    private String lastEditedBy;

    @Column("lastEditedDate")
    private LocalDateTime lastEditedDate;

    private Integer version;

    @Column("linkCase")
    private String linkCase;

    @Column("fromBug")
    private Integer fromBug;

    @Column("fromCaseID")
    private Integer fromCaseID;

    @Column("fromCaseVersion")
    private Integer fromCaseVersion;

    private String deleted;

    @Column("lastRunner")
    private String lastRunner;

    @Column("lastRunDate")
    private LocalDateTime lastRunDate;

    @Column("lastRunResult")
    private String lastRunResult;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Integer getExecution() {
        return execution;
    }

    public void setExecution(Integer execution) {
        this.execution = execution;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public Integer getLib() {
        return lib;
    }

    public void setLib(Integer lib) {
        this.lib = lib;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getPath() {
        return path;
    }

    public void setPath(Integer path) {
        this.path = path;
    }

    public Integer getStory() {
        return story;
    }

    public void setStory(Integer story) {
        this.story = story;
    }

    public Integer getStoryVersion() {
        return storyVersion;
    }

    public void setStoryVersion(Integer storyVersion) {
        this.storyVersion = storyVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrecondition() {
        return precondition;
    }

    public void setPrecondition(String precondition) {
        this.precondition = precondition;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public Integer getPri() {
        return pri;
    }

    public void setPri(Integer pri) {
        this.pri = pri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getHowRun() {
        return howRun;
    }

    public void setHowRun(String howRun) {
        this.howRun = howRun;
    }

    public String getScriptedBy() {
        return scriptedBy;
    }

    public void setScriptedBy(String scriptedBy) {
        this.scriptedBy = scriptedBy;
    }

    public Date getScriptedDate() {
        return scriptedDate;
    }

    public void setScriptedDate(Date scriptedDate) {
        this.scriptedDate = scriptedDate;
    }

    public String getScriptStatus() {
        return scriptStatus;
    }

    public void setScriptStatus(String scriptStatus) {
        this.scriptStatus = scriptStatus;
    }

    public String getScriptLocation() {
        return scriptLocation;
    }

    public void setScriptLocation(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(String openedBy) {
        this.openedBy = openedBy;
    }

    public LocalDateTime getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDateTime openedDate) {
        this.openedDate = openedDate;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Date getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(Date reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public String getLastEditedBy() {
        return lastEditedBy;
    }

    public void setLastEditedBy(String lastEditedBy) {
        this.lastEditedBy = lastEditedBy;
    }

    public LocalDateTime getLastEditedDate() {
        return lastEditedDate;
    }

    public void setLastEditedDate(LocalDateTime lastEditedDate) {
        this.lastEditedDate = lastEditedDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getLinkCase() {
        return linkCase;
    }

    public void setLinkCase(String linkCase) {
        this.linkCase = linkCase;
    }

    public Integer getFromBug() {
        return fromBug;
    }

    public void setFromBug(Integer fromBug) {
        this.fromBug = fromBug;
    }

    public Integer getFromCaseID() {
        return fromCaseID;
    }

    public void setFromCaseID(Integer fromCaseID) {
        this.fromCaseID = fromCaseID;
    }

    public Integer getFromCaseVersion() {
        return fromCaseVersion;
    }

    public void setFromCaseVersion(Integer fromCaseVersion) {
        this.fromCaseVersion = fromCaseVersion;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
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

}
