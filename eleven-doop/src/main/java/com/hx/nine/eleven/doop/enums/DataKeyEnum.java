package com.hx.nine.eleven.doop.enums;

import org.jooq.Clause;
import org.jooq.impl.DefaultConnectionProvider;

import java.sql.Connection;

/**
 * @auth wml
 * @date 2024/3/26
 */
public enum DataKeyEnum {

	/**
	 * The level of anonymous block nesting, in case we're generating a block.
	 */
	DATA_BLOCK_NESTING,

	/**
	 * [#531] The local window definitions.
	 * <p>
	 * The window definitions declared in the <code>WINDOW</code> clause are
	 * needed in the <code>SELECT</code> clause when emulating them by inlining
	 * window specifications.
	 */
	DATA_WINDOW_DEFINITIONS,

	/**
	 * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
	 * a new transaction.
	 */
	DATA_DEFAULT_TRANSACTION_PROVIDER_SAVEPOINTS,

	/**
	 * [#1629] The {@link DefaultConnectionProvider} instance to be used during
	 * the transaction.
	 */
	DATA_DEFAULT_TRANSACTION_PROVIDER_CONNECTION,

	/**
	 * [#2080] When emulating OFFSET pagination in certain databases, synthetic
	 * aliases are generated that must be referenced also in
	 * <code>ORDER BY</code> clauses, in lieu of their corresponding original
	 * aliases.
	 */
	DATA_OVERRIDE_ALIASES_IN_ORDER_BY,

	/**
	 * [#3381] The table to be used for the {@link Clause#SELECT_INTO} clause.
	 */
	@SuppressWarnings("javadoc")
	DATA_SELECT_INTO_TABLE,

	/**
	 * [#1206] The collected Semi / Anti JOIN predicates.
	 */
	DATA_COLLECTED_SEMI_ANTI_JOIN,

	/**
	 * [#6583] The target table on which a DML operation operates on.
	 */
	DATA_DML_TARGET_TABLE,

	/**
	 * [#8479] There is a WHERE clause to be emulated for ON DUPLICATE KEY
	 */
	DATA_ON_DUPLICATE_KEY_WHERE,

	/**
	 * [#3607] [#8522] CTEs that need to be added to the top level CTE
	 * section.
	 */
	DATA_TOP_LEVEL_CTE,

	/**
	 * [#10540] Aliases to be applied to the current <code>SELECT</code>
	 * statement.
	 */
	DATA_SELECT_ALIASES
}
