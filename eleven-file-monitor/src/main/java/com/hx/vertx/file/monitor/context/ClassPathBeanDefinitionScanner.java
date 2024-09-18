package com.hx.vertx.file.monitor.context;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.hx.vertx.file.monitor.annotations.Component;
import com.hx.vertx.file.monitor.annotations.ConfigurationPropertisBind;
import com.hx.vertx.file.monitor.log.HXLogger;
import com.hx.vertx.file.monitor.properties.ApplicationConfigProperties;
import com.hx.vertx.file.monitor.utils.ApplicationAnnotationUtils;
import com.hx.vertx.file.monitor.utils.StringUtils;
import com.hx.vertx.file.monitor.utils.YamlReadUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * package 配置扫描
 *
 * @author wml
 * @date 2022-03-15
 */
public class ClassPathBeanDefinitionScanner {

  private String scanPackage;

  public ClassPathBeanDefinitionScanner(String scanPackage) {
    this.scanPackage = scanPackage;
  }

  /**
   * 扫描package包
   */
  public void initClass() {
    if (StringUtils.isNotBlank(this.scanPackage)) {
      HXLogger.build(this).info("扫描 basePackage = [{}],初始化bean容器", this.scanPackage);
      Reflections reflections = new Reflections(this.scanPackage);
      doComponentReflections(reflections);
      return;
    }
  }

  /**
   * 处理应用实例化类
   *
   * @param reflections
   */
  private void doComponentReflections(Reflections reflections) {
    List<Class<Annotation>> annotations = ApplicationAnnotationUtils.values();
    ApplicationConfigProperties configProperties = new ApplicationConfigProperties();
    annotations.stream().forEach(a -> {
      Set<Class<?>> anntationSet = reflections.getTypesAnnotatedWith(a);
      Optional.ofNullable(anntationSet).ifPresent(classList -> {
        String type = a.getSimpleName();
        switch (type) {
          case "Component":
            addComponentBean(classList);
            break;
          case "ConfigurationPropertisBind":
            addPropertisBean(classList,configProperties);
            break;
          default:
        }
      });
    });
    ApplicationContextContainer.build().addBean(configProperties);
  }

  /**
   * 创建单例对象
   * @param anntationSet
   */
  public void addComponentBean(Set<Class<?>> anntationSet) {
    anntationSet.forEach(bean -> {
      try {
        Component component = bean.getAnnotation(Component.class);
        Optional.ofNullable(component).ifPresent(c -> {
          ConstructorAccess constructorAccess = ConstructorAccess.get(bean);
          Object yamlObj = constructorAccess.newInstance();
          if (StringUtils.isNotEmpty(c.value())) {
            ApplicationContextContainer.build().addBean(c.value(), yamlObj);
          } else {
            ApplicationContextContainer.build().addBean(yamlObj);
          }
          HXLogger.build(this).info("注册bean=[{}]", bean.getSimpleName());
        });
      } catch (Exception e) {
        HXLogger.build(this).error("@annotation Component 注解 class 注册容器异常，exception：{}", e);
        System.exit(0);
      }
    });
  }

  /**
   * 扫描配置文件，放入上下文中
   *
   * @param anntationSet
   */
  public ApplicationConfigProperties addPropertisBean(Set<Class<?>> anntationSet, ApplicationConfigProperties configProperties) {
    anntationSet.forEach(bean -> {
      try {
        ConfigurationPropertisBind component = bean.getAnnotation(ConfigurationPropertisBind.class);
        Optional.ofNullable(component).ifPresent(c -> {
          Object properties = YamlReadUtils.build().getProperties(bean);
          configProperties.addProperties(properties);
          HXLogger.build(this).info("注册bean=[{}]", bean.getSimpleName());
        });
      } catch (Exception e) {
        HXLogger.build(this).error("@annotation ConfigurationPropertisBind 注解 class 注册容器异常，exception：{}", e);
        System.exit(0);
      }
    });
    configProperties.addProperties(YamlReadUtils.build().getValueMap());
    return configProperties;
  }
}
