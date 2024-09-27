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
@Table(value = "zt_im_conference", schema = "zentao")
public class ImConferencePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(keyType = KeyType.Auto)
    private Integer id;

    private String rid;

    private String cgid;

    private String status;

    private String participants;

    @Column("openedBy")
    private Integer openedBy;

    @Column("openedDate")
    private LocalDateTime openedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getCgid() {
        return cgid;
    }

    public void setCgid(String cgid) {
        this.cgid = cgid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }

    public Integer getOpenedBy() {
        return openedBy;
    }

    public void setOpenedBy(Integer openedBy) {
        this.openedBy = openedBy;
    }

    public LocalDateTime getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDateTime openedDate) {
        this.openedDate = openedDate;
    }

}
