package com.hx.vertx.file.monitor.db.mapper;

import java.util.List;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-12
 */
public interface BaseMapper<P> extends Mapper {

  <T> int insert(T po);
  <T> int batchInsert(List<T> poList);
  <T,P> int update(T po, P param);
  <T,P> int updateByPrimaryKey(T po);
  <T,P> int batchUpdate(List<T> po, P param);
  <P> int delete(P param);
  void deleteByPrimaryKey();
  void select();
  void selectByPrimaryKey();
  void selectPage();
}
