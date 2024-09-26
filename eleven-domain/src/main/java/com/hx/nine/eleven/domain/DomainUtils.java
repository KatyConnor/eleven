package com.hx.nine.eleven.domain;

import com.hx.nine.eleven.domain.annotations.domain.support.DomainDO;
import com.hx.nine.eleven.domain.context.DomainContextAware;
import com.hx.nine.eleven.domain.enums.FieldTypeEnum;
import com.hx.nine.eleven.domain.context.DomainServiceRouteSupport;
import com.hx.nine.eleven.domain.entity.DataMapperPOEntity;
import com.hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import com.hx.nine.eleven.domain.factory.DataMapperFactory;
import com.hx.nine.eleven.domain.obj.param.BaseParam;
import com.hx.nine.eleven.domain.obj.po.BasePO;
import com.hx.nine.eleven.commons.utils.*;
import com.hx.nine.eleven.domain.context.DomainContext;
import com.hx.nine.eleven.domain.conver.BeanConvert;
import com.hx.nine.eleven.domain.entity.DataMapperParamsEntity;
import com.hx.nine.eleven.domain.entity.PageEntity;
import com.hx.nine.eleven.domain.exception.DBExecuteException;
import com.hx.nine.eleven.domain.exception.DomainOperatorException;
import com.hx.nine.eleven.domain.obj.domain.Domain;
import com.hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import net.sf.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.util.*;

public class DomainUtils {

	public static <D extends Domain> D createDomain(D domnain, Field[] fields) {
		DomainContext context = DomainContextAware.build().getDomainContext();
		Object requestBody = context.getRequestBody();
		if (!Optional.ofNullable(requestBody).isPresent()) {
			return domnain;
		}
		BeanUtils.copyProperties(requestBody, domnain);
		BeanMap beanMap = BeanMap.create(domnain);
		// 特殊字段赋值
		for (Field field : fields) {
			// 添加了DomainDO注解，必须指定交易码,主交易码不为空，则主交易码下所有的requestBody都映射到该DomainDO注解字段
			DomainDO domainDO = field.getAnnotation(DomainDO.class);
			if (domainDO == null) {
				continue;
			}
			String tradeCode = domainDO.tradeCode();
			String[] subTradeCode = domainDO.subTradeCode();
			if (ObjectUtils.isEmpty(subTradeCode) && StringUtils.isEmpty(tradeCode)) {
				continue;
			}

			if (StringUtils.isNotEmpty(tradeCode)){
				findAndSetFieldValue(requestBody,field,domainDO,beanMap);
				continue;
			}

			if (ObjectUtils.isNotEmpty(subTradeCode)){
				List<String> subTradeCodeList = Arrays.asList(subTradeCode);
				if (subTradeCodeList.contains(context.getRequestHeaderDTO().getSubTradeCode())){
					// 查找字段，如果value有值则寻找value匹配的字段进行赋值
					findAndSetFieldValue(requestBody,field,domainDO,beanMap);
				}
			}
		}
		return (D) beanMap.getBean();
	}

	/**
	 * 通过 @seeDataMapperEntity 对象给领域对象赋值
	 *
	 * @param dataMapperPOEntity
	 * @param domain
	 * @param <D>
	 * @param <P>
	 * @see DataMapperPOEntity
	 */
	public static <D extends Domain, P extends BasePO> void setDomain(DataMapperPOEntity<P> dataMapperPOEntity, D domain) {
		if (!Optional.ofNullable(dataMapperPOEntity).isPresent()) {
			throw new DomainOperatorException(DomainApplicationSysCode.A030000000);
		}

		// domain 属性和PO一对一对应赋值
		Map<String, P> poMap = dataMapperPOEntity.getPoMap();
		Optional.ofNullable(poMap).ifPresent(o -> {
			BeanUtils.setField(poMap, domain, () -> null);
		});

		// domain属性字段对应数据库PO查询结果List集合赋值
		Map<String, List<P>> poMapList = dataMapperPOEntity.getPoMapList();
		Optional.ofNullable(poMapList).ifPresent(o -> {
			BeanUtils.setField(poMapList, domain, () -> null);
		});

		// domain和dataMapperEntity映射的属性字段赋值
		Map<String, Object> propertiesMap = dataMapperPOEntity.getProperty();
		Optional.ofNullable(propertiesMap).ifPresent(o -> {
			BeanUtils.setField(propertiesMap, domain, () -> null);
		});

		// domain分页查询数据赋值
		PageEntity pageEntity = dataMapperPOEntity.getPageDoEntity();
		Optional.ofNullable(pageEntity).ifPresent(page -> {
			BeanUtils.setField(pageEntity, domain, "pageDoEntity");
		});
	}

	/**
	 * @param domainContext
	 * @param paramsEntity
	 */
	public static void setDataMapperParamsEntity(Map<String, Object> domainContext, DataMapperParamsEntity paramsEntity) {
		for (Map.Entry entry : domainContext.entrySet()) {
			Object value = entry.getValue();
			if (value == null || ObjectUtils.isEmpty(value)) {
				continue;
			}

			if (value instanceof List && ((List) value).get(0) instanceof BasePO) {
				if (paramsEntity.contains(((List) value).get(0).getClass())){
					paramsEntity.pushPO(entry.getKey().toString(),(List) value);
				}else {
					paramsEntity.pushPO((List) value);
				}
				continue;
			}
			if (BasePO.class.isAssignableFrom(value.getClass())) {
				paramsEntity.pushPO((BasePO) value);
				continue;
			}

			if (value instanceof List && ((List) value).get(0) instanceof BaseParam) {
				paramsEntity.pushParam((List) value);
				continue;
			}

			if (BaseParam.class.isAssignableFrom(value.getClass())) {
				paramsEntity.pushParam((BaseParam) value);
				continue;
			}

			paramsEntity.pushParam(entry.getKey().toString(), value);
		}
	}

