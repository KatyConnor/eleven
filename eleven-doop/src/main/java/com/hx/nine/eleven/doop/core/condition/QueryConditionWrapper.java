package com.hx.nine.eleven.doop.core.condition;

import com.hx.nine.eleven.doop.core.table.BaseTable;
import com.hx.nine.eleven.doop.core.table.TableColumn;

/**
 * @auth wml
 * @date 2025/1/26
 */
public class QueryConditionWrapper {

	private TableColumn[] columns;
	private BaseTable table;
	private WhereCondition whereCondition;
	private Object[] whereValues;

	public static QueryConditionWrapper of(){
		return new QueryConditionWrapper();
	}

	public QueryConditionWrapper select(TableColumn[] columns){
		this.columns = columns;
		return this;
	}

	public QueryConditionWrapper from(BaseTable table){
		this.table = table;
		return this;
	}

	public QueryConditionWrapper where(WhereCondition whereCondition){
		this.whereCondition = whereCondition;
		return this;
	}

	public String buildWhereSql(){
		this.whereValues = new Object[this.whereCondition.getLastConditionSize()];
		StringBuilder whereSql = new StringBuilder("WHERE ");
		this.whereValues[0] = this.whereCondition.value;
		String where = this.whereCondition.buildAnd();
		whereSql.append(where.substring("and".length()));
		WhereCondition next = this.whereCondition.next;
		int i = 1;
		while (next != null){
			this.whereValues[i] = next.value;
			whereSql.append(next.buildAnd());
			next = next.next;
			i++;
		}
		return whereSql.toString();
	}

	public TableColumn[] getColumns() {
		return columns;
	}

	public BaseTable getTable() {
		return table;
	}

	public WhereCondition getWhereCondition() {
		return whereCondition;
	}

	public Object[] getWhereValues() {
		return whereValues;
	}
}
