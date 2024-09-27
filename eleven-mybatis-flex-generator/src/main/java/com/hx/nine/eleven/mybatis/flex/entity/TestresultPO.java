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
@Table(value = "zt_testresult", schema = "zentao")
public class TestresultPO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private Integer run;

    private Integer cases;

    private Integer version;

    private Integer job;

    private Integer compile;

    @Column("caseResult")
    private String caseResult;

    @Column("stepResults")
    private String stepResults;

    @Column("lastRunner")
    private String lastRunner;

    private LocalDateTime date;

    private Float duration;

    private String xml;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRun() {
        return run;
    }

    public void setRun(Integer run) {
        this.run = run;
    }

    public Integer getCases() {
        return cases;
    }

    public void setCase(Integer cases) {
        this.cases = cases;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Integer getJob() {
        return job;
    }

    public void setJob(Integer job) {
        this.job = job;
    }

    public Integer getCompile() {
        return compile;
    }

    public void setCompile(Integer compile) {
        this.compile = compile;
    }

    public String getCaseResult() {
        return caseResult;
    }

    public void setCaseResult(String caseResult) {
        this.caseResult = caseResult;
    }

    public String getStepResults() {
        return stepResults;
    }

    public void setStepResults(String stepResults) {
        this.stepResults = stepResults;
    }

    public String getLastRunner() {
        return lastRunner;
    }

    public void setLastRunner(String lastRunner) {
        this.lastRunner = lastRunner;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

}
