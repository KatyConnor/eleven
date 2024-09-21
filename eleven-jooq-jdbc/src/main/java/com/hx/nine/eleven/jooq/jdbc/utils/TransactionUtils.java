package com.hx.nine.eleven.jooq.jdbc.utils;

import com.hx.nine.eleven.jooq.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.jooq.jdbc.HXDataSourceConnectionProvider;
import org.jooq.ConnectionProvider;
import org.jooq.impl.ThreadLocalTransactionProvider;

/**
 * @author wml
 * @date 2023-05-10
 */
public abstract class TransactionUtils {

	public static ConnectionProvider createConnectionProvider(AbstractRoutingDataSource dataSource){
		return new HXDataSourceConnectionProvider(dataSource);
	}

	public static ThreadLocalTransactionProvider createTransactionProvider(AbstractRoutingDataSource dataSource){
		return new ThreadLocalTransactionProvider(createConnectionProvider(dataSource));
	}
}
