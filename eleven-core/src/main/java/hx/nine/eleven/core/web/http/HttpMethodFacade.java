package hx.nine.eleven.core.web.http;

import hx.nine.eleven.core.exception.ServletException;

import java.io.IOException;
/**
 * @auth wml
 * @date 2024/11/6
 */
public interface HttpMethodFacade {

	void preService(HttpServletRequest httpServletRequest);

	void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;

	void afterService(HttpServletRequest httpServletRequest, HttpServletResponse servletResponse);

}
