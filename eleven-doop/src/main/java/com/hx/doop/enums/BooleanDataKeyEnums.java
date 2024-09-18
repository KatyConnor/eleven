package com.hx.doop.enums;

import java.sql.Connection;

/**
 * @auth wml
 * @date 2024/3/26
 */
public enum BooleanDataKeyEnums {


	/**
	 * [#1520] Count the number of bind values, and potentially enforce a static
	 * statement.
	 */
	DATA_COUNT_BIND_VALUES,

	/**
	 * [#2080] When emulating OFFSET pagination in certain databases, synthetic
	 * aliases are generated that must be referenced also in
	 * <code>ORDER BY</code> clauses, in lieu of their corresponding original
	 * aliases.
	 * [#8898] Oracle doesn't support aliases in RETURNING clauses.
	 */
	DATA_UNALIAS_ALIASED_EXPRESSIONS,

	/**
	 * [#7139] No data must be selected in the <code>SELECT</code> statement.
	 */
	DATA_SELECT_NO_DATA,

	/**
	 * [#1658] Specify whether the trailing LIMIT clause needs to be rendered.
	 */
	DATA_RENDER_TRAILING_LIMIT_IF_APPLICABLE,

	/**
	 * [#3886] Whether a list has already been indented.
	 */
	DATA_LIST_ALREADY_INDENTED,

	/**
	 * [#3338] [#5086] Whether a constraint is being referenced (rather than
	 * declared).
	 */
	DATA_CONSTRAINT_REFERENCE,

	/**
	 * [#1206] Whether to collect Semi / Anti JOIN.
	 */
	DATA_COLLECT_SEMI_ANTI_JOIN,

	/**
	 * [#11486] An <code>INSERT .. SELECT</code> statement.
	 */
	DATA_INSERT_SELECT,


	/**
	 * [#1629] The {@link Connection#getAutoCommit()} flag value before starting
	 * a new transaction.
	 */
	DATA_DEFAULT_TRANSACTION_PROVIDER_AUTOCOMMIT,

	/**
	 * [#2995] An <code>INSERT INTO t SELECT</code> statement. Without any
	 * explicit column list, the <code>SELECT</code> statement must not be
	 * wrapped in parentheses (which would be interpreted as the column
	 * list's parentheses).
	 */
	DATA_INSERT_SELECT_WITHOUT_INSERT_COLUMN_LIST,

	/**
	 * [#5191] Whether INSERT RETURNING is being emulated for bulk insertions.
	 */
	DATA_EMULATE_BULK_INSERT_RETURNING,

	/**
	 * [#1535] We're currently generating the window specification of a
	 * window function that requires an ORDER BY clause.
	 */
	DATA_WINDOW_FUNCTION_REQUIRES_ORDER_BY,

	/**
	 * [#9925] In some cases the <code>AS</code> keyword is required for aliasing, e.g. XML.
	 */
	DATA_AS_REQUIRED
}
