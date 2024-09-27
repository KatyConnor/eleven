package com.hx.nine.eleven.mybatis.flex.entity.user.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;


/**
 *  表定义层。
 *
 * @author wml
 * @since 2024-09-24
 */
public class UserTableDef extends TableDef {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public static final UserTableDef USER_PO = new UserTableDef();

    
    public final QueryColumn ID = new QueryColumn(this, "id");

    
    public final QueryColumn IP = new QueryColumn(this, "ip");

    
    public final QueryColumn QQ = new QueryColumn(this, "qq");

    
    public final QueryColumn DEPT = new QueryColumn(this, "dept");

    
    public final QueryColumn JOIN = new QueryColumn(this, "join");

    
    public final QueryColumn LAST = new QueryColumn(this, "last");

    
    public final QueryColumn ROLE = new QueryColumn(this, "role");

    
    public final QueryColumn TYPE = new QueryColumn(this, "type");

    
    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    
    public final QueryColumn FAILS = new QueryColumn(this, "fails");

    
    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    
    public final QueryColumn SCORE = new QueryColumn(this, "score");

    
    public final QueryColumn SKYPE = new QueryColumn(this, "skype");

    
    public final QueryColumn SLACK = new QueryColumn(this, "slack");

    
    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    
    public final QueryColumn GENDER = new QueryColumn(this, "gender");

    
    public final QueryColumn LOCKED = new QueryColumn(this, "locked");

    
    public final QueryColumn MOBILE = new QueryColumn(this, "mobile");

    
    public final QueryColumn NATURE = new QueryColumn(this, "nature");

    
    public final QueryColumn RANZHI = new QueryColumn(this, "ranzhi");

    
    public final QueryColumn VISITS = new QueryColumn(this, "visits");

    
    public final QueryColumn WEIXIN = new QueryColumn(this, "weixin");

    
    public final QueryColumn ACCOUNT = new QueryColumn(this, "account");

    
    public final QueryColumn ADDRESS = new QueryColumn(this, "address");

    
    public final QueryColumn COMPANY = new QueryColumn(this, "company");

    
    public final QueryColumn DELETED = new QueryColumn(this, "deleted");

    
    public final QueryColumn ZIPCODE = new QueryColumn(this, "zipcode");

    
    public final QueryColumn ANALYSIS = new QueryColumn(this, "analysis");

    
    public final QueryColumn BIRTHDAY = new QueryColumn(this, "birthday");

    
    public final QueryColumn COMMITER = new QueryColumn(this, "commiter");

    
    public final QueryColumn DINGDING = new QueryColumn(this, "dingding");

    
    public final QueryColumn NICKNAME = new QueryColumn(this, "nickname");

    
    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    
    public final QueryColumn REALNAME = new QueryColumn(this, "realname");

    
    public final QueryColumn STRATEGY = new QueryColumn(this, "strategy");

    
    public final QueryColumn WHATSAPP = new QueryColumn(this, "whatsapp");

    
    public final QueryColumn CLIENT_LANG = new QueryColumn(this, "clientLang");

    
    public final QueryColumn SCORE_LEVEL = new QueryColumn(this, "scoreLevel");

    
    public final QueryColumn CLIENT_STATUS = new QueryColumn(this, "clientStatus");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, COMPANY, TYPE, DEPT, ACCOUNT, PASSWORD, ROLE, REALNAME, NICKNAME, COMMITER, AVATAR, BIRTHDAY, GENDER, EMAIL, SKYPE, QQ, MOBILE, PHONE, WEIXIN, DINGDING, SLACK, WHATSAPP, ADDRESS, ZIPCODE, NATURE, ANALYSIS, STRATEGY, JOIN, VISITS, IP, LAST, FAILS, LOCKED, RANZHI, SCORE, SCORE_LEVEL, DELETED, CLIENT_STATUS, CLIENT_LANG};

    public UserTableDef() {
        super("zentao", "zt_user");
    }

    private UserTableDef(String schema, String name, String alisa) {
        super(schema, name, alisa);
    }

    public UserTableDef as(String alias) {
        String key = getNameWithSchema() + "." + alias;
        return getCache(key, k -> new UserTableDef("zentao", "zt_user", alias));
    }

}
