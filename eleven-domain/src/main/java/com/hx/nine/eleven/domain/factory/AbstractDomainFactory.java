package com.hx.nine.eleven.domain.factory;

import com.hx.nine.eleven.domain.obj.domain.Domain;

/**
 * @Description 领域对象创建工厂
 * @Author wangml
 * @Date 2020-05-21
 */

public interface AbstractDomainFactory {

    /**
     * 直接创建一个空领域对象，根据DTO参数创建一个有属性值领域对象
     * 根据Parma参数生成一个domian 对象，param属性和domain中属性一一对应，属性类型，名称必须保持一致
     * param根据接口order生成
     *
     * @param classzz 领域对象类型
     * @param <D>     生成领域对象(泛型)
     * @return 返回泛型（T） 领域对象
     */
    <D extends Domain> D create(Class<D> classzz);

    /**
     * 激活一个领域对象
     *
     * @param domain          激活领域对象类型
     * @param <D>             领域对象类型
     * @return 返回领域对象
     */
    <D extends Domain> D active(Class<D> domain);

    /**
     * 保存领域对象
     *
     * @param domain          需要保存领域对象
     * @param <D>             领域对象类型
     * @return 返回领域对象
     */
    <D extends Domain> D store(D domain);

    /**
     * 领域属性激活
     * @param domain
     * @param <D>
     * @return
     */
    <D extends Domain> D query(D domain);

}
