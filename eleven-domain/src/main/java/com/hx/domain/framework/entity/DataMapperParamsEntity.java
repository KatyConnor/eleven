package com.hx.domain.framework.entity;

import com.hx.domain.framework.obj.po.BasePO;
import com.hx.lang.commons.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author wml
 */
public class DataMapperParamsEntity {

    /**
     * 当前执行的数据库方法
     */
    private String mapperFactoryMethod;
    /**
     * 多表对象集合，可以存储表实体集合
     */
    private Map<String,Object> poMap = new HashMap<>();

    /**
     * 数据库操作条件参数
     */
    private  Map<String,Object> paramsMap = new HashMap<>();

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param param
     * @param <P>
     * @return
     */
    public <P extends Object> DataMapperParamsEntity pushParam(P param){
        Optional.ofNullable(param).ifPresent(p->{
            paramsMap.put(param.getClass().getName(),param);
        });
        return this;
    }

    /**
     * 键值对存储
     * @param key
     * @param param
     * @param <P>
     * @return
     */
    public <P extends Object> DataMapperParamsEntity pushParam(String key,P param){
        Optional.ofNullable(param).ifPresent(p->{
            paramsMap.put(StringUtils.isNotBlank(key)?key:param.getClass().getName(),param);
        });
        return this;
    }

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param paramList
     * @param <P>
     * @return
     */
    public <P extends Object> DataMapperParamsEntity pushParam(List<P> paramList){
        Optional.ofNullable(paramList).ifPresent(p->{
            String name = p.get(0).getClass().getName();
            paramsMap.put("List:"+name,p);
        });
        return this;
    }

    /**
     * 键值对存储
     * @param key
     * @param paramList
     * @param <P>
     * @return
     */
    public <P extends Object> DataMapperParamsEntity pushParam(String key,List<P> paramList){
        Optional.ofNullable(paramList).ifPresent(p->{
            paramsMap.put(StringUtils.isNotBlank(key)?key:paramList.get(0).getClass().getName(),p);
        });
        return this;
    }

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param po
     * @param <P>
     * @return
     */
    public <P extends BasePO> DataMapperParamsEntity pushPO(P po){
        Optional.ofNullable(po).ifPresent(p->{
            poMap.put(po.getClass().getName(),po);
        });
        return this;
    }

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param po
     * @param <P>
     * @return
     */
    public <P extends BasePO> DataMapperParamsEntity pushPO(String key,P po){
        Optional.ofNullable(po).ifPresent(p->{
            poMap.put(StringUtils.isNotBlank(key)?key:po.getClass().getName(),p);
        });
        return this;
    }

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param poList
     * @param <P>
     * @return
     */
    public <P extends BasePO> DataMapperParamsEntity pushPO(List<P> poList){
       Optional.ofNullable(poList).ifPresent(p->{
           String name = p.get(0).getClass().getName();
           poMap.put("List:"+name,p);
       });
        return this;
    }

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param poList
     * @param <P>
     * @return
     */
    public <P extends BasePO> DataMapperParamsEntity pushPO(String key,List<P> poList){
        Optional.ofNullable(poList).ifPresent(p->{
            poMap.put(StringUtils.isNotBlank(key)?key:poList.get(0).getClass().getName(),p);
        });
        return this;
    }



    public <P extends Object> P peekParam(Class<P> Pclasszz){
        return (P)paramsMap.get(Pclasszz.getName());
    }

    public <P extends Object> P peekParam(String key){
        return (P)paramsMap.get(key);
    }

    public <P extends Object> List<P> peekParamForList(Class<P> Pclasszz){
        return (List<P>)paramsMap.get("List:"+Pclasszz.getName());
    }

    public <P extends Object> List<P> peekParamForList(String key){
        return (List<P>)paramsMap.get(key);
    }

    public <P extends BasePO> P peekPO(Class<P> Pclasszz){
        return (P)poMap.get(Pclasszz.getName());
    }

    public <P extends BasePO> P peekPO(String key){
        return (P)poMap.get(key);
    }

    public <P extends BasePO> List<P> peekPOForList(Class<P> Pclasszz){
        return (List<P>)poMap.get("List:"+Pclasszz.getName());
    }

    public <P extends BasePO> List<P> peekPOForList(String key){
        return (List<P>)poMap.get(key);
    }

    public String getMapperFactoryMethod() {
        return mapperFactoryMethod;
    }

    public DataMapperParamsEntity setMapperFactoryMethod(String mapperFactoryMethod) {
        this.mapperFactoryMethod = mapperFactoryMethod;
        return this;
    }

    public boolean contains(Class classes){
        return this.poMap.containsKey("List:"+classes.getName());
    }
}
