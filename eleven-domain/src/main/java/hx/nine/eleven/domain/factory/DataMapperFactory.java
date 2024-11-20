package hx.nine.eleven.domain.factory;

import hx.nine.eleven.domain.entity.DataMapperPOEntity;
import hx.nine.eleven.domain.entity.DataMapperParamsEntity;
import hx.nine.eleven.domain.obj.param.BaseParam;
import hx.nine.eleven.domain.obj.po.BasePO;

/**
 * 数据库操作
 * @author wangml
 * @Date 2019-10-16
 */
public abstract class DataMapperFactory {

    /**
     * 单表操作
     * @param paramsEntity    数据库实体
     * @return      返回true/false
     */
    public abstract <P extends BasePO> DataMapperPOEntity<P> execute(DataMapperParamsEntity paramsEntity);
    /**
     * 查询操作,通过条件查询
     * @param paramsEntity    条件参数
     * @return DataMapperEntity
     */
    public abstract <P extends BasePO> DataMapperPOEntity<P> select(DataMapperParamsEntity paramsEntity);

    /**
     * 数据库查询参数类型转换
     * @param paramClass
     * @param param
     * @param <T>
     * @return
     */
    public <T extends BaseParam> T convertParam(Class<T> paramClass, Object param){
        return param.getClass().isAssignableFrom(paramClass) ? (T)param : null;
    }

    /**
     *  数据库映射实体类型转换
     * @param poClass
     * @param po
     * @param <T>
     * @return
     */
    public <T extends BasePO> T convertPO(Class<T> poClass,Object po){
        return po.getClass().isAssignableFrom(poClass)?(T) po:null;
    }
}
