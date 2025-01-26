package com.hx.nine.eleven.doop.core.condition;

import com.hx.nine.eleven.doop.core.table.TableColumn;

/**
 * @auth wml
 * @date 2025/1/26
 */
public class WhereCondition {

	protected TableColumn column;
	protected String logicalOperator;
	protected Object value;
	protected WhereCondition prev;
	protected WhereCondition next;
	protected int conditionSize = 1;

	public WhereCondition(TableColumn column, String logicalOperator, Object value) {
		this.column = column;
		this.logicalOperator = logicalOperator;
		this.value = value;
	}

	public WhereCondition and(WhereCondition next){
		this.next = next;
		this.next.prev = this;
		this.next.conditionSize = this.conditionSize + 1;
		return this.next;
	}
	public String buildAnd(){
		return " AND"+this.column.getColumn()+logicalOperator+" = ?";
	}

	public int getLastConditionSize(){
		WhereCondition next = this.next;
		while (next != null){
			next = next.next;
		}
		return next.conditionSize;
	}

	public TableColumn getColumn() {
		return column;
	}

	public void setColumn(TableColumn column) {
		this.column = column;
	}

	public String getLogicalOperator() {
		return logicalOperator;
	}

	public void setLogicalOperator(String logicalOperator) {
		this.logicalOperator = logicalOperator;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public WhereCondition getPrev() {
		return prev;
	}

	public void setPrev(WhereCondition prev) {
		this.prev = prev;
	}

	public WhereCondition getNext() {
		return next;
	}

	public void setNext(WhereCondition next) {
		this.next = next;
	}

	public int getConditionSize() {
		return conditionSize;
	}

	public void setConditionSize(int conditionSize) {
		this.conditionSize = conditionSize;
	}
}
