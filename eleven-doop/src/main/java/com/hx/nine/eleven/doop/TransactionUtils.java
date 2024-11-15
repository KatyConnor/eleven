package com.hx.nine.eleven.doop;


import com.hx.nine.eleven.doop.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.doop.jdbc.provider.ConnectionProvider;
import com.hx.nine.eleven.doop.jdbc.provider.DataSourceConnectionProvider;
import com.hx.nine.eleven.doop.jdbc.provider.ThreadLocalTransactionProvider;

/**
 *
 * @author wml
 * @date 2024/3/26
 */
public class TransactionUtils {

    /**
     * 获取 DataSourceConnectionProvider 对象
     * @param dataSource
     * @return
     */
    public static ConnectionProvider createConnectionProvider(AbstractRoutingDataSource dataSource) {
        return new DataSourceConnectionProvider(dataSource);
    }

    /**
     * 获取 ThreadLocalTransactionProvider
     * @param dataSource
     * @return
     */
    public static ThreadLocalTransactionProvider createTransactionProvider(AbstractRoutingDataSource dataSource) {
        return new ThreadLocalTransactionProvider(createConnectionProvider(dataSource));
    }
}
