package com.hx.domain.framework.factory;

import com.hx.domain.framework.DomainUtils;
import com.hx.domain.framework.context.DomainContextAware;
import com.hx.domain.framework.properties.DomainEventListenerHandlerProperties;
import com.hx.lang.commons.utils.BeanMapUtil;
import com.hx.domain.framework.context.DomainContext;
import com.hx.domain.framework.obj.domain.Domain;
import com.hx.domain.framework.entity.DataMapperParamsEntity;
import com.hx.vertx.boot.annotations.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/**
 * @author wml
 * @Date 2019-08-27
 */
@Component
public class DefaultDomainFactory implements AbstractDomainFactory {

    @Resource
    private AutowiredDomainFactory autowiredDomainFactory;
    @Resource
    private DomainEventListenerHandlerProperties properties;

    @Override
    public <D extends Domain> D create(Class<D> classzz) {
        D domnain = autowiredDomainFactory.create(classzz);
        Field[] fields = classzz.getDeclaredFields();
        return DomainUtils.createDomain(domnain,fields);
    }

    @Override
    public <D extends Domain> D active(Class<D> classzz) {
        D domain = this.create(classzz);
        DomainContext context = DomainContextAware.build().getDomainContext();
        // 设置查询参数
        DataMapperParamsEntity paramsEntity = new DataMapperParamsEntity();
//        paramsEntity.setMapperFactoryMethod(context.getMapperFactoryMethod());
        DomainUtils.setParam(context,paramsEntity);
        DomainUtils.setDataMapperParamsEntity(context.getDomainContext(),paramsEntity);
        // 执行数据库操作
        DomainUtils.mapperInvok(context,paramsEntity,domain,properties.getEnableDomainSupport(),true,false);
        return domain;
    }

    @Override
    public <D extends Domain> D store(D domain) {
        DomainContext context = DomainContextAware.build().getDomainContext();
        // 设置参数
        DataMapperParamsEntity paramsEntity = new DataMapperParamsEntity();
//        paramsEntity.setMapperFactoryMethod(context.getMapperFactoryMethod());
        Optional.ofNullable(domain).ifPresent(d->{
            Map<String,Object> poMap = BeanMapUtil.beanToMap(domain);
            DomainUtils.setDataMapperParamsEntity(poMap,paramsEntity);
        });
        DomainUtils.setDataMapperParamsEntity(context.getDomainContext(),paramsEntity);
        // DomainContext消费完毕，清空DomainContext
        DomainUtils.setParam(context,paramsEntity);
        // 执行数据库操作
        DomainUtils.mapperInvok(context,paramsEntity,domain,properties.getEnableDomainSupport(),false,false);
        return domain;
    }

    @Override
    public <D extends Domain> D query(D domain) {
        return null;
    }







}
