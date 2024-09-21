package com.hx.nine.eleven.datasources;

import com.hx.nine.eleven.datasources.utils.HXLogger;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.vertx.jooq.jdbc.AbstractRoutingDataSource;
import lombok.Setter;
import javax.sql.DataSource;
import java.util.Map;


/**
 * 动态数据源,
 * @Author wml
 * @Date 2022-10-15
 */
public class HXDynamicRoutingDataSource extends AbstractRoutingDataSource {

    @Setter
    private Boolean p6spy = false;
    @Setter
    private Boolean seata = false;

    /**
     * @TODO 后期可以优化，通过数据源分组实现负载获取数据源
     * 查找数据源，返回需要使用的数据源名称
     * @return 数据源名称
     */
    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DynamicDataSourceContextHolder.peek();
        HXLogger.build(this).info("获取当前线程数据源，{}，datasource:[{}]",Thread.currentThread().getName(),dataSource);
        return dataSource;
    }

    public void afterPropertiesSet() {
        // 检查环境，开启了配置但没有相关依赖
        checkEnv();
        // 检测默认数据源是否设置
        DataSource  defaultDataSource = super.getResolvedDefaultDataSource();
        if (StringUtils.isEmpty(defaultDataSource)) {
            throw new IllegalArgumentException("没有配置默认数据源，请配置默认数据源或者检查数据源配置");
        }
        Map<Object, DataSource> dataSourceMap = super.getResolvedDataSources();
        dataSourceMap.forEach((k,v) ->{
            if (v.equals(defaultDataSource)){
                HXLogger.build(this).warn("数据源加载完成,默认数据源：[{}]",k);
                return;
            }
        });
    }

    /**
     * 检查环境
     */
    private void checkEnv() {
        HXLogger.build(this).info("检查数据源配置环境");
        if (p6spy) {
            try {
                Class.forName("com.p6spy.engine.spy.P6DataSource");
                HXLogger.build(this).info("dynamic-datasource detect P6SPY plugin and enabled it");
            } catch (Exception e) {
                throw new RuntimeException("dynamic-datasource enabled P6SPY ,however without p6spy dependency", e);
            }
        }

        if (seata) {
            try {
                Class.forName("io.seata.rm.datasource.DataSourceProxy");
                HXLogger.build(this).info("dynamic-datasource detect ALIBABA SEATA and enabled it");
            } catch (Exception e) {
                throw new RuntimeException("dynamic-datasource enabled ALIBABA SEATA,however without seata dependency", e);
            }
        }
    }

    /**
     * 关闭数据源
     * @param ds
     * @param dataSource
     */
    private void closeDataSource(String ds, DataSource dataSource) {

    }
}
