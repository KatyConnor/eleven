package com.hx.nine.eleven.mybatis.flex.entity.user.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;


/**
 *  表定义层。
 *
 * @author wml
 * @since 2024-09-24
 */
public class ProjectTableDef extends TableDef {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final ProjectTableDef PROJECT_PO = new ProjectTableDef();

    
    public final QueryColumn ID = new QueryColumn(this, "id");

    
    public final QueryColumn PM = new QueryColumn(this, "PM");

    
    public final QueryColumn PO = new QueryColumn(this, "PO");

    
    public final QueryColumn QD = new QueryColumn(this, "QD");

    
    public final QueryColumn RD = new QueryColumn(this, "RD");

    
    public final QueryColumn ACL = new QueryColumn(this, "acl");

    
    public final QueryColumn END = new QueryColumn(this, "end");

    
    public final QueryColumn PRI = new QueryColumn(this, "pri");

    
    public final QueryColumn AUTH = new QueryColumn(this, "auth");

    
    public final QueryColumn CODE = new QueryColumn(this, "code");

    
    public final QueryColumn DAYS = new QueryColumn(this, "days");

    
    public final QueryColumn DESC = new QueryColumn(this, "desc");

    
    public final QueryColumn NAME = new QueryColumn(this, "name");

    
    public final QueryColumn PATH = new QueryColumn(this, "path");

    
    public final QueryColumn TEAM = new QueryColumn(this, "team");

    
    public final QueryColumn TYPE = new QueryColumn(this, "type");

    
    public final QueryColumn BEGIN = new QueryColumn(this, "begin");

    
    public final QueryColumn GRADE = new QueryColumn(this, "grade");

    
    public final QueryColumn MODEL = new QueryColumn(this, "model");

    
    public final QueryColumn ORDER = new QueryColumn(this, "order");

    
    public final QueryColumn BUDGET = new QueryColumn(this, "budget");

    
    public final QueryColumn OUTPUT = new QueryColumn(this, "output");

    
    public final QueryColumn PARENT = new QueryColumn(this, "parent");

    
    public final QueryColumn STATGE = new QueryColumn(this, "statge");

    
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    
    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    
    public final QueryColumn PERCENT = new QueryColumn(this, "percent");

    
    public final QueryColumn PRODUCT = new QueryColumn(this, "product");

    
    public final QueryColumn PROJECT = new QueryColumn(this, "project");

    
    public final QueryColumn REAL_END = new QueryColumn(this, "realEnd");

    
    public final QueryColumn VERSION = new QueryColumn(this, "version");

    
    public final QueryColumn CLOSED_BY = new QueryColumn(this, "closedBy");

    
    public final QueryColumn LIFETIME = new QueryColumn(this, "lifetime");

    
    public final QueryColumn OPENED_BY = new QueryColumn(this, "openedBy");

    
    public final QueryColumn ATTRIBUTE = new QueryColumn(this, "attribute");

    
    public final QueryColumn MILESTONE = new QueryColumn(this, "milestone");

    
    public final QueryColumn REAL_BEGAN = new QueryColumn(this, "realBegan");

    
    public final QueryColumn SUB_STATUS = new QueryColumn(this, "subStatus");

    
    public final QueryColumn WHITELIST = new QueryColumn(this, "whitelist");

    
    public final QueryColumn BUDGET_UNIT = new QueryColumn(this, "budgetUnit");

    
    public final QueryColumn CANCELED_BY = new QueryColumn(this, "canceledBy");

    
    public final QueryColumn CLOSED_DATE = new QueryColumn(this, "closedDate");

    
    public final QueryColumn OPENED_DATE = new QueryColumn(this, "openedDate");

    
    public final QueryColumn CANCELED_DATE = new QueryColumn(this, "canceledDate");

    
    public final QueryColumn LAST_EDITED_BY = new QueryColumn(this, "lastEditedBy");

    
    public final QueryColumn PLAN_DURATION = new QueryColumn(this, "planDuration");

    
    public final QueryColumn REAL_DURATION = new QueryColumn(this, "realDuration");

    
    public final QueryColumn STORY_CONCEPT = new QueryColumn(this, "storyConcept");

    
    public final QueryColumn OPENED_VERSION = new QueryColumn(this, "openedVersion");

    
    public final QueryColumn PARENT_VERSION = new QueryColumn(this, "parentVersion");

    
    public final QueryColumn LAST_EDITED_DATE = new QueryColumn(this, "lastEditedDate");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, PROJECT, MODEL, TYPE, PRODUCT, LIFETIME, BUDGET, BUDGET_UNIT, ATTRIBUTE, PERCENT, MILESTONE, OUTPUT, AUTH, PARENT, PATH, GRADE, NAME, CODE, BEGIN, END, REAL_BEGAN, REAL_END, DAYS, STORY_CONCEPT, STATUS, SUB_STATUS, STATGE, PRI, DESC, VERSION, PARENT_VERSION, PLAN_DURATION, REAL_DURATION, OPENED_BY, OPENED_DATE, OPENED_VERSION, LAST_EDITED_BY, LAST_EDITED_DATE, CLOSED_BY, CLOSED_DATE, CANCELED_BY, CANCELED_DATE, PO, PM, QD, RD, TEAM, ACL, WHITELIST, ORDER, DELETED};

    public ProjectTableDef() {
        super("zentao", "zt_project");
    }

    private ProjectTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public ProjectTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new ProjectTableDef("zentao", "zt_project", alias));
    }

}
