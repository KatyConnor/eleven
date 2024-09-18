package com.hx.vertx.file.monitor.utils;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.hx.vertx.file.monitor.annotations.ConfigurationPropertisBind;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 配置文件加载
 */
public class YamlReadUtils {

  private LinkedHashMap valueMap;

  public static YamlReadUtils build() {
    return Signle.INSTANCE;
  }

  /**
   * 加载 yaml 配置文件,将所有的属性值转换成LinkedHashMap对象进行存储
   * VertxApplicationMain.class.getClassLoader().getResourceAsStream("bootstrap.yml")
   * Thread.currentThread().getContextClassLoader().getResourceAsStream("bootstrap.yml")
   *
   * @return
   */
  public YamlReadUtils readYamlConfiguration() {
    LoaderOptions loadingConfig = new LoaderOptions();
    try (InputStream content = Thread.currentThread().getContextClassLoader().getResourceAsStream("bootstrap.yml")) {
      Yaml yaml = new Yaml(new Constructor(loadingConfig));
      Iterable<Object> its = yaml.loadAll(content);
      valueMap = new LinkedHashMap();
      for (Object it : its) {
        StringBuilder key = new StringBuilder();
        convert(key, (LinkedHashMap) it, valueMap);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return this;
  }

  public <T> T getProperties(Class<T> yamlClass) {
    ConfigurationPropertisBind propertisBind = yamlClass.getAnnotation(ConfigurationPropertisBind.class);
    StringBuilder valueKey = new StringBuilder(propertisBind.prefix());
    valueKey.append(".");
    int startLength = valueKey.length();
    ConstructorAccess<T> constructorAccess = ConstructorAccess.get(yamlClass);
    Field[] fields = yamlClass.getDeclaredFields();
    MethodAccess methodAccess = MethodAccess.get(yamlClass);
    T yamlObj = constructorAccess.newInstance();
    StringBuilder methodName = new StringBuilder("set");
    for (Field field : fields) {
      methodName.append(StringUtils.convertFirstUpperCase(field.getName()));
      valueKey.append(field.getName());
      Object value = this.valueMap.get(valueKey.toString());
      Optional.ofNullable(value).ifPresent(v -> {
        methodAccess.invoke(yamlObj, methodName.toString(), v);
      });
      valueKey.delete(startLength, valueKey.length());
      methodName.delete(3, methodName.length());
    }
    return yamlObj;
  }

  /**
   *
   * @param parent
   * @param valueMap
   * @param resultMap
   */
  private void convert(StringBuilder parent, LinkedHashMap valueMap, LinkedHashMap resultMap) {
    String perfix = parent.toString();
    valueMap.forEach((k, v) -> {
      if (parent.length() <= 0) {
        parent.append(perfix);
      }
      if (v instanceof List){
        List<Map> values = new ArrayList<>(((List<?>) v).size());
        String key = parent.toString();
        ((List)v).stream().forEach(p->{
          LinkedHashMap listMap = new LinkedHashMap<>();
          convert(parent.length() > 0 ? parent.append(".").append(k) : parent.append(k), (LinkedHashMap) p, listMap);
          parent.append(key);
          values.add(listMap);
        });
        resultMap.put(perfix, values);
      } else if (v instanceof Map) {
        convert(parent.length() > 0 ? parent.append(".").append(k) : parent.append(k), (LinkedHashMap) v, resultMap);
      } else {
        resultMap.put(parent.append(".").append(k).toString(), v);
        parent.delete(0, parent.length());
      }
    });
  }

  public LinkedHashMap getValueMap() {
    return valueMap;
  }

  private final static class Signle {
    public final static YamlReadUtils INSTANCE = new YamlReadUtils();
  }
}
