package com.hx.nine.eleven.doop.jdbc.provider;

import com.hx.nine.eleven.doop.exception.DataAccessException;
import com.hx.nine.eleven.doop.jdbc.provider.ConnectionProvider;
import com.hx.nine.eleven.doop.jdbc.provider.DefaultConnectionProvider;
import com.hx.nine.eleven.doop.jdbc.provider.TransactionProvider;
import com.hx.nine.eleven.doop.tx.TransactionContext;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayDeque;
import java.util.Deque;

import static com.hx.nine.eleven.doop.enums.BooleanDataKeyEnums.DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT;
import static com.hx.nine.eleven.doop.enums.DataKeyEnum.DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION;
import static com.hx.nine.eleven.doop.enums.DataKeyEnum.DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS;
import static java.lang.Boolean.TRUE;

/**
 *
 * 默认事务处理机制
 * @auth wml
 * @date 2024/3/26
 */
public class DefaultTransactionProvider implements TransactionProvider {

	/**
	 * 这个 {@link Savepoint}作为不支持保存点的顶级事务标记
	 */
	private static final Savepoint UNSUPPORTED_SAVEPOINT = new DefaultSavepoint();

	/**
	 * 如果{@link #nested()}事务被停止了，则{@link Savepoint}作为顶级事务的标记。
	 */
	private static final Savepoint IGNORED_SAVEPOINT     = new DefaultSavepoint();

	/**
	 * ConnectionProvider 实现对象
	 */
	private final ConnectionProvider connectionProvider;

	/**
	 * 是否嵌套事务.默认false
	 */
	private final boolean nested;

	/**
	 * 默认是不开启嵌套事务
	 * @param connectionProvider
	 */
	public DefaultTransactionProvider(ConnectionProvider connectionProvider){
		this(connectionProvider,false);
	}

	public DefaultTransactionProvider(ConnectionProvider connectionProvider, boolean nested){
		this.connectionProvider = connectionProvider;
		this.nested = nested;
	}

	/**
	 * 是否嵌套标记
	 * @return
	 */
	public final boolean nested() {
		return nested;
	}

	/**
	 * 嵌套时，设置顶级事务标记
	 * @param ctx
	 * @return
	 */
	final int nestingLevel(TransactionContext ctx){
		return savepoints(ctx).size();
	}

	/**
	 * 开启一个事务
	 * @param ctx
	 * @throws DataAccessException
	 */
	@Override
	public void begin(TransactionContext ctx) throws DataAccessException {
		Deque<Savepoint> savepoints = savepoints(ctx);
		boolean topLevel = savepoints.isEmpty();
		// 如果当前事务是顶级事务
		if (topLevel){
			brace(ctx, true);
		}
		Savepoint savepoint = setSavepoint(ctx, topLevel);
		if (savepoint == UNSUPPORTED_SAVEPOINT && !topLevel){
			throw new DataAccessException("Cannot nest transactions because Savepoints are not supported");
		}
		savepoints.push(savepoint);
	}

	@Override
	public void commit(TransactionContext ctx) throws DataAccessException {
		Deque<Savepoint> savepoints = savepoints(ctx);
		// 存在嵌套事务
		if (savepoints.size() > 1){
			for (int i = 0; i < savepoints.size(); i++) {
				Savepoint savepoint = savepoints.pollLast();
				//嵌套事务提交,从最内一层事务开始一层一层往上提交
				//有Savepoint，并且不是UNSUPPORTED_SAVEPOINT和IGNORED_SAVEPOINT，则在事务提交之前显式地释放保存点
				// （即非顶层事务首先显示释放保存点），然后在提交事务
				connection(ctx).commit();
				if (savepoint != null && savepoint != UNSUPPORTED_SAVEPOINT && savepoint != IGNORED_SAVEPOINT){
					try {
						connection(ctx).releaseSavepoint(savepoint);
					}
					catch (DataAccessException ignore) {}
				}
			}
			brace(ctx, false);
		}else {
			//顶级事务提交，没有嵌套事务,直接当前connection提价
			if (savepoints.isEmpty()) {
				connection(ctx).commit();
				brace(ctx, false);
			}
		}
	}

