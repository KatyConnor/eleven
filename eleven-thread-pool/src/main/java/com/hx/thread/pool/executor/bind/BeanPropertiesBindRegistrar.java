package com.hx.thread.pool.executor.bind;

import com.hx.lang.commons.utils.BeanMapUtil;
import com.hx.lang.commons.utils.BeanUtils;
import com.hx.thread.pool.executor.HXApplicationContext;
import com.hx.thread.pool.executor.StringUtils;
import com.hx.thread.pool.executor.ThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>根据绑定的properties 注册bean,首先注册默认bean
 *
 * </p>
 *
 * @author wml
 * 2021-09-09
 */
@Deprecated
public class BeanPropertiesBindRegistrar {//implements ImportBeanDefinitionRegistrar, EnvironmentAware {

//    private static final Logger LOGGER = LoggerFactory.getLogger(BeanPropertiesBindRegistrar.class);

//    private Environment evn;
    /**
     * 参数绑定工具 springboot2.0新推出
     */
//    private Binder binder;

//    @Override
//    public void setEnvironment(Environment environment) {
//        LOGGER.info("set environment and bind ");
//        this.evn = environment;
//        binder = Binder.get(evn);
//    }
//
//    @Override
//    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
//        LOGGER.info("init bind properties");
//        List<Map> bindList = binder.bind("vertx.hx.thread.pool", Bindable.listOf(Map.class)).get();
//        List<ThreadPoolProperties> list = new ArrayList<>();
//        bindList.stream().forEach(bvalue -> {
//            Map<String,Object> valueMap = new HashMap<>();
//            for (Map.Entry entry : ((Map<String,Object>)bvalue).entrySet()){
//                String key = StringUtils.convertUnderlineToHump(entry.getKey().toString());
//                valueMap.put(key,entry.getValue());
//            }
//            ThreadPoolProperties threadPoolProperties = BeanMapUtil.mapToBean(valueMap,ThreadPoolProperties.class);
//            list.add(threadPoolProperties);
//        });
//        HXApplicationContext.build().add("threadPoolProperties",list);
//    }
}
