package hx.nine.eleven.domain.conver;

import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.obj.dto.PageHeaderDTO;
import hx.nine.eleven.domain.obj.form.PageHeaderForm;
import hx.nine.eleven.domain.obj.param.BaseParam;
import hx.nine.eleven.domain.obj.vo.PageResponseHeaderVO;
import hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import hx.nine.eleven.domain.obj.dto.DefaultHeaderDTO;
import hx.nine.eleven.domain.obj.form.DefaultHeaderForm;
import hx.nine.eleven.domain.obj.param.HeaderParam;
import hx.nine.eleven.domain.obj.vo.ResponseHeaderVO;
import hx.nine.eleven.domain.web.DomainApplicationContainer;
import hx.nine.eleven.commons.utils.BeanUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * 对象转换
 * @author wml
 * @date 2022-10-28
 */
public class BeanConvert {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanConvert.class);

    /**
     * @param obj
     * @param tradeCode    主交易码+子交易码
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
        if (StringUtils.isNotEmpty(tradeCode)){
            classzz = DomainApplicationContainer.build().getClass(type + tradeCode);
            if (ObjectUtils.isEmpty(classzz)){
                if (WebRouteParamsEnums.BODY_PARAM != WebRouteParamsEnums.getByName(type)){
                    throw new RuntimeException(StringUtils.format("参数对象-[{}]--[{}]:主子交易没有找到对应的映射对象",type,tradeCode));
                }
                LOGGER.info("参数对象-[{}]--主子交易-[{}]，没有配置数据库[param]参数对象，如需要对数据库条件匹配操作，请设置[param]实体类",type,tradeCode);
            }
            instance = BeanUtils.newInstance(classzz);
            return BeanUtils.copyProperties(obj, instance);
        }

        classzz = DomainApplicationContainer.build().getClass(type + headerCode);
        if (ObjectUtils.isEmpty(classzz)){
            classzz =  getDefaultHeader(type,headerCode);
        }
        instance = BeanUtils.newInstance(classzz);
        return BeanUtils.copyProperties(obj, instance);
    }

    private static Class<?> getDefaultHeader(String type,String headerCode){
        WebRouteParamsEnums header = WebRouteParamsEnums.getByName(type);
        if (WebRouteParamsEnums.HEADER_FORM == header) {
            return headerCode == null||StringUtils.equals(headerCode, WebHttpBodyConstant.DEFAULT_HEADER)?
                    DefaultHeaderForm.class: PageHeaderForm.class;
        }

        if (WebRouteParamsEnums.HEADER_DTO == header) {
            return headerCode == null||StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    DefaultHeaderDTO.class: PageHeaderDTO.class;
        }

        if (WebRouteParamsEnums.HEADER_PARAM == header) {
            return headerCode == null|| StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    BaseParam.class:HeaderParam.class;
        }

        if (WebRouteParamsEnums.HEADER_VO == header) {
            return headerCode == null||StringUtils.equals(headerCode,WebHttpBodyConstant.DEFAULT_HEADER)?
                    ResponseHeaderVO.class: PageResponseHeaderVO.class;
        }

        throw new RuntimeException(StringUtils.format("没有找到 headerCode: [{}],headerType: [{}] 匹配的 header 类型 ",headerCode,type));
    }
}
