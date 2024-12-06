package hx.nine.eleven.core.web.http;

import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.entity.FileUploadEntity;
import hx.nine.eleven.core.enums.HttpMethodEnum;
import hx.nine.eleven.core.web.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @auth wml
 * @date 2024/11/6
 */
public class HttpServletRequest implements ServletRequest {
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpServletRequest.class);
	/**
	 * 存储前端参数
	 */
	private Map<String,Object> jsonObject;
	private Map<String,Object> headerMap;
	private HttpMethodEnum httpMethod;
	/**
	 * 输入流
	 */
	private InputStream inputStream;
	/**
	 * 上传的文件清单
	 */
	private List<FileUploadEntity> fileUploadEntities;
	/**
	 * 是否有权限
	 */
	private Boolean authenticate = false;
	/**
	 * 是否登录状态
	 */
	private Boolean loginStatus = false;
	/**
	 * 授权信息
	 */
	private Object authorityPermission;
	/**
	 * token信息
	 */
	private String token;

	public HttpServletRequest(){
		this.jsonObject = new LinkedHashMap();
		this.headerMap = new LinkedHashMap();
	}

	public static HttpServletRequest build(){
		return new HttpServletRequest();
	}

	/**
	 * post请求，body新增值
	 * @param key
	 * @param value
	 * @return
	 */
	public HttpServletRequest setAttribute(String key,Object value){
		this.jsonObject.put(key, value);
		return this;
	}

	/**
	 *  post请求，删除body值
	 * @param key
	 */
	public HttpServletRequest removeAttribute(String key){
		this.jsonObject.remove(key);
		return this;
	}

	/**
	 *  post请求，删除body值
	 * @param key
	 * @param value
	 */
	public HttpServletRequest removeAttribute(String key,Object value){
		this.jsonObject.remove(key,value);
		return this;
	}

	public HttpServletRequest setContentType(String contentType) {
		this.headerMap.put("Content-Type",contentType);
		return this;
	}

	public HttpServletRequest setMethod(String method){
		this.headerMap.put("Method",method);
		return this;
	}

	public HttpServletRequest addAttribute(Map<String,Object> value){
		this.jsonObject.putAll(value);
		return this;
	}

	public HttpServletRequest addHeader(String key,String value){
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)){
			LOGGER.info("设置header存在为空，key: [{}] , value: [{}]",key,value);
			return this;
		}
		this.headerMap.put(key, value);
		return this;
	}

	public HttpServletRequest addHeaders(Map<String,Object> headers){
		if (Optional.ofNullable(headers).isPresent()){
			this.headerMap.putAll(headers);
		}
		return this;
	}

	public HttpServletRequest addBody(Object body){
		if (ObjectUtils.isEmpty(body)){
			return this;
		}
		Map<String,Object> value;
		if (body instanceof Map){
			value = (Map<String,Object>)body;

		}else{
			value = BeanMapUtil.beanToMap(body);
		}
		this.jsonObject.putAll(value);
		return this;
	}

	@Override
	public Object getAttribute(String key) {
		return this.jsonObject.get(key);
	}

	@Override
	public Map<String, Object> getBody() {
		return this.jsonObject;
	}

	@Override
	public String getCharacterEncoding() {
		return StringUtils.valueOf(this.headerMap.get("Accept-Charset"))+";"+
				StringUtils.valueOf(this.headerMap.get("Accept-Encoding"));
	}

	@Override
	public void setCharacterEncoding(String encode) throws UnsupportedEncodingException {

	}

	@Override
	public long getContentLengthLong() {
		String value = StringUtils.valueOf(this.headerMap.get("Content-Length"));
		return  StringUtils.isEmpty(value)?-1:Long.valueOf(value);
	}

	@Override
	public String getContentType() {
		return  StringUtils.valueOf(this.headerMap.get("Content-Type"));
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return this.inputStream;
	}

	@Override
	public String getParameter(String key) {
		return StringUtils.valueOf(this.jsonObject.get(key));
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return null;
	}

	@Override
	public String getProtocol() {
		return StringUtils.valueOf(this.headerMap.get("Protocol"));
	}

	@Override
	public String getServletPath() {
		return  StringUtils.valueOf(this.headerMap.get("Servlet-Path"));
	}

	@Override
	public String getServerName() {
		return StringUtils.valueOf(this.headerMap.get("Server-Name"));
	}

	@Override
	public int getServerPort() {
		String value = StringUtils.valueOf(this.headerMap.get("Server-port"));
		return StringUtils.isEmpty(value)?-1:Integer.valueOf(value);
	}

	@Override
	public String getLocalHost() {
		return StringUtils.valueOf(this.headerMap.get("Local-Host"));
	}

	@Override
	public String getRemoteHost() {
		return StringUtils.valueOf(this.headerMap.get("Remote-Host"));
	}

	@Override
	public String getHeader(String key) {
		return StringUtils.valueOf(this.headerMap.get(key));
	}

	@Override
	public Map<String, Object> getHeaders() {
		return this.headerMap;
	}

	@Override
	public String getMethod() {
		return StringUtils.valueOf(this.headerMap.get("Method"));
	}

	@Override
	public Principal getUserPrincipal() {
		return null;
	}

	public HttpMethodEnum getHttpMethod() {
		return httpMethod;
	}

	public HttpServletRequest setHttpMethod(HttpMethodEnum httpMethod) {
		this.httpMethod = httpMethod;
		return this;
	}

	public List<FileUploadEntity> getFileUploadEntities() {
		return fileUploadEntities;
	}

	public HttpServletRequest setFileUploadEntities(List<FileUploadEntity> fileUploadEntities) {
		this.fileUploadEntities = fileUploadEntities;
		return this;
	}

	public HttpServletRequest setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
		return this;
	}

	public Boolean getAuthenticate() {
		return authenticate;
	}

	public HttpServletRequest setAuthenticate(Boolean authenticate) {
		this.authenticate = authenticate;
		return this;
	}

	public Boolean getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(Boolean loginStatus) {
		this.loginStatus = loginStatus;
	}

	public Object getAuthorityPermission() {
		return authorityPermission;
	}

	public HttpServletRequest setAuthorityPermission(Object authorityPermission) {
		this.authorityPermission = authorityPermission;
		return this;
	}

	public String getToken() {
		return token;
	}

	public HttpServletRequest setToken(String token) {
		this.token = token;
		return this;
	}
}
