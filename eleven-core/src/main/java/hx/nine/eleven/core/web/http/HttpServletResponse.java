package hx.nine.eleven.core.web.http;

import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.web.ServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @auth wml
 * @date 2024/11/6
 */
public class HttpServletResponse implements ServletResponse {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletResponse.class);

	/**
	 * 存储前端参数
	 */
	private Map<String,Object> jsonObject;
	/**
	 * HTTP 请求头
	 */
	private Map<String,String> headerMap;
	private OutputStream outputStream;
	private HttpResponse httpResponse;

	public HttpServletResponse(){
		this.jsonObject = new LinkedHashMap();
		this.headerMap = new LinkedHashMap();
		this.httpResponse = HttpResponse.build().setSuccess(true);
	}

	public static HttpServletResponse build(){
		return new HttpServletResponse();
	}

	/**
	 * 设置返回 body 数据
	 * @param body
	 * @return
	 */
	public HttpServletResponse setBody(Map<String, Object> body) {
		if (ObjectUtils.isEmpty(body)){
			LOGGER.info("输入 body 参数对象为空，body: [{}]",body);
			return this;
		}
		this.jsonObject.putAll(body);
		return this;
	}

	/**
	 * 单独设置 body 返回字段值
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpServletResponse setBody(String key,Object value){
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
			LOGGER.info("设置key、value存在为空，key: [{}] , value: [{}]",key,value);
			return this;
		}
		this.jsonObject.put(key, value);
		return this;
	}

	public HttpServletResponse addHeader(String key,String value){
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
			LOGGER.info("设置header存在为空，key: [{}] , value: [{}]",key,value);
			return this;
		}
		this.headerMap.put(key, value);
		return this;
	}

	public HttpServletResponse addHeaders(Map<String,String> headers){
		if (Optional.ofNullable(headers).isPresent()){
			this.headerMap.putAll(headers);
		}
		return this;
	}

	public HttpServletResponse send(String code,String msg){
		this.httpResponse.setCode(code).setMessage(msg);
		return this;
	}

	/**
	 * 设置自定义的body对象数据
	 * @param body
	 * @return
	 */
	public HttpServletResponse send(Object body){
		this.httpResponse.setBody(body);
		return this;
	}

	/**
	 * 组装最终的 response body 数据，自定义优先级，其次系统默认自带body结构
	 * @return
	 */
	public HttpResponse httpResponse(){
		if (this.httpResponse.getBody() == null){
			return this.jsonObject != null?this.httpResponse.setBody(this.jsonObject):this.httpResponse;
		}
		return this.httpResponse;
	}

	@Override
	public Map<String, Object> getBody() {
		return this.jsonObject;
	}

	@Override
	public String getCharacterEncoding() {
		return null;
	}

	@Override
	public void setCharacterEncoding(String encode) throws UnsupportedEncodingException {

	}

	@Override
	public String getContentType() {
		return  this.headerMap.get("Content-Type");
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		return this.outputStream;
	}

	@Override
	public long getContentLengthLong() {
		return  Long.valueOf(this.headerMap.get("Content-Length"));
	}

	/**
	 *  设置 ContentType 类型
	 * @param contentType
	 */
	public HttpServletResponse setContentType(String contentType) {
		if (StringUtils.isEmpty(contentType)){
			LOGGER.info("设置 contentType 为空，contentType: [{}] ",contentType);
			return this;
		}
		this.headerMap.put("Content-Type",contentType);
		return this;
	}

	/**
	 * 添加输出流
	 * @param outputStream
	 * @return
	 */
	public HttpServletResponse sendOutputStream(OutputStream outputStream){
		this.outputStream = outputStream;
		return this;
	}

	/**
	 * 设置返回报文长度
	 * @param contentLength
	 * @return
	 */
	public HttpServletResponse setContentLengthLong(long contentLength) {
		this.headerMap.put("Content-Length",String.valueOf(contentLength));
		return this;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}
}
