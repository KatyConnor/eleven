package com.hx.doop.tx;

import com.hx.doop.exception.DataAccessException;
import com.hx.doop.jdbc.provider.ConnectionProvider;
import org.jooq.impl.DefaultConnectionProvider;
import java.sql.Connection;

import static com.hx.doop.enums.DataKeyEnum.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;


/**
 * @auth wml
 * @date 2024/3/26
 */
public class ThreadLocalTransactionProvider implements TransactionProvider {

	final DefaultTransactionProvider delegateTransactionProvider;
	final ThreadLocalConnectionProvider localConnectionProvider;
	final ThreadLocal<Connection>           localTxConnection;

	public ThreadLocalTransactionProvider(ConnectionProvider connectionProvider){
		this(connectionProvider,true);
	}

	public ThreadLocalTransactionProvider(ConnectionProvider connectionProvider, boolean nested){
		this.localConnectionProvider = new ThreadLocalConnectionProvider(connectionProvider);
		this.delegateTransactionProvider = new DefaultTransactionProvider(localConnectionProvider, nested);
		this.localTxConnection = new ThreadLocal<>();
	}

	@Override
	public void begin(TransactionContext ctx) throws DataAccessException {
		delegateTransactionProvider.begin(ctx);
		if (delegateTransactionProvider.nestingLevel(ctx) == 1){
			localTxConnection.set(((DefaultConnectionProvider) ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION)).acquire());
		}
	}

	@Override
	public void commit(TransactionContext ctx) throws DataAccessException {
		if (delegateTransactionProvider.nestingLevel(ctx) == 1){
			localTxConnection.remove();
		}
		delegateTransactionProvider.commit(ctx);
	}

	@Override
	public void rollback(TransactionContext ctx) throws DataAccessException {
		if (delegateTransactionProvider.nestingLevel(ctx) == 1){
			localTxConnection.remove();
		}
		delegateTransactionProvider.rollback(ctx);
	}

	final class ThreadLocalConnectionProvider implements ConnectionProvider {

		final ConnectionProvider delegateConnectionProvider;

		public ThreadLocalConnectionProvider(ConnectionProvider delegate) {
			this.delegateConnectionProvider = delegate;
		}

		@Override
		public final Connection acquire() {
			Connection local = localTxConnection.get();
			if (local == null)
				return delegateConnectionProvider.acquire();
			else
				return local;
		}

		@Override
		public final void release(Connection connection) {
			Connection local = localTxConnection.get();

			if (local == null)
				delegateConnectionProvider.release(connection);
			else if (local != connection)
				throw new IllegalStateException(
						"A different connection was released than the thread-bound one that was expected");
		}

		@Override
		public void release() throws org.jooq.exception.DataAccessException {

		}
	}
}
