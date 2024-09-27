package com.hx.nine.eleven.mybatis.flex.entity;

import com.mybatisflex.annotation.Table;
import java.io.Serializable;



/**
 *  实体类。
 *
 * @author wml
 * @since 2024-09-24
 */
@Table(value = "zt_suitecase", schema = "zentao")
public class SuitecasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer suite;

    private Integer product;

    private Integer cases;

    private Integer version;

    public Integer getSuite() {
        return suite;
    }

    public void setSuite(Integer suite) {
        this.suite = suite;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }

    public Integer getCases() {
        return cases;
    }

    public void setCases(Integer cases) {
        this.cases = cases;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
