package hx.nine.eleven.domain.response;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.msg.code.code.ApplicationSystemCode;
import hx.nine.eleven.msg.code.enums.SystemCode;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 接口交互数据结果集
 * @author wangml
 * @Date 2019-09-09
 */
public class ResponseEntity<H,E> implements Serializable{

    /**
     * 数据对象
     */
    private WebHttpResponse<H,E> httpResponseBody;
    private Boolean fileStream = false;
    private String fileDownloadPath;
    private Map<String, Object> headerMap;

    public ResponseEntity(){
        this.httpResponseBody = new WebHttpResponse<>();
        headerMap = new LinkedHashMap();
        this.successful();
    }

    public WebHttpResponse getHttpResponseBody() {
        return httpResponseBody;
    }

    public ResponseEntity setResponseHeader(H responseHeader) {
        this.httpResponseBody.setResponseHeader(responseHeader);
        return this;
    }

    public ResponseEntity addStatusCode(String statusCode) {
        this.httpResponseBody.getResponseBody().setStatusCode(statusCode);
        return this;
    }

    public String pollStatusCode() {
        return this.httpResponseBody.getResponseBody().getStatusCode();
    }

    public ResponseEntity addCode(String code) {
        this.httpResponseBody.getResponseBody().setCode(code);
        return this;
    }

    public String pollCode() {
        return  this.httpResponseBody.getResponseBody().getCode();
    }

    public ResponseEntity addMessage(String message) {
        this.httpResponseBody.getResponseBody().setMessage(message);
        return this;
    }

    public String pollMessage() {
        return  this.httpResponseBody.getResponseBody().getMessage();
    }

    public ResponseEntity addData(E data) {
        this.httpResponseBody.getResponseBody().setData(data);
        return this;
    }

    public E pollData() {
        return  this.httpResponseBody.getResponseBody().getData();
    }

    public ResponseEntity successful(){
        if (this.httpResponseBody == null){
            this.httpResponseBody = new WebHttpResponse<>();
        }
        BaseResponse response = this.httpResponseBody.getResponseBody();
        response.setStatusCode(SystemCode.SUCCESS.getCode());
        response.setCode( ApplicationSystemCode.B0000000000.getCode());
        response.setMessage( ApplicationSystemCode.B0000000000.getMessage());
        return this;
    }

    public ResponseEntity failure(){
        BaseResponse response = this.httpResponseBody.getResponseBody();
        response.setStatusCode(SystemCode.FAILED.getCode());
        response.setCode(ApplicationSystemCode.B0000000001.getCode());
        response.setMessage(ApplicationSystemCode.B0000000001.getMessage());
        return this;
    }

    public ResponseEntity addHeader(String key,Object value){
        this.headerMap.put(key,value);
        return this;
    }

    public ResponseEntity addHeader(Map<String,Object> headers){
        this.headerMap.putAll(headers);
        return this;
    }

    public Object getHeader(String key){
        return this.headerMap.get(key);
    }

    public String getStringHeader(String key){
        return StringUtils.valueOf(this.headerMap.get(key));
    }

    public Boolean getBooleanHeader(String key){
        String value = StringUtils.valueOf(this.headerMap.get(key));
        return StringUtils.isEmpty(value)?null:Boolean.valueOf(value);
    }

    public Integer getIntegerHeader(String key){
        String value = StringUtils.valueOf(this.headerMap.get(key));
        return StringUtils.isEmpty(value)?-1:Integer.valueOf(value);
    }

    public Long getLongHeader(String key){
        String value = StringUtils.valueOf(this.headerMap.get(key));
        return StringUtils.isEmpty(value)?null:Long.valueOf(value);
    }

    public Map<String, Object> getHeaderMap() {
        return headerMap;
    }

    public Boolean getFileStream() {
        return fileStream;
    }

    public void setFileStream(Boolean fileStream) {
        this.fileStream = fileStream;
    }

    public String getFileDownloadPath() {
        return fileDownloadPath;
    }

    public void setFileDownloadPath(String fileDownloadPath) {
        this.fileDownloadPath = fileDownloadPath;
    }

    public static ResponseEntity build(){
        return new ResponseEntity();
    }
}
