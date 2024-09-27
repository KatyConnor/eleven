package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_oauth", schema = "zentao")
public class OauthPO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String account;

    @Column("openID")
    private String openID;

    @Column("providerType")
    private String providerType;

    @Column("providerID")
    private Integer providerID;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }

    public Integer getProviderID() {
        return providerID;
    }

    public void setProviderID(Integer providerID) {
        this.providerID = providerID;
    }

}
