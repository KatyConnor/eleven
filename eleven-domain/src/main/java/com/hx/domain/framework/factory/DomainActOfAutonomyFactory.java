package com.hx.domain.framework.factory;

import com.hx.lang.commons.utils.BeanMapUtil;
import com.hx.domain.framework.DomainUtils;
import com.hx.domain.framework.context.DomainContext;
import com.hx.domain.framework.context.DomainContextAware;
import com.hx.domain.framework.entity.DataMapperParamsEntity;
import com.hx.domain.framework.obj.domain.Domain;
import com.hx.domain.framework.properties.DomainEventListenerHandlerProperties;
import com.hx.vertx.boot.annotations.Component;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
@Component
public class DomainActOfAutonomyFactory implements AbstractDomainActOfAutonomyFactory {

    @Resource
    private AutowiredDomainFactory autowiredDomainFactory;
    @Resource
    private DomainEventListenerHandlerProperties properties;

    @Override
    public <D extends Domain> D createDomain(Class<D> classzz) {
        D domnain = autowiredDomainFactory.create(classzz);
        Field[] fields = classzz.getDeclaredFields();
        return DomainUtils.createDomain(domnain,fields);
    }

    @Override
    public <D extends Domain> D activeDomain(D domain) {
        DomainContext context = DomainContextAware.build().getDomainContext();
        // 设置查询参数
        DataMapperParamsEntity paramsEntity = new DataMapperParamsEntity();
        DomainUtils.setDataMapperParamsEntity(context.getDomainContext(),paramsEntity);
        // 执行数据库操作
        DomainUtils.mapperInvok(context,paramsEntity,domain,properties.getEnableDomainSupport(),true,true);
        return domain;
    }

    /**
     * 添加事务控制
     * @param domain 需要保存领域对象
     * @param <D>
     * @return
     */
    @Override
    public <D extends Domain> D storeDomain(D domain) {
        try {
            DomainContext context = DomainContextAware.build().getDomainContext();
            // 设置参数
            DataMapperParamsEntity paramsEntity = new DataMapperParamsEntity();
            Optional.ofNullable(domain).ifPresent(d -> {
                Map<String, Object> poMap = BeanMapUtil.beanToMap(domain);
                DomainUtils.setDataMapperParamsEntity(poMap,paramsEntity);
            });
            DomainUtils.setDataMapperParamsEntity(context.getDomainContext(),paramsEntity);
            // 执行数据库操作
            DomainUtils.mapperInvok(context,paramsEntity,domain,properties.getEnableDomainSupport(),false,true);
            // 提交事务
        } catch (Exception ex) {

        }
        return domain;
    }
}
