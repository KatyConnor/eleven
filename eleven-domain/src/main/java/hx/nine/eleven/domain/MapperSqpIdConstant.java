package hx.nine.eleven.domain;

/**
 * @Description 数据库操作
 * @author wml
 * 2020-06-29
 */
public interface MapperSqpIdConstant {

    // 公共部分
    String SQL_UPDATE_BY_PRIMARY_KEY = "updateByPrimaryKey";
    String SQL_UPDATE_BY_PRIMARY_SELECTIVE = "updateByParamsSelective";
    String SQL_DELETE_BY_PRIMARY_KEY = "deleteByPrimaryKey";
    String SQL_INSERT = "insert";
    String SQL_UPDATE = "update";
    String SQL_SELECT_BY_PRIMARY_KEY = "selectByPrimaryKey";
    String SQL_SELECT_LIST = "selectList";
    String SQL_SELECT_ONE = "selectOne";
    String SQL_SELECT_ONE_CALL_PROCEDURE = "callProcedure";
    String SQL_UPDATE_BATCH = "updateBatch";
    String SQL_INSERT_BATCH = "insertBatch";
    String FOR_UPDATE = "forUpdate";


}
