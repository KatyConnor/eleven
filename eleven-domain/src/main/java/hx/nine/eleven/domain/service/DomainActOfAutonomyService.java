package hx.nine.eleven.domain.service;

import hx.nine.eleven.domain.BeanFactoryLocator;
import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.context.DomainContext;
import hx.nine.eleven.domain.context.DomainContextAware;
import hx.nine.eleven.domain.factory.AbstractDomainActOfAutonomyFactory;
import hx.nine.eleven.domain.factory.DomainActOfAutonomyFactory;
import hx.nine.eleven.domain.obj.domain.Domain;
import hx.nine.eleven.domain.response.ResponseEntity;
import hx.nine.eleven.commons.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 *  内部调用domain service
 * @author wml
 * @date 2022-10-12
 */
public class DomainActOfAutonomyService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DomainActOfAutonomyService.class);
    private DomainContext domainContext;

    public DomainActOfAutonomyService(){
        this.domainContext = DomainContextAware.build().getDomainContext();
        if (ObjectUtils.isEmpty(this.domainContext)){
            this.domainContext = new DomainContext();
        }
    }

    public static <T extends AbstractDomainActOfAutonomyFactory> T getDomainActOfAutonomyFactory(Class<T> factoryClass){
        return  BeanFactoryLocator.getBean(factoryClass);
    }

    /**
     * 设置主交易码
     * @param tradeCode
     * @return
     */
    public DomainActOfAutonomyService addTradeCode(String tradeCode){
        this.domainContext.setTradeCode(tradeCode);
        return this;
    }

    /**
     * 设置子交易码
     * @param subTradeCode
     * @return
     */
    public DomainActOfAutonomyService addSubTradeCode(String subTradeCode){
        this.domainContext.setSubTradeCode(subTradeCode);
        return this;
    }

    /**
     * 设置 mapper 执行方法
     * @param mapperFactoryMethod
     * @return
     */
    public DomainActOfAutonomyService addMapperFactoryMethod(String mapperFactoryMethod){
        this.domainContext.setMapperFactoryMethod(mapperFactoryMethod);
        return this;
    }

    /**
     * 设置domain service 执行方法
     * @param domainServiceMethod
     * @return
     */
    public DomainActOfAutonomyService addDomainServiceMethod(String domainServiceMethod){
        this.domainContext.setDomainServiceMethod(domainServiceMethod);
        return this;
    }

    /**
     * 添加header
     * @param key    主键key
     * @param value  值
     * @return
     */
    public DomainActOfAutonomyService putRequestHeader(String key, Object value){
        Object header = this.domainContext.getDomainContext().get(WebHttpBodyConstant.ASYN_HEADER_DTO);
        if (ObjectUtils.isEmpty(header)){
            Map<String,Object> headerMap = new HashMap<>();
            headerMap.put(key,value);
            this.domainContext.getDomainContext().put(WebHttpBodyConstant.ASYN_HEADER_DTO,headerMap);
            return this;
        }
        if (header instanceof Map){
            Map<String,Object> headerMap = (Map)header;
            headerMap.put(key,value);
            this.domainContext.getDomainContext().put(WebHttpBodyConstant.ASYN_HEADER_DTO,headerMap);
        }else {
            throw new RuntimeException("当前存在body对象非Map类型，不能使用本方法设置属性值");
        }
        return this;
    }

    /**
     * 设置body
     * @param key    主键key
     * @param value  值
     * @return
     */
    public DomainActOfAutonomyService putRequestBody(String key,Object value){
        Object body = this.domainContext.getDomainContext().get(WebHttpBodyConstant.ASYN_BODY_DTO);
        if (ObjectUtils.isEmpty(body)){
            Map<String,Object> bodyMap = new HashMap<>();
            bodyMap.put(key,value);
            this.domainContext.getDomainContext().put(WebHttpBodyConstant.ASYN_BODY_DTO,bodyMap);
            return this;
        }
        if (body instanceof Map){
            Map<String,Object> bodyMap = (Map)body;
            bodyMap.put(key,value);
            this.domainContext.getDomainContext().put(WebHttpBodyConstant.ASYN_BODY_DTO,bodyMap);
        }else {
            throw new RuntimeException("当前存在body对象非Map类型，不能使用本方法设置属性值");
        }
        return this;
    }

    /**
     * 添加header
     * @param requestHeader  header对象
     * @return
     */
    public DomainActOfAutonomyService buildRequestHeader(Object requestHeader){
        if (ObjectUtils.isEmpty(requestHeader)){
            LOGGER.warn("传入参数为null");
            return this;
        }
        this.domainContext.getDomainContext().put(WebHttpBodyConstant.ASYN_HEADER_DTO,requestHeader);
        return this;
    }

    /**
     * 添加body
     * @param requestBody  body对象
     * @return
     */
    public DomainActOfAutonomyService buildRequestBody(Object requestBody){
        if (ObjectUtils.isEmpty(requestBody)){
            LOGGER.warn("传入参数为null");
            return this;
        }
        this.domainContext.getDomainContext().put(WebHttpBodyConstant.ASYN_BODY_DTO,requestBody);
        return this;
    }

    /**
     * 添加domainContext对象
     * @param domainContext  上下文
     * @return
     */
    public DomainActOfAutonomyService putDomainContext(Map<String, Object> domainContext){
         this.domainContext.putDomainContextAll(domainContext);
         return this;
    }

    /**
     * 刷新上下文
     * @return
     */
    public DomainActOfAutonomyService refreshContext(){
        DomainContextAware.build().refreshContext(this.domainContext);
        return this;
    }

    /**
     * 内部直接调用 service 方法,异步执行
     * @return
     */
    public ResponseEntity exeAsyncDomainService(){
       return DomainContextAware.build().commitDomainContextEvent(true);
    }

    /**
     * 内部直接调用 service 方法,同步执行
     * @return
     */
    public ResponseEntity exeDomainService(){
        return DomainContextAware.build().commitDomainContextService();
    }


    /**
     * 根据 Domain class 创建domain对象
     * @param domainClass  domain class
     * @param <D>          领域对象
     * @return   return domain 对象
     */
    public static <D extends Domain> D createDomain(Class<D> domainClass){
        DomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(DomainActOfAutonomyFactory.class);
        return factory.createDomain(domainClass);
    }

    /**
     *  激活一个domain领域对象
     * @param domain  领域对象
     * @param <D>     激活返回的领域对象
     * @return   return domain对象
     */
    public static <D extends Domain> D  activeDomain(D domain){
        DomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(DomainActOfAutonomyFactory.class);
        return factory.activeDomain(domain);
    }

    /**
     *  激活一个domain领域对象
     * @param domainClass  领域对象
     * @param <D>          激活返回的领域对象
     * @return   return domain对象
     */
    public static <D extends Domain> D  activeDomain(Class<D> domainClass){
        DomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(DomainActOfAutonomyFactory.class);
        D domain = factory.createDomain(domainClass);
        return factory.activeDomain(domain);
    }

    /**
     *  保存领域对象
     * @param domain  领域对象
     * @param <D>     保存后返回的领域对象
     * @return   return domain对象
     */
    public static<D extends Domain> D storeDomain(D domain){
        DomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(DomainActOfAutonomyFactory.class);
        return  factory.storeDomain(domain);
    }

    /**
     * 根据 Domain class 创建domain对象
     * 自定义继承DomainActOfAutonomyFactory实现createDomain
     * @param classzz      domain class
     * @param factoryClass 继承DomainActOfAutonomyFactory class
     * @param <D>          领域对象
     * @param <T>          DomainActOfAutonomyFactory
     * @return           return domain 对象
     */
    public static <D extends Domain,T extends AbstractDomainActOfAutonomyFactory> D  createDomain(Class<D> classzz,Class<T> factoryClass){
        DomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(DomainActOfAutonomyFactory.class);
        return factory.createDomain(classzz);
    }

    /**
     * 激活一个domain领域对象
     * 自定义继承DomainActOfAutonomyFactory实现activeDomain
     * @param domain        domain class
     * @param factoryClass  继承DomainActOfAutonomyFactory class
     * @param <D>           领域对象
     * @param <T>           DomainActOfAutonomyFactory
     * @return          return domain 对象
     */
    public static <D extends Domain,T extends AbstractDomainActOfAutonomyFactory> D  activeDomain(D domain,Class<T> factoryClass){
        AbstractDomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(factoryClass);
        return factory.activeDomain(domain);
    }

    /**
     * 保存领域对象
     * 自定义继承DomainActOfAutonomyFactory实现storeDomain
     * @param domain        domain class
     * @param factoryClass  继承DomainActOfAutonomyFactory class
     * @param <D>           领域对象
     * @param <T>           DomainActOfAutonomyFactory
     * @return           return domain 对象
     */
    public static<D extends Domain,T extends AbstractDomainActOfAutonomyFactory> D storeDomain(D domain,Class<T> factoryClass){
        AbstractDomainActOfAutonomyFactory factory = BeanFactoryLocator.getBean(factoryClass);
        return  factory.storeDomain(domain);
    }

    public static DomainActOfAutonomyService build(){
        return new DomainActOfAutonomyService();
    }

//    private final static class Single{
//        private final static DomainActOfAutonomyService INSTANCE = new DomainActOfAutonomyService();
//    }
}
