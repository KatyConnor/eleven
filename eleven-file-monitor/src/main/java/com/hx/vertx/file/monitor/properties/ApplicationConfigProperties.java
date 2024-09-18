package com.hx.vertx.file.monitor.properties;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfigProperties extends HashMap {

  public <T> T getProperties(Class<T> properties){
    return (T)this.get(properties.getName());
  }

  public <T> void addProperties(T properties){
    this.put(properties.getClass().getName(),properties);
  }

  public void addProperties(Map propertiesMap){
    this.putAll(propertiesMap);
  }
}
