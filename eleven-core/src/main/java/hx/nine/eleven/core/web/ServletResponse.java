package hx.nine.eleven.core.web;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

/**
 * @auth wml
 * @date 2024/11/6
 */
public interface ServletResponse {

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
	 * 获取 ContentType 类型
	 * @return
	 */
	String getContentType();

	/**
	 * 文件输出流
	 * @return
	 * @throws IOException
	 */
	OutputStream getOutputStream() throws IOException;

	/**
	 *  获取报文长度
	 * @param contentLength
	 */
	long getContentLengthLong();


}
