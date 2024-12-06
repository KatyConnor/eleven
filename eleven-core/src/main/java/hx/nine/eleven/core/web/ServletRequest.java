package hx.nine.eleven.core.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/11/6
 */
public interface ServletRequest {

	/**
	 * 指定key获取属性值，主要是post请求时body数据
	 * @param var1
	 * @return
	 */
	Object getAttribute(String var1);

	/**
	 *  获取post请求整个body数据
	 * @return
	 */
	Map<String,Object> getBody();

	/**
	 * 字符集
	 * @return
	 */
	String getCharacterEncoding();

	/**
	 * 设置字符集
	 * @param encode
	 * @throws UnsupportedEncodingException
	 */
	void setCharacterEncoding(String encode) throws UnsupportedEncodingException;

	/**
	 * 获取报文长度
	 * @return
	 */
	long getContentLengthLong();

	/**
	 * 获取 ContentType 类型
	 * @return
	 */
	String getContentType();

	/**
	 * 获取文件
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;

	/**
	 * 指定key获取get请求参数
	 * @param var1
	 * @return
	 */
	String getParameter(String var1);

	/**
	 * 获取get请求所有参数
	 * @return
	 */
	Map<String, String[]> getParameterMap();

	/**
	 * 获取请求协议
	 * @return
	 */
	String getProtocol();

	/**
	 * 服务根地址
	 * @return
	 */
	String getServletPath();

	/**
	 *
	 * @return
	 */
	String getServerName();
	/**
	 * 服务端口号
	 * @return
	 */
	int getServerPort();

	/**
	 * 本机IP地址
	 * @return
	 */
	String getLocalHost();

	/**
	 * 远程IP地址
	 * @return
	 */
	String getRemoteHost();

	/**
	 * 获取指定的header值
	 * @param var1
	 * @return
	 */
	String getHeader(String var1);

	/**
	 * 获取header
	 * @return
	 */
	Map<String,Object> getHeaders();

	/**
	 * http请求类型
	 * @return
	 */
	String getMethod();

	/**
	 * 用户权限数据
	 * @return
	 */
	Principal getUserPrincipal();



}
