package hx.nine.eleven.core.web;

import hx.nine.eleven.core.web.http.HttpServletRequest;
import hx.nine.eleven.core.web.http.HttpServletResponse;

/**
 * 领域服务路由处理逻辑,由 domain 层实现路由转发逻辑
 * @auth wml
 * @date 2024/11/7
 */
public interface DomainRouter {

	void route(HttpServletRequest request, HttpServletResponse response);
}
