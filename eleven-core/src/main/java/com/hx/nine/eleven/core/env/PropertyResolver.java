package com.hx.nine.eleven.core.env;

import java.util.List;
import java.util.Map;

public interface PropertyResolver {

  /**
   * 是否包含传入属性key
   * @param key
   * @return
   */
  boolean containsProperty(String key);

  /**
   * 根据key获取属性值
   * @param key
   * @return
   */
  String getProperty(String key);

  /**
   * 根据key获取属性值,当key为null时，设置返回默认值
   * @param key
   * @param defaultValue
   * @return
   */
  String getProperty(String key, String defaultValue);

  /**
   * 根据key返回一个对应的属性对象
   * @param targetType
   * @param <T>
   * @return
   */
  <T> T getProperty(Class<T> targetType);

  /**
   * 根据key返回一个对应的属性对象,当上下文中没有时，返回设置的默认对象
   * @param targetType
   * @param defaultValue
   * @param <T>
   * @return
   */
  <T> T getProperty(Class<T> targetType, T defaultValue);

  /**
   * 获取集合属性
   * @param name
   * @param targetType
   * @param <T>
   * @return
   */
  <T> List<T> getProperty(String name, Class<T> targetType);

  /**
   * 获取集合属性
   * @param targetType
   * @param <T>
   * @return
   */
  <T> List<T> getProperties(Class<T> targetType);

  /**
   * 添加属性对象
   * @param properties
   * @param <T>
   */
  <T> void addProperty(T properties);

  /**
   * 添加属性
   * @param name
   * @param properties
   * @param <T>
   */
  <T> void addProperty(String name,T properties);

  /**
   * 注入所有属性Map对象
   * @param propertiesMap
   */
  void addProperty(Map propertiesMap);

  /**
   * 添加属性集合
   * @param name
   * @param properties
   * @param <T>
   */
  <T> void addProperty(String name, List<T> properties);
}