	@Override
	public void rollback(TransactionContext ctx) throws DataAccessException {
		Deque<Savepoint> savepoints = savepoints(ctx);
		Savepoint savepoint = null;

		// [#3537] If something went wrong with the savepoints per se
		if (!savepoints.isEmpty())
			savepoint = savepoints.pop();

		try {
			if (savepoint == null || savepoint == UNSUPPORTED_SAVEPOINT) {
				connection(ctx).rollback();
			}

			// [#3955] ROLLBACK is only effective if an exception reaches the
			//         top-level transaction.
			else if (savepoint == IGNORED_SAVEPOINT) {
				if (savepoints.isEmpty())
					connection(ctx).rollback();
			}
			else {
				connection(ctx).rollback(savepoint);
			}
		}

		finally {
			if (savepoints.isEmpty()){
				brace(ctx, false);
			}
		}
	}

	/**
	 * 事务设置一个顶级标记 Savepoint，首先判断当前上下文中是否已经存在，如果存在直接获取，如果不存在则新建一个，放入当前上下文
	 * @param ctx
	 * @return
	 */
	private final Deque<Savepoint> savepoints(TransactionContext ctx) {
		Deque<Savepoint> savepoints = (Deque<Savepoint>) ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS);
		if (savepoints == null) {
			savepoints = new ArrayDeque<>();
			ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS, savepoints);
		}
		return savepoints;
	}

	/**
	 * 获取 ConnectionProvider 对象，首先上下文中是否已经存在，如果不存在则生成一个新的ConnectionProvider {@like DefaultConnectionProvider}
	 * 然后将 ConnectionProvider 放入 {@like TransactionContext.data } 缓存
	 * 如果当前已经有事务，则根据事务传递级别进行处理
	 * @param ctx 事务上下文
	 * @return
	 */
	private final DefaultConnectionProvider connection(TransactionContext ctx) {
		DefaultConnectionProvider connectionWrapper = (DefaultConnectionProvider) ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);

		// 当前事务已经存在，需要根据事务传递机制判断是否新建事务进行嵌套
		if (connectionWrapper != null) {
			if (ctx.transactionPropagationLevel == 1){
				return connectionWrapper;
			}

			if (ctx.transactionPropagationLevel == 6){
				return connectionWrapper;
			}
		}
		connectionWrapper = new DefaultConnectionProvider(connectionProvider.acquire());
		ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION, connectionWrapper);
		return connectionWrapper;
	}

	/**
	 *  自动提交事务标识,如果当前 ctx上下文中有，则判断是否为false ，如果是false或者为null
	 * @param ctx
	 * @return
	 */
	private final boolean autoCommit(TransactionContext ctx) {
		Boolean autoCommit = (Boolean) ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT);

		if (!TRUE.equals(autoCommit)) {
			autoCommit = connection(ctx).getAutoCommit();
			ctx.data(DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT, autoCommit);
		}
		return autoCommit;
	}


	/**
	 * 1、开启事务
	 * 给 connection 设置 autoCommit 值，一般默认为false，只有顶级事务才能进入该方法，顶级事务默认connection中autoCommit=true
	 * 因此该方法要将autoCommit改为false；
	 * begin时，start=true
	 * 2、commit和rollback事务时也调用改方法，同理也是只有当前事务是顶级事务时才进入改方法
	 * commit和rollback时，start=false，此时需要进入finally调用 {@like ConnectionProvider.release} 结束当前connection,释放资源
	 * 同事从ctx 中删除 DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION 设置
	 *
	 * Ensure an <code>autoCommit</code> value on the connection, if it was set
	 * to <code>true</code>, originally.
	 */
	private final void brace(TransactionContext ctx, boolean start) {
		DefaultConnectionProvider connection = connection(ctx);
		try {
			boolean autoCommit = autoCommit(ctx);
			if (autoCommit == true){
				connection.setAutoCommit(!start);
			}
		}
		finally{
			if (!start) {
				connectionProvider.release(connection.acquire());
				ctx.data().remove(DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION);
			}
		}
	}

	/**
	 *
	 * @param ctx
	 * @param topLevel
	 * @return
	 */
	private final Savepoint setSavepoint(TransactionContext ctx, boolean topLevel) {
		if (topLevel || !nested()){
			return IGNORED_SAVEPOINT;
		}
		return connection(ctx).setSavepoint();
	}

	private static class DefaultSavepoint implements Savepoint {
		@Override
		public int getSavepointId() throws SQLException {
			return 0;
		}

		@Override
		public String getSavepointName() throws SQLException {
			return null;
		}
	}

}
