package com.hx.nine.eleven.domain.entity;

import com.hx.nine.eleven.domain.obj.po.BasePO;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  分页实体数据
 * @author wml
 * @date 2019-02-15
 */
public class PageEntity {

    /**
     * 分页数据
     */
    Map<String, Collection> pageEntity = new HashMap<>();

    /**
     * 当前页码
     */
    private int currentPageNum = 1;
    /**
     * 每页条数
     */
    private int pageSize = 20;
    /**
     * 其实条数
     */
    private int startRow;
    /**
     * 结束条数
     */
    private int endRow = 20;
    /**
     * 总条数
     */
    private long totalSize;
    /**
     * 总页数
     */
    private int totalPages;

    public <T extends BasePO> List<T> getPageEntity(Class<T> key) {
        return (List<T>)pageEntity.get(key);
    }

    public <T extends BasePO> PageEntity setPageEntity(Class<T> key,Collection<T> pageEntity) {
        this.pageEntity.put(key.getName(),pageEntity);
        return this;
    }

    public int getCurrentPageNum() {
        return currentPageNum;
    }

    public PageEntity setCurrentPageNum(int currentPageNum) {
        this.currentPageNum = currentPageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public PageEntity setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public int getStartRow() {
        return startRow;
    }

    public PageEntity setStartRow(int startRow) {
        this.startRow = startRow;
        return this;
    }

    public int getEndRow() {
        return endRow;
    }

    public PageEntity setEndRow(int endRow) {
        this.endRow = endRow;
        return this;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public PageEntity setTotalSize(long totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public PageEntity setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
