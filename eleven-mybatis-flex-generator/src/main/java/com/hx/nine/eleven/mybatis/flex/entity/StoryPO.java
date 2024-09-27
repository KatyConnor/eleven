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
@Table(value = "zt_story", schema = "zentao")
public class StoryPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer parent;

    private Integer product;

    private Integer branch;

    private Integer module;

    private String plan;

    private String source;

    @Column("sourceNote")
    private String sourceNote;

    @Column("fromBug")
    private Integer fromBug;

    private String title;

    private String keywords;

    private String type;

    private Integer pri;

    private Float estimate;

    private String status;

    @Column("subStatus")
    private String subStatus;

    private String color;

    private String stage;

    @Column("stagedBy")
    private String stagedBy;

    private String mailto;

    @Column("openedBy")
    private String openedBy;

    @Column("openedDate")
    private LocalDateTime openedDate;

    @Column("assignedTo")
    private String assignedTo;

    @Column("assignedDate")
    private LocalDateTime assignedDate;

    @Column("lastEditedBy")
    private String lastEditedBy;

    @Column("lastEditedDate")
    private LocalDateTime lastEditedDate;

    @Column("reviewedBy")
    private String reviewedBy;

    @Column("reviewedDate")
    private Date reviewedDate;

    @Column("closedBy")
    private String closedBy;

    @Column("closedDate")
    private LocalDateTime closedDate;

    @Column("closedReason")
    private String closedReason;

    @Column("toBug")
    private Integer toBug;

    @Column("childStories")
    private String childStories;

    @Column("linkStories")
    private String linkStories;

    @Column("duplicateStory")
    private Integer duplicateStory;

    private Integer version;

    private String urchanged;

    private String deleted;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getBranch() {
        return branch;
    }

    public void setBranch(Integer branch) {
        this.branch = branch;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceNote() {
        return sourceNote;
    }

    public void setSourceNote(String sourceNote) {
        this.sourceNote = sourceNote;
    }

    public Integer getFromBug() {
        return fromBug;
    }

    public void setFromBug(Integer fromBug) {
        this.fromBug = fromBug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public String getMailto() {
        return mailto;
    }

    public void setMailto(String mailto) {
        this.mailto = mailto;
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

    public String getClosedReason() {
        return closedReason;
    }

    public void setClosedReason(String closedReason) {
        this.closedReason = closedReason;
    }

    public Integer getToBug() {
        return toBug;
    }

    public void setToBug(Integer toBug) {
        this.toBug = toBug;
    }

    public String getChildStories() {
        return childStories;
    }

    public void setChildStories(String childStories) {
        this.childStories = childStories;
    }

    public String getLinkStories() {
        return linkStories;
    }

    public void setLinkStories(String linkStories) {
        this.linkStories = linkStories;
    }

    public Integer getDuplicateStory() {
        return duplicateStory;
    }

    public void setDuplicateStory(Integer duplicateStory) {
        this.duplicateStory = duplicateStory;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUrchanged() {
        return urchanged;
    }

    public void setUrchanged(String urchanged) {
        this.urchanged = urchanged;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