	public static <D extends Domain> void mapperInvok(DomainContext context, DataMapperParamsEntity paramsEntity, D domain, boolean enableDomainSupport, boolean query, boolean innerInvok) {
		String tradeCode = context.getRequestHeaderDTO().getTradeCode();
		String subTradeCode = innerInvok ? context.getSubTradeCode() : context.getRequestHeaderDTO().getSubTradeCode();
		// 触发数据库操作的mapperFactory
		DataMapperFactory dataMapperFactory = (DataMapperFactory) BeanFactoryLocator.getBean(WebRouteParamsEnums.MAPPER_FACTORY + tradeCode);
		if (dataMapperFactory == null) {
			throw new DBExecuteException(DomainApplicationSysCode.B0001010000);
		}

		DataMapperPOEntity dataMapperPOEntity = null;
		if (enableDomainSupport && StringUtils.isNotBlank(subTradeCode)) {
			Object result = DomainServiceRouteSupport.invokMapperFactory(subTradeCode, dataMapperFactory, paramsEntity);
			if (result != null && result.getClass().isAssignableFrom(DataMapperPOEntity.class)) {
				dataMapperPOEntity = (DataMapperPOEntity) result;
			}
		} else {
			dataMapperPOEntity = query ? dataMapperFactory.select(paramsEntity) : dataMapperFactory.execute(paramsEntity);
		}
		if (dataMapperPOEntity != null) {
			DomainUtils.setDomain(dataMapperPOEntity, domain);
		}

		// 内部请求生命周期结束，清空domainContext 中的子交易码
		if (innerInvok) {
			context.deleteSubTradeCode();
		}
	}

	public static void setParam(DomainContext context, DataMapperParamsEntity paramsEntity) {
		String tradeCode = context.getRequestHeaderDTO().getTradeCode();
		String subTradeCode = context.getRequestHeaderDTO().getSubTradeCode();
		String headerCode = context.getRequestHeaderDTO().getHeaderCode();
		Object headerParam = BeanConvert.convert(context.getRequestHeaderDTO(), StringUtils.append(tradeCode, subTradeCode), headerCode, WebRouteParamsEnums.HEADER_PARAM.getName());
		Object bodyParam = BeanConvert.convert(context.getRequestBody(), StringUtils.append(tradeCode, subTradeCode), headerCode, WebRouteParamsEnums.BODY_PARAM.getName());
		paramsEntity.pushParam((BaseParam) bodyParam);
		paramsEntity.pushParam((BaseParam) headerParam);
	}

	private static void findAndSetFieldValue(Object requestBody,Field field,DomainDO domainDO,BeanMap beanMap){
		String value = domainDO.value();
		if (StringUtils.isNotBlank(value)) {
			Object fieldValue = BeanMapUtil.beanToMap(requestBody).get(value);
			setDomainField(fieldValue,field,domainDO,beanMap);
		}else {
			Object fieldValue = BeanMapUtil.beanToMap(requestBody).get(field.getName());
			setDomainField(Optional.ofNullable(fieldValue).isPresent()?fieldValue:requestBody,field,domainDO,beanMap);
		}
	}

	private static void setDomainField(Object requestBody, Field field,DomainDO domainDO,BeanMap beanMap) {
		if (!Optional.ofNullable(requestBody).isPresent()){
			ElevenLoggerFactory.build(DomainUtils.class).info("requestBody为空");
			return;
		}
		Class<?> fieldClass = field.getType();
		//检查字段是否是集合
		String fieldType = domainDO.fieldType();
		if ((Optional.ofNullable(fieldType).isPresent() && FieldTypeEnum.LIST == FieldTypeEnum.getByCode(fieldType))
				|| fieldClass.isAssignableFrom(List.class)
				|| fieldClass.isAssignableFrom(Collection.class)) {
			if (StringUtils.isEmpty(domainDO.convertType())){
				ElevenLoggerFactory.build(DomainUtils.class).info("@DomainDO 没有指定 convertType 类型,[{}]赋值失败",field.getName());
				return;
			}
			setListField(requestBody, beanMap,field.getName(), domainDO.convertType());
		} else {
			Object fieldValue = BeanUtils.copyProperties(requestBody,fieldClass);
			beanMap.put(field.getName(), fieldValue);
		}
	}

	private static void setListField(Object sourceObj, BeanMap beanMap, String fieldName, Class<?> fieldConvertType) {
		if (!(sourceObj instanceof List)) {
			throw new RuntimeException("当前requestBody获取字段[{}]类型[{}]，不属于List，跳过List赋值");
		}
		List<Object> sourceObjList = (List<Object>) sourceObj;
		List<Object> fieldValueList = new ArrayList<>(sourceObjList.size());
		sourceObjList.forEach(o -> {
			Object obj = BeanUtils.copyProperties(o, fieldConvertType);
			fieldValueList.add(obj);
		});
		beanMap.put(fieldName, fieldValueList);
	}
}
