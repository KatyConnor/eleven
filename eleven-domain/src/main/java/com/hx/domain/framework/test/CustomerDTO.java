package com.hx.domain.framework.test;

import com.hx.domain.framework.obj.dto.BaseDTO;
import com.hx.lang.commons.annotation.FieldList;

import java.util.List;

public class CustomerDTO extends BaseDTO {

    /**
     * 目的用户ID
     */
    private String destUserId;
    /**
     * 目的应用ID
     */
    private String destAppId;
    /**
     * 交易码
     */
    private String secTxCode;

    private CustomerChannelDTO customerChannelDTO;

    @FieldList(convertType = CustomerChannelDTO.class)
    private List<CustomerChannelDTO> list;

    public CustomerDTO() {
    }

    public CustomerDTO(String destUserId, String destAppId, String secTxCode) {
        this.destUserId = destUserId;
        this.destAppId = destAppId;
        this.secTxCode = secTxCode;
    }

    public String getDestUserId() {
        return destUserId;
    }

    public void setDestUserId(String destUserId) {
        this.destUserId = destUserId;
    }

    public String getDestAppId() {
        return destAppId;
    }

    public void setDestAppId(String destAppId) {
        this.destAppId = destAppId;
    }

    public String getSecTxCode() {
        return secTxCode;
    }

    public void setSecTxCode(String secTxCode) {
        this.secTxCode = secTxCode;
    }

    public CustomerChannelDTO getCustomerChannelDTO() {
        return customerChannelDTO;
    }

    public void setCustomerChannelDTO(CustomerChannelDTO customerChannelDTO) {
        this.customerChannelDTO = customerChannelDTO;
    }

    public List<CustomerChannelDTO> getList() {
        return list;
    }

    public void setList(List<CustomerChannelDTO> list) {
        this.list = list;
    }
}
