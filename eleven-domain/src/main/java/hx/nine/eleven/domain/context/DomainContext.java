package hx.nine.eleven.domain.context;

import hx.nine.eleven.domain.constant.WebHttpBodyConstant;
import hx.nine.eleven.domain.obj.dto.BaseDTO;
import hx.nine.eleven.domain.obj.dto.HeaderDTO;
import com.github.f4b6a3.ulid.UlidCreator;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * domain领域对象上下文
 * @author wml
 * @date 2022-12-03
 */
public class DomainContext {

    /**
     * 上下文唯一编号
     */
    private String contextId;

    /**
     * 当前指定执行的数据库方法
     */
    private String mapperFactoryMethod;

    /**
     * 当前指定执行的domain service 方法
     */
    private String domainServiceMethod;

    /**
     * 请求报文头信息
     */
    private HeaderDTO requestHeaderDTO;

    /**
     * 请求报文体body信息
     */
    private BaseDTO requestBody;

    /**
     * domain 与 domain 之间通信时，上下文传输数据对象临时存储
     */
    private Map<String, Object> domainContext;

    /**
     * 输入数据流
     */
    private InputStream inputStream;

    /**
     * 输出数据流
     */
    private OutputStream outputStream;

    /**
     * 用户主体数据
     */
    private Object principal;

    public DomainContext() {
        this.domainContext = new HashMap<>();
        this.contextId = UlidCreator.getUlid().toString();
    }

    /**
     * 放入数据到context
     * @param key    关键字
     * @param value  存入的value值
     * @return
     */
    public DomainContext putDomainContext(String key,Object value){
        if (this.domainContext == null){
            this.domainContext = new HashMap<>();
        }
        this.domainContext.put(key,value);
        return this;
    }

    public DomainContext putDomainContextAll(Map<String, Object> domainContext){
        if (this.domainContext == null){
            this.domainContext = new HashMap<>();
        }
        this.domainContext.putAll(domainContext);
        return this;
    }

    /**
     * 设置内部流程子交易码
     * @param tradeCode 主交易码
     * @return
     */
    public DomainContext setTradeCode(String tradeCode){
        if (this.domainContext == null){
            this.domainContext = new HashMap<>();
        }
        this.domainContext.put(WebHttpBodyConstant.TRADE_CODE,tradeCode);
        return this;
    }

    /**
     * 设置内部流程子交易码
     * @param subTradeCode 子交易码
     * @return
     */
    public DomainContext setSubTradeCode(String subTradeCode){
        if (this.domainContext == null){
            this.domainContext = new HashMap<>();
        }
        this.domainContext.put(WebHttpBodyConstant.SUB_TRADE_CODE,subTradeCode);
        return this;
    }

    /**
     * 获取内部流程主交易码
     * @return
     */
    public String getTradeCode(){
        return String.valueOf(this.domainContext.getOrDefault(WebHttpBodyConstant.TRADE_CODE,null));
    }

    /**
     * 获取内部流程子交易码
     * @return
     */
    public String getSubTradeCode(){
        return String.valueOf(this.domainContext.getOrDefault(WebHttpBodyConstant.SUB_TRADE_CODE,null));
    }

    /**
     * 删除子交易码
     * @return
     */
    public String deleteSubTradeCode(){
        return String.valueOf(this.domainContext.remove(WebHttpBodyConstant.SUB_TRADE_CODE));
    }

    /**
     * 放入数据到context，每一个class对象只能存一个，重复传入同一个class对象，会覆盖原来的对象值，如果要传入同一个class多个值，
     * 可以用key，value键值对，然后获取的是否通过key获取
     * @param value  存入的value值
     * @return
     */
    public DomainContext putDomainContext(Object value){
        if (this.domainContext == null){
            this.domainContext = new HashMap<>();
        }
        this.domainContext.put(value.getClass().getName(),value);
        return this;
    }

    /**
     *
     * @param value
     * @param <T>
     * @return
     */
    public <T> T gutDomainContextValue(Class<T> value){
        if (this.domainContext != null){
            this.domainContext.get(value.getClass().getName());
        }
        return null;
    }

    public String getContextId() {
        return contextId;
    }

    public DomainContext setContextId(String contextId) {
        this.contextId = contextId;
        return this;
    }

    public HeaderDTO getRequestHeaderDTO() {
        return requestHeaderDTO;
    }

    public DomainContext setRequestHeaderDTO(HeaderDTO requestHeaderDTO) {
        this.requestHeaderDTO = requestHeaderDTO;
        return this;
    }

    public DomainContext setRequestBody(BaseDTO requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public BaseDTO getRequestBody() {
        return requestBody;
    }

    public Map<String, Object> getDomainContext() {
        return this.domainContext;
    }

    public DomainContext setDomainContext(Map<String, Object> domainContext) {
        this.domainContext = domainContext;
        return this;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public DomainContext setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public DomainContext setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        return this;
    }

    public String getMapperFactoryMethod() {
        return mapperFactoryMethod;
    }

    public DomainContext setMapperFactoryMethod(String mapperFactoryMethod) {
        this.mapperFactoryMethod = mapperFactoryMethod;
        return this;
    }

    public String getDomainServiceMethod() {
        return domainServiceMethod;
    }

    public void setDomainServiceMethod(String domainServiceMethod) {
        this.domainServiceMethod = domainServiceMethod;
    }

    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    //    public HttpServletRequest getHttpServletRequest() {
//        return httpServletRequest;
//    }
//
//    public DomainContext setHttpServletRequest(HttpServletRequest httpServletRequest) {
//        this.httpServletRequest = httpServletRequest;
//        return this;
//    }
//
//    public HttpServletResponse getHttpServletResponse() {
//        return httpServletResponse;
//    }
//
//    public DomainContext setHttpServletResponse(HttpServletResponse httpServletResponse) {
//        this.httpServletResponse = httpServletResponse;
//        return this;
//    }
}
