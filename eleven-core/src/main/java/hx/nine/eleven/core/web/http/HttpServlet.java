package hx.nine.eleven.core.web.http;

import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.UserAuthenticateProvider;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.enums.HttpMethodEnum;
import hx.nine.eleven.core.exception.ServletException;
import hx.nine.eleven.core.exception.UserAuthenticateException;
import hx.nine.eleven.core.web.DomainRouter;
import hx.nine.eleven.core.web.Servlet;
import hx.nine.eleven.core.web.ServletRequest;
import hx.nine.eleven.core.web.ServletResponse;

import java.io.IOException;

/**
 *
 * @auth wml
 * @date 2024/11/6
 */
public class HttpServlet implements Servlet {

	private HttpMethodFacade httpMethodFacade;
	private DomainRouter domainRouter;

	public HttpServlet() {
		httpMethodFacade = ElevenApplicationContextAware.getSubTypesOfBean(HttpMethodFacade.class);
		domainRouter =  ElevenApplicationContextAware.getSubTypesOfBean(DomainRouter.class);
	}

	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			String token = request.getToken();
			if (StringUtils.isNotEmpty(token) && !request.getAuthenticate()){
				UserAuthenticateProvider provider = ElevenApplicationContextAware.getSubTypesOfBean(UserAuthenticateProvider.class);
				if (provider == null){
					throw new UserAuthenticateException("用户鉴权失败，没有查找到[UserAuthenticateProvider]接口实现实例!");
				}
				if (provider.authenticate(token)){
					request.setAuthenticate(true);
					request.setLoginStatus(true);
					Object tokenReal = provider.decodeTokenAuth(token);
					token = provider.generateToken(token);
					request.setToken(token);
					request.setAuthorityPermission(tokenReal);
					response.setHeader(ConstantType.AUTH_TOKEN,token);
				}
			}
			this.httpMethodFacade.preService(request);
			this.service(request, response);
			domainRouter.route(request, response);
			this.httpMethodFacade.afterService(request, response);
		} else {
			throw new ServletException("non-HTTP request or response");
		}
	}

	private void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpMethodEnum method = req.getHttpMethod();
		if (method == HttpMethodEnum.METHOD_GET) {
			httpMethodFacade.doGet(req, resp);
		} else if (method == HttpMethodEnum.METHOD_HEAD) {
			httpMethodFacade.doHead(req, resp);
		} else if (method == HttpMethodEnum.METHOD_POST) {
			httpMethodFacade.doPost(req, resp);
		} else if (method == HttpMethodEnum.METHOD_PUT) {
			httpMethodFacade.doPut(req, resp);
		} else if (method == HttpMethodEnum.METHOD_DELETE) {
			httpMethodFacade.doDelete(req, resp);
		} else if (method == HttpMethodEnum.METHOD_OPTIONS) {
			httpMethodFacade.doOptions(req, resp);
		} else if (method == HttpMethodEnum.METHOD_TRACE) {
			httpMethodFacade.doTrace(req, resp);
		} else {
			// 不支持的方法
//			String errMsg = lStrings.getString("http.method_not_implemented");
//			Object[] errArgs = new Object[]{method};
//			errMsg = MessageFormat.format(errMsg, errArgs);
//			resp.sendError(501, errMsg);
		}

	}
}
