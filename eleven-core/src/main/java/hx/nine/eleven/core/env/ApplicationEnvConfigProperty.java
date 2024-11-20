package hx.nine.eleven.core.env;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.constant.DefualtProperType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化属性配置上下文
 * @author wml
 * @date 2023-03-24
 */
public class ApplicationEnvConfigProperty extends HashMap implements Environment {

  @Override
  public <T> void addProperty(T properties){
    this.put(properties.getClass().getName(),properties);
  }

  @Override
  public <T> void addProperty(String name, T properties) {
    this.put(name,properties);
  }

  @Override
  public void addProperty(Map propertiesMap){
    this.putAll(propertiesMap);
  }

  @Override
  public <T> void addProperty(String name, List<T> properties) {
    this.put(name,properties);
  }

  @Override
  public boolean containsProperty(String key) {
    return this.containsKey(key);
  }

  @Override
  public String getProperty(String key) {
    return String.valueOf(this.get(key));
  }

  @Override
  public String getProperty(String key, String defaultValue) {
    String value = String.valueOf(this.get(key));
    return StringUtils.isBlank(value)?defaultValue:value;
  }

  @Override
  public <T> T getProperty(Class<T> targetType) {
    return (T)this.get(targetType.getName());
  }

  @Override
  public <T> T getProperty(Class<T> targetType, T defaultValue) {
    Object obj = this.get(targetType.getName());
    return obj == null?defaultValue:(T)obj;
  }

  @Override
  public <T> List<T> getProperty(String name, Class<T> targetType) {
    Object value = this.get(name);
    return value instanceof List?(List<T>)value:null;
  }

  @Override
  public <T> List<T> getProperties(Class<T> targetType) {
    return this.getProperty(targetType.getName(),targetType);
  }

  @Override
  public String getActiveProfiles() {
    return this.getProperty(DefualtProperType.ACTIVE_PROFILE);
  }
}
