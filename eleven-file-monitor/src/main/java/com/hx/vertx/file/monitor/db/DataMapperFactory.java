package com.hx.vertx.file.monitor.db;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import org.jooq.DSLContext;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-12
 */
public class DataMapperFactory {

  public static DSLContext dSLContext(){
    return ApplicationContextContainer.build().getBean(DSLContext.class);
  }

  public static H2DbConnectionPool getH2DbConnectionPool(){
    return ApplicationContextContainer.build().getBean(H2DbConnectionPool.class);
  }
}
