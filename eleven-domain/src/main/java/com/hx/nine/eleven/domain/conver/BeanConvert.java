package com.hx.nine.eleven.domain.conver;

import com.hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import com.hx.nine.eleven.domain.obj.dto.PageHeaderDTO;
import com.hx.nine.eleven.domain.obj.form.PageHeaderForm;
import com.hx.nine.eleven.domain.obj.param.BaseParam;
import com.hx.nine.eleven.domain.obj.vo.PageResponseHeaderVO;
import com.hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import com.hx.nine.eleven.domain.obj.dto.DefaultHeaderDTO;
import com.hx.nine.eleven.domain.obj.form.DefaultHeaderForm;
import com.hx.nine.eleven.domain.obj.param.HeaderParam;
import com.hx.nine.eleven.domain.obj.vo.ResponseHeaderVO;
import com.hx.nine.eleven.domain.web.DomainApplicationContainer;
import com.hx.nine.eleven.commons.utils.BeanUtils;
import com.hx.nine.eleven.commons.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class BeanConvert {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConvert.class);

    /**
     * @param obj
     * @param tradeCode
     * @param headerCode
     * @param type
     * @return
     * @TODO 后续考虑是否对报文头进行主交易、子交易区分支持
     */
    public static Object convert(Object obj, String tradeCode, String headerCode, String type) {
        if (!Optional.ofNullable(obj).isPresent()){
            return null;
        }
        Class<?> classzz = null;
        Object instance = null;
        classzz = DomainApplicationContainer.build().getClass(type + tradeCode);
        if (Optional.ofNullable(classzz).isPresent()) {
            instance = BeanUtils.newInstance(classzz);
            return BeanUtils.copyProperties(obj, instance);
        }
        if (WebRouteParamsEnums.HEADER_FORM == WebRouteParamsEnums.getByName(type)) {
            classzz = DomainApplicationContainer.build().getClass(type + headerCode);
            classzz = classzz == null?headerCode == null||StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    DefaultHeaderForm.class: PageHeaderForm.class:classzz;
            instance = BeanUtils.newInstance(classzz);
            return BeanUtils.copyProperties(obj, instance);
        }

        if (WebRouteParamsEnums.HEADER_DTO == WebRouteParamsEnums.getByName(type)) {
            classzz = DomainApplicationContainer.build().getClass(type + headerCode);
            classzz = classzz == null?headerCode == null||StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    DefaultHeaderDTO.class: PageHeaderDTO.class:classzz;
            instance = BeanUtils.newInstance(classzz);
            return BeanUtils.copyProperties(obj, instance);
        }

        if (WebRouteParamsEnums.HEADER_PARAM == WebRouteParamsEnums.getByName(type)) {
            classzz = DomainApplicationContainer.build().getClass(type + headerCode);
            classzz = classzz == null?headerCode == null|| StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    BaseParam.class:HeaderParam.class:classzz;
            instance = BeanUtils.newInstance(classzz);
            return BeanUtils.copyProperties(obj, instance);
        }

        if (WebRouteParamsEnums.HEADER_VO == WebRouteParamsEnums.getByName(type)) {
            classzz = DomainApplicationContainer.build().getClass(type + headerCode);
            classzz = classzz == null?headerCode == null||StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    ResponseHeaderVO.class: PageResponseHeaderVO.class:classzz;
            instance = BeanUtils.newInstance(classzz);
            return BeanUtils.copyProperties(obj, instance);
        }
        return null;
    }
}
