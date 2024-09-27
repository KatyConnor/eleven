package com.hx.nine.eleven.mybatis.flex.entity.user.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;


/**
 *  表定义层。
 *
 * @author wml
 * @since 2024-09-24
 */
public class TeamTableDef extends TableDef {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final TeamTableDef TEAM_PO = new TeamTableDef();

    
    public final QueryColumn ID = new QueryColumn(this, "id");

    
    public final QueryColumn DAYS = new QueryColumn(this, "days");

    
    public final QueryColumn JOIN = new QueryColumn(this, "join");

    
    public final QueryColumn LEFT = new QueryColumn(this, "left");

    
    public final QueryColumn ROLE = new QueryColumn(this, "role");

    
    public final QueryColumn ROOT = new QueryColumn(this, "root");

    
    public final QueryColumn TYPE = new QueryColumn(this, "type");

    
    public final QueryColumn HOURS = new QueryColumn(this, "hours");

    
    public final QueryColumn ORDER = new QueryColumn(this, "order");

    
    public final QueryColumn ACCOUNT = new QueryColumn(this, "account");

    
    public final QueryColumn LIMITED = new QueryColumn(this, "limited");

    
    public final QueryColumn CONSUMED = new QueryColumn(this, "consumed");

    
    public final QueryColumn ESTIMATE = new QueryColumn(this, "estimate");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, ROOT, TYPE, ACCOUNT, ROLE, LIMITED, JOIN, DAYS, HOURS, ESTIMATE, CONSUMED, LEFT, ORDER};

    public TeamTableDef() {
        super("zentao", "zt_team");
    }

    private TeamTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public TeamTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new TeamTableDef("zentao", "zt_team", alias));
    }

}
