package hx.nine.eleven.domain.factory;

import hx.nine.eleven.domain.obj.domain.Domain;

public interface AbstractDomainActOfAutonomyFactory {

    /**
     * 激活领域对象内部
     * @param classzz 领域class
     * @param <D>     领域对象类型
     * @return  返回领域对象 <D extends Domain> D
     */
    <D extends Domain> D createDomain(Class<D> classzz);
    /**
     * 激活领域对象内部
     *
     * @param domain          激活领域对象类型
     * @param <D>             领域对象类型
     * @return 返回领域对象
     */
   <D extends Domain> D activeDomain(D domain);

    /**
     * 保存领域对象属性
     *
     * @param domain          需要保存领域对象
     * @param <D>             领域对象类型
     * @return 返回领域对象
     */
    <D extends Domain> D storeDomain(D domain);
}
