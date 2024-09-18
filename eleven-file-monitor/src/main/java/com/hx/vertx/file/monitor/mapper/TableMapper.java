package com.hx.vertx.file.monitor.mapper;

import com.hx.vertx.file.monitor.context.ApplicationContextContainer;
import org.jooq.DSLContext;

/**
 * 校验表中是否被初始化，是否存在数据
 */
public class TableMapper {

  private DSLContext dslContext;

  public static TableMapper build(){
    return new TableMapper();
  }

  public TableMapper(){
    this.dslContext = ApplicationContextContainer.build().getDSLContext();
  }

  public int selectCount(String tableName){
    return Integer.valueOf(this.dslContext.selectCount().from(tableName).fetch().getValue(0,"count").toString());
  }
}
