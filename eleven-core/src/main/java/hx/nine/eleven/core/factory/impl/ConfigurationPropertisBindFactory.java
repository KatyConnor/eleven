package hx.nine.eleven.core.factory.impl;

import hx.nine.eleven.commons.utils.BeanUtils;
import hx.nine.eleven.core.annotations.NestedConfigurationProperty;
import hx.nine.eleven.core.annotations.Order;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.core.bean.ElevenBeanFactory;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.env.ApplicationEnvConfigProperty;
import hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import hx.nine.eleven.core.annotations.ConfigurationPropertiesBind;
import hx.nine.eleven.core.env.ElevenYamlReadUtils;
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
            List properties = ElevenYamlReadUtils.build().getProperties(bean,c.prefix());
            if (Optional.ofNullable(properties).isPresent()){
              if (properties.size() == 1){
                configProperty.addProperty(properties.get(0));
              }else {
                configProperty.addProperty(properties.get(0).getClass().getName(),properties);
              }
            }else {
              configProperty.addProperty(ElevenBeanFactory.createBean(bean));
            }
            LOGGER.info("注册bean=[{}]", bean.getSimpleName());
          });
        } catch (Exception e) {
          LOGGER.error("@annotation ConfigurationPropertisBind 注解 class 注册容器异常，exception：{}", e);
          System.exit(0);
        }
      });
      configProperty.addProperty(ElevenYamlReadUtils.build().getValueMap());
      DefaultElevenApplicationContext.build().addBean(configProperty);
    });
    afterBeanCreate();
  }

  private void afterBeanCreate() {
    ConcurrentHashMap<Class<?>,List<Field>> nextPropertiesBeans = ElevenApplicationContextAware.getBean(ConstantType.CONDITIONAL_ON_NEST_PROPERTITES_BEAN);
    if (nextPropertiesBeans != null && nextPropertiesBeans.size()>0){
      ApplicationEnvConfigProperty configProperty = DefaultElevenApplicationContext.build().getBean(ApplicationEnvConfigProperty.class);
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
