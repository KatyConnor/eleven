package com.hx.nine.eleven.core.factory.impl;

import com.hx.lang.commons.utils.BeanUtils;
import com.hx.nine.eleven.core.annotations.NestedConfigurationProperty;
import com.hx.nine.eleven.core.annotations.Order;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.VertxApplicationContextAware;
import com.hx.nine.eleven.core.core.bean.VertxBeanFactory;
import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import com.hx.nine.eleven.core.env.ApplicationEnvConfigProperty;
import com.hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import com.hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import com.hx.nine.eleven.core.env.HXVertxYamlReadUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加载初始化properties对象
 * @author wml
 * @date 2023-03-24
 */
@Order(order = -9999)
public class ConfigurationPropertisBindFactory implements ApplicationAnnotationFactory {

  private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationPropertisBindFactory.class);

  @Override
  public void loadAnnotations(Reflections reflections) {
    ApplicationEnvConfigProperty configProperty = new ApplicationEnvConfigProperty();
    Set<Class<?>> anntationSet = reflections.getTypesAnnotatedWith(ConfigurationPropertiesBind.class);
    Optional.ofNullable(anntationSet).ifPresent(classList -> {
      classList.forEach(bean -> {
        try {
          ConfigurationPropertiesBind component = bean.getAnnotation(ConfigurationPropertiesBind.class);
          Optional.ofNullable(component).ifPresent(c -> {
            List properties = HXVertxYamlReadUtils.build().getProperties(bean,c.prefix());
            if (Optional.ofNullable(properties).isPresent()){
              if (properties.size() == 1){
                configProperty.addProperty(properties.get(0));
              }else {
                configProperty.addProperty(properties.get(0).getClass().getName(),properties);
              }
            }else {
              configProperty.addProperty(VertxBeanFactory.createBean(bean));
            }
            LOGGER.info("注册bean=[{}]", bean.getSimpleName());
          });
        } catch (Exception e) {
          LOGGER.error("@annotation ConfigurationPropertisBind 注解 class 注册容器异常，exception：{}", e);
          System.exit(0);
        }
      });
      configProperty.addProperty(HXVertxYamlReadUtils.build().getValueMap());
      DefaultVertxApplicationContext.build().addBean(configProperty);
    });
    afterBeanCreate();
  }

  private void afterBeanCreate() {
    ConcurrentHashMap<Class<?>,List<Field>> nextPropertiesBeans = VertxApplicationContextAware.getBean(ConstantType.CONDITIONAL_ON_NEST_PROPERTITES_BEAN);
    if (nextPropertiesBeans != null && nextPropertiesBeans.size()>0){
      ApplicationEnvConfigProperty configProperty = DefaultVertxApplicationContext.build().getBean(ApplicationEnvConfigProperty.class);
      nextPropertiesBeans.forEach((k,v)->{
        Object property = configProperty.getProperty(k);
        v.forEach(o ->{
          NestedConfigurationProperty annotation = o.getAnnotation(NestedConfigurationProperty.class);
          Object obj = configProperty.getProperty(annotation.type());
          BeanUtils.setField(obj,property,o.getName());
        });
      });
      nextPropertiesBeans.clear();
      nextPropertiesBeans = null;
    }
  }


}
