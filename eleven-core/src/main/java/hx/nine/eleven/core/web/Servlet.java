package hx.nine.eleven.core.web;

import hx.nine.eleven.core.exception.ServletException;
import java.io.IOException;

/**
 * 针对发起的http请求模拟servlet处理
 * @auth wml
 * @date 2024/11/6
 */
public interface Servlet {

	/**
	 * 前置处理
	 */
//	void init();

	/**
	 * 业务处理
	 * @param servletRequest
	 * @param servletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;

	/**
	 * 后置处理
	 */
//	void destroy();
}
