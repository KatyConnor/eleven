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
@Table(value = "zt_bug", schema = "zentao")
public class BugPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer project;

    private Integer product;

    private Integer branch;

    private Integer module;

    private Integer execution;

    private Integer plan;

    private Integer story;

    @Column("storyVersion")
    private Integer storyVersion;

    private Integer task;

    @Column("toTask")
    private Integer toTask;

    @Column("toStory")
    private Integer toStory;

    private String title;

    private String keywords;

    private Integer severity;

    private Integer pri;

    private String type;

    private String os;

    private String browser;

    private String hardware;

    private String found;

    private String steps;

    private String status;

    @Column("subStatus")
    private String subStatus;

    private String color;

    private Boolean confirmed;

    @Column("activatedCount")
    private Integer activatedCount;

    @Column("activatedDate")
    private LocalDateTime activatedDate;

    private String mailto;

    @Column("openedBy")
    private String openedBy;

    @Column("openedDate")
    private LocalDateTime openedDate;

    @Column("openedBuild")
    private String openedBuild;

    @Column("assignedTo")
    private String assignedTo;

    @Column("assignedDate")
    private LocalDateTime assignedDate;

    private Date deadline;

    @Column("resolvedBy")
    private String resolvedBy;

    private String resolution;

    @Column("resolvedBuild")
    private String resolvedBuild;

    @Column("resolvedDate")
    private LocalDateTime resolvedDate;

    @Column("closedBy")
    private String closedBy;

    @Column("closedDate")
    private LocalDateTime closedDate;

    @Column("duplicateBug")
    private Integer duplicateBug;

    @Column("linkBug")
    private String linkBug;

    private Integer cases;

    @Column("caseVersion")
    private Integer caseVersion;

    private Integer result;

    private Integer repo;

    private String entry;

    private String lines;

    private String v1;

    private String v2;

    @Column("repoType")
    private String repoType;

    private Integer testtask;

    @Column("lastEditedBy")
    private String lastEditedBy;

    @Column("lastEditedDate")
    private LocalDateTime lastEditedDate;

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

    public Integer getExecution() {
        return execution;
    }

    public void setExecution(Integer execution) {
        this.execution = execution;
    }

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

    public Integer getStoryVersion() {
        return storyVersion;
    }

    public void setStoryVersion(Integer storyVersion) {
        this.storyVersion = storyVersion;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    public Integer getToTask() {
        return toTask;
    }

    public void setToTask(Integer toTask) {
        this.toTask = toTask;
    }

    public Integer getToStory() {
        return toStory;
    }

    public void setToStory(Integer toStory) {
        this.toStory = toStory;
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

    public Integer getSeverity() {
        return severity;
    }

    public void setSeverity(Integer severity) {
        this.severity = severity;
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

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getFound() {
        return found;
    }

    public void setFound(String found) {
        this.found = found;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
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

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Integer getActivatedCount() {
        return activatedCount;
    }

    public void setActivatedCount(Integer activatedCount) {
        this.activatedCount = activatedCount;
    }

    public LocalDateTime getActivatedDate() {
        return activatedDate;
    }

    public void setActivatedDate(LocalDateTime activatedDate) {
        this.activatedDate = activatedDate;
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

    public String getOpenedBuild() {
        return openedBuild;
    }

    public void setOpenedBuild(String openedBuild) {
        this.openedBuild = openedBuild;
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

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(String resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getResolvedBuild() {
        return resolvedBuild;
    }

    public void setResolvedBuild(String resolvedBuild) {
        this.resolvedBuild = resolvedBuild;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
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

    public Integer getDuplicateBug() {
        return duplicateBug;
    }

    public void setDuplicateBug(Integer duplicateBug) {
        this.duplicateBug = duplicateBug;
    }

    public String getLinkBug() {
        return linkBug;
    }

    public void setLinkBug(String linkBug) {
        this.linkBug = linkBug;
    }

    public Integer getCases() {
        return cases;
    }

    public void setCases(Integer cases) {
        this.cases = cases;
    }

    public Integer getCaseVersion() {
        return caseVersion;
    }

    public void setCaseVersion(Integer caseVersion) {
        this.caseVersion = caseVersion;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public Integer getRepo() {
        return repo;
    }

    public void setRepo(Integer repo) {
        this.repo = repo;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getLines() {
        return lines;
    }

    public void setLines(String lines) {
        this.lines = lines;
    }

    public String getV1() {
        return v1;
    }

    public void setV1(String v1) {
        this.v1 = v1;
    }

    public String getV2() {
        return v2;
    }

    public void setV2(String v2) {
        this.v2 = v2;
    }

    public String getRepoType() {
        return repoType;
    }

    public void setRepoType(String repoType) {
        this.repoType = repoType;
    }

    public Integer getTesttask() {
        return testtask;
    }

    public void setTesttask(Integer testtask) {
        this.testtask = testtask;
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

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
