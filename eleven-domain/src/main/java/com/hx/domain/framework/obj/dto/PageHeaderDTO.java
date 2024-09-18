package com.hx.domain.framework.obj.dto;

public class PageHeaderDTO extends DefaultHeaderDTO {

	/**
	 * 当前页码
	 */
	private int currentPage = 1;
	/**
	 * 每页条数
	 */
	private int pageSize = 20;
	/**
	 * 数据查询开始坐标
	 */
	private int startNum = 0;
	/**
	 * 总条数
	 */
	private int totalNum;
	/**
	 * 总页数
	 */
	private int totalPageSize;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getStartNum() {
		return startNum;
	}

	public void setStartNum(int startNum) {
		this.startNum = startNum;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalPageSize() {
		return totalPageSize;
	}

	public void setTotalPageSize(int totalPageSize) {
		this.totalPageSize = totalPageSize;
	}
}
