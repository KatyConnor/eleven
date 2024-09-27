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
@Table("zt_project")
public class ProjectPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer project;

    private String model;

    private String type;

    private String product;

    private String lifetime;

    private String budget;

    @Column("budgetUnit")
    private String budgetUnit;

    private String attribute;

    private Float percent;

    private String milestone;

    private String output;

    private String auth;

    private Integer parent;

    private String path;

    private Integer grade;

    private String name;

    private String code;

    private Date begin;

    private Date end;

    @Column("realBegan")
    private Date realBegan;

    @Column("realEnd")
    private Date realEnd;

    private Integer days;

    @Column("storyConcept")
    private Integer storyConcept;

    private String status;

    @Column("subStatus")
    private String subStatus;

    private String statge;

    private String pri;

    private String desc;

    private Integer version;

    @Column("parentVersion")
    private Integer parentVersion;

    @Column("planDuration")
    private Integer planDuration;

    @Column("realDuration")
    private Integer realDuration;

    @Column("openedBy")
    private String openedBy;

    @Column("openedDate")
    private LocalDateTime openedDate;

    @Column("openedVersion")
    private String openedVersion;

    @Column("lastEditedBy")
    private String lastEditedBy;

    @Column("lastEditedDate")
    private LocalDateTime lastEditedDate;

    @Column("closedBy")
    private String closedBy;

    @Column("closedDate")
    private LocalDateTime closedDate;

    @Column("canceledBy")
    private String canceledBy;

    @Column("canceledDate")
    private LocalDateTime canceledDate;

    private String po;

    private String pm;

    private String qd;

    private String rd;

    private String team;

    private String acl;

    private String whitelist;

    private Integer order;

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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getLifetime() {
        return lifetime;
    }

    public void setLifetime(String lifetime) {
        this.lifetime = lifetime;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBudgetUnit() {
        return budgetUnit;
    }

    public void setBudgetUnit(String budgetUnit) {
        this.budgetUnit = budgetUnit;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getRealBegan() {
        return realBegan;
    }

    public void setRealBegan(Date realBegan) {
        this.realBegan = realBegan;
    }

    public Date getRealEnd() {
        return realEnd;
    }

    public void setRealEnd(Date realEnd) {
        this.realEnd = realEnd;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getStoryConcept() {
        return storyConcept;
    }

    public void setStoryConcept(Integer storyConcept) {
        this.storyConcept = storyConcept;
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

    public String getStatge() {
        return statge;
    }

    public void setStatge(String statge) {
        this.statge = statge;
    }

    public String getPri() {
        return pri;
    }

    public void setPri(String pri) {
        this.pri = pri;
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

    public Integer getParentVersion() {
        return parentVersion;
    }

    public void setParentVersion(Integer parentVersion) {
        this.parentVersion = parentVersion;
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

    public String getOpenedVersion() {
        return openedVersion;
    }

    public void setOpenedVersion(String openedVersion) {
        this.openedVersion = openedVersion;
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

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getQd() {
        return qd;
    }

    public void setQd(String qd) {
        this.qd = qd;
    }

    public String getRd() {
        return rd;
    }

    public void setRd(String rd) {
        this.rd = rd;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public String getWhitelist() {
        return whitelist;
    }

    public void setWhitelist(String whitelist) {
        this.whitelist = whitelist;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

}
