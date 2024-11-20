package hx.nine.eleven.mybatisflex.tx;

import com.mybatisflex.core.datasource.FlexDataSource;
import org.apache.ibatis.session.TransactionIsolationLevel;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Properties;

/**
 * @auth wml
 * @date 2024/11/13
 */
public class ElevenFlexTransactionFactory implements TransactionFactory {

	@Override
	public void setProperties(Properties props) {
		TransactionFactory.super.setProperties(props);
	}

	@Override
	public Transaction newTransaction(Connection conn) {
//		throw new UnsupportedOperationException("New Flex transactions require a DataSource");
		return  new ElevenFlexTransaction(conn);

	}

	@Override
	public Transaction newTransaction(DataSource dataSource, TransactionIsolationLevel level, boolean autoCommit) {
		return new ElevenFlexTransaction((FlexDataSource) dataSource,level,autoCommit);
	}
}
