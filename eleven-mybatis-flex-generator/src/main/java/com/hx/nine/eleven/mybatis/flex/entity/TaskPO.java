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
@Table(value = "zt_task", schema = "zentao")
public class TaskPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer project;

    private Integer parent;

    private Integer execution;

    private Integer module;

    private Integer design;

    private Integer story;

    @Column("storyVersion")
    private Integer storyVersion;

    @Column("designVersion")
    private Integer designVersion;

    @Column("fromBug")
    private Integer fromBug;

    private String name;

    private String type;

    private Integer pri;

    private Float estimate;

    private Float consumed;

    private Float left;

    private Date deadline;

    private String status;

    @Column("subStatus")
    private String subStatus;

    private String color;

    private String mailto;

    private String desc;

    private Integer version;

    @Column("openedBy")
    private String openedBy;

    @Column("openedDate")
    private LocalDateTime openedDate;

    @Column("assignedTo")
    private String assignedTo;

    @Column("assignedDate")
    private LocalDateTime assignedDate;

    @Column("estStarted")
    private Date estStarted;

    @Column("realStarted")
    private LocalDateTime realStarted;

    @Column("finishedBy")
    private String finishedBy;

    @Column("finishedDate")
    private LocalDateTime finishedDate;

    @Column("finishedList")
    private String finishedList;

    @Column("canceledBy")
    private String canceledBy;

    @Column("canceledDate")
    private LocalDateTime canceledDate;

    @Column("closedBy")
    private String closedBy;

    @Column("closedDate")
    private LocalDateTime closedDate;

    @Column("planDuration")
    private Integer planDuration;

    @Column("realDuration")
    private Integer realDuration;

    @Column("closedReason")
    private String closedReason;

    @Column("lastEditedBy")
    private String lastEditedBy;

    @Column("lastEditedDate")
    private LocalDateTime lastEditedDate;

    @Column("activatedDate")
    private Date activatedDate;

    private String deleted;

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

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getExecution() {
        return execution;
    }

    public void setExecution(Integer execution) {
        this.execution = execution;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public Integer getDesign() {
        return design;
    }

    public void setDesign(Integer design) {
        this.design = design;
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

    public Integer getDesignVersion() {
        return designVersion;
    }

    public void setDesignVersion(Integer designVersion) {
        this.designVersion = designVersion;
    }

    public Integer getFromBug() {
        return fromBug;
    }

    public void setFromBug(Integer fromBug) {
        this.fromBug = fromBug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPri() {
        return pri;
    }

    public void setPri(Integer pri) {
        this.pri = pri;
    }

    public Float getEstimate() {
        return estimate;
    }

    public void setEstimate(Float estimate) {
        this.estimate = estimate;
    }

    public Float getConsumed() {
        return consumed;
    }

    public void setConsumed(Float consumed) {
        this.consumed = consumed;
    }

    public Float getLeft() {
        return left;
    }

    public void setLeft(Float left) {
        this.left = left;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
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

    public String getMailto() {
        return mailto;
    }

    public void setMailto(String mailto) {
        this.mailto = mailto;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getEstStarted() {
        return estStarted;
    }

    public void setEstStarted(Date estStarted) {
        this.estStarted = estStarted;
    }

    public LocalDateTime getRealStarted() {
        return realStarted;
    }

    public void setRealStarted(LocalDateTime realStarted) {
        this.realStarted = realStarted;
    }

    public String getFinishedBy() {
        return finishedBy;
    }

    public void setFinishedBy(String finishedBy) {
        this.finishedBy = finishedBy;
    }

    public LocalDateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getFinishedList() {
        return finishedList;
    }

    public void setFinishedList(String finishedList) {
        this.finishedList = finishedList;
    }

    public String getCanceledBy() {
        return canceledBy;
    }

    public void setCanceledBy(String canceledBy) {
        this.canceledBy = canceledBy;
    }

    public LocalDateTime getCanceledDate() {
        return canceledDate;
    }

    public void setCanceledDate(LocalDateTime canceledDate) {
        this.canceledDate = canceledDate;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public LocalDateTime getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(LocalDateTime closedDate) {
        this.closedDate = closedDate;
    }

    public Integer getPlanDuration() {
        return planDuration;
    }

    public void setPlanDuration(Integer planDuration) {
        this.planDuration = planDuration;
    }

    public Integer getRealDuration() {
        return realDuration;
    }

    public void setRealDuration(Integer realDuration) {
        this.realDuration = realDuration;
    }

    public String getClosedReason() {
        return closedReason;
    }

    public void setClosedReason(String closedReason) {
        this.closedReason = closedReason;
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

    public Date getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(Date activatedDate) {
        this.activatedDate = activatedDate;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
