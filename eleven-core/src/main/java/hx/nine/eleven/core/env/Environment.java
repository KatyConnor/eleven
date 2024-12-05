package hx.nine.eleven.core.env;

public interface Environment extends PropertyResolver{

  /**
   * 激活环境变量
   * @return
   */
  String getActiveProfiles();

  /**
   * 获取int属性
   * @param key
   * @return
   */
  int getIntProperty(String key);

  /**
   * 获取int属性，如果未获取到返回指定默认值
   * @param key
   * @return
   */
  int getIntProperty(String key,int defaultInt);

  /**
   * 获取 boolean属性
   * @param key
   * @return
   */
  boolean getBooleanProperty(String key);

  /**
   * 获取 boolean属性，如果未获取到返回指定默认值
   * @param key
   * @return
   */
  boolean getBooleanProperty(String key,boolean defaultInt);

  /**
   * 获取 long 属性
   * @param key
   * @return
   */
  long getLongProperty(String key);

  /**
   * 获取 long 属性，如果未获取到返回指定默认值
   * @param key
   * @return
   */
  long getLongProperty(String key,long defaultInt);
}
