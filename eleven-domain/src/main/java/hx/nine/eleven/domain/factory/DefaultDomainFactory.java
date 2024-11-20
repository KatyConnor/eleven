package hx.nine.eleven.domain.factory;

import hx.nine.eleven.domain.DomainUtils;
import hx.nine.eleven.domain.context.DomainContextAware;
import hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.domain.context.DomainContext;
import hx.nine.eleven.domain.obj.domain.Domain;
import hx.nine.eleven.domain.entity.DataMapperParamsEntity;
import hx.nine.eleven.core.annotations.Component;

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
