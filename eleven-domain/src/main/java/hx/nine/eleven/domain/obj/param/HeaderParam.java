package hx.nine.eleven.domain.obj.param;

public class HeaderParam extends BaseParam{

    /**
     * 交易码
     */
    private String tradeCode;
    /**
     * 交易请求流水号
     */
    private String reqNo;
    /**
     * 交易跟踪ID
     */
    private String traceId;
    /**
     * 交易请求时间
     */
    private String tradeTime;
    /**
     * 当前页码
     */
    private int currentPage = 1;
    /**
     * 每页条数
     */
    private int pageSize = 20;
    /**
     * 总条数
     */
    private long totalNum;
    /**
     * 总页数
     */
    private long totalPageSize;
    /**
     * 是否跟新
     */
    private boolean forUpdate;

    public String getTradeCode() {
        return tradeCode;
    }

    public void setTradeCode(String tradeCode) {
        this.tradeCode = tradeCode;
    }

    public String getReqNo() {
        return reqNo;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

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

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public long getTotalPageSize() {
        return totalPageSize;
    }

    public void setTotalPageSize(long totalPageSize) {
        this.totalPageSize = totalPageSize;
    }

    public boolean isForUpdate() {
        return forUpdate;
    }

    public void setForUpdate(boolean forUpdate) {
        this.forUpdate = forUpdate;
    }
}
