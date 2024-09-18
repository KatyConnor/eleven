package com.hx.vertx.file.monitor.db;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

/**
 * H2数据库服务初始化
 * @author wml
 * @Discription
 * @Date 2023-03-10
 */
public class H2DBServer {
  /**
   * 初始化化数据库，创建数据库
   */
  public static void initH2Db(){
    H2DbConnectionPool h2DbConnectionPool = H2DbConnectionPool.getInstance();
    h2DbConnectionPool.initH2Db();
    ApplicationContextContainer.build().addBean(h2DbConnectionPool);
    // 初始化JOOQ服务
    DSLContext dslContext = DSL.using(H2DbConnectionPool.getInstance().getConnection(),SQLDialect.H2);
    ApplicationContextContainer.build().addBean(dslContext);
    //执行初始化化脚本
    D2DbTableInitializer initializer = new D2DbTableInitializer();
    initializer.initH2DbTable();
  }
}
