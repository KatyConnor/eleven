package com.hx.nine.eleven.doop.core.table;

import com.hx.nine.eleven.doop.core.condition.WhereCondition;

/**
 * 数据库映射列
 * @auth wml
 * @date 2025/1/26
 */
public class TableColumn {

	private String column;
	private Class<?> columnType;

	public TableColumn(String column,Class<?> columnType){
		this.column = column;
		this.columnType = columnType;
	}

	public WhereCondition eq(Object value){
		return new WhereCondition(this,"",value);
	}

	public String getColumn() {
		return column;
	}

	public Class<?> getColumnType() {
		return columnType;
	}
}
