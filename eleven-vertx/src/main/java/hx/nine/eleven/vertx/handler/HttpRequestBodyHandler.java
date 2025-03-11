package hx.nine.eleven.vertx.handler;

import hx.nine.eleven.commons.utils.BeanMapUtil;
import hx.nine.eleven.commons.utils.JSONObjectMapper;
import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.core.constant.ConstantType;
import hx.nine.eleven.core.constant.DefaultProperType;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.utils.MDCThreadUtil;
import hx.nine.eleven.core.web.http.HttpResponse;
import hx.nine.eleven.core.web.http.HttpServlet;
import hx.nine.eleven.core.web.http.HttpServletRequest;
import hx.nine.eleven.core.web.http.HttpServletResponse;
import hx.nine.eleven.vertx.constant.DefaultVertxProperType;
import hx.nine.eleven.vertx.entity.VertxHttpResponseEntity;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.Cookie;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

/**
 * 接受处理POST请求信息
 *
 * @auth wml
 * @date 2024/11/6
 */
public class HttpRequestBodyHandler implements Handler<RoutingContext> {

	public static HttpRequestBodyHandler build() {
		return new HttpRequestBodyHandler();
	}

	@Override
	public void handle(RoutingContext context) {
		HttpServlet httpServlet = ElevenApplicationContextAware.getBean(HttpServlet.class);
		HttpServletRequest req = HttpServletRequest.build();
		HttpServletResponse resp = HttpServletResponse.build();
		HttpResponse res = null;
		try {
			MDCThreadUtil.wrap();
			req.setAttribute(DefaultVertxProperType.VERTX_CONTEXT, context);
			MultiMap multiMap = context.request().headers();
			if (multiMap != null && multiMap.size() > 0) {
				multiMap.forEach((k, v) -> {
					req.addHeader(k, v);
				});
			}
			String authenticate = multiMap.get(DefaultProperType.AUTHENTICATE);
			if (StringUtils.isNotEmpty(authenticate)) {
				req.setAuthenticate(Boolean.valueOf(authenticate));
			}
			String isLogin = multiMap.get(DefaultProperType.IS_LOGIN);
			if (StringUtils.isNotEmpty(isLogin)) {
				req.setLoginStatus(Boolean.valueOf(isLogin));
			}
			String authToken = multiMap.get(DefaultProperType.AUTH_TOKEN);
			if (StringUtils.isNotEmpty(authToken)) {
				req.setToken(authToken);
			}
			String userData = multiMap.get(DefaultProperType.USER_DATA);
			if (StringUtils.isNotEmpty(userData)) {
				req.setAuthorityPermission(userData);
			}
			httpServlet.service(req, resp);
			res = resp.httpResponse();
			Map<String, Object> header = resp.getHeaderMap();
			if (header != null && header.size() > 0) {
				header.forEach((k, v) -> {
					if (k.equals(DefaultProperType.AUTH_TOKEN)) {
						context.response().addCookie(Cookie.cookie(DefaultProperType.AUTH_TOKEN, StringUtils.valueOf(v))
								.setHttpOnly(true).setPath("/*"));

					}
					if (k.equals(DefaultProperType.AUTH_TOKEN)) {
						context.response().addCookie(Cookie.cookie(DefaultProperType.REFRESH_AUTH_TOKEN, StringUtils.valueOf(v))
								.setHttpOnly(true).setPath("/*"));
					}
					context.response().putHeader(k, StringUtils.valueOf(v));
				});
			}

			// 判断是否有文件流返回,如果有则直接返回下载的文件流
			Map<String, Object> jsonObject = BeanMapUtil.beanToMap(res.getBody());
			Boolean fileDownload = Boolean.valueOf(StringUtils.valueOf(jsonObject.get(ConstantType.FILE_STREAM)));
			Object fileDownloadPath = jsonObject.get(ConstantType.FILE_DOWNLOAD_PATH);
			if (fileDownload) {
				context.response().sendFile(String.valueOf(fileDownloadPath));
			} else {
				Object httpResponseBody = jsonObject.get(ConstantType.HTTP_RESPONSE_BODY);
				Map<String, Object> responseBodyMap = BeanMapUtil.beanToMap(httpResponseBody);
				VertxHttpResponseEntity responseEntity = new VertxHttpResponseEntity();
				responseEntity.setResponseHeader(responseBodyMap.get(ConstantType.RESPONSE_HEADER_ENTITY))
						.setResponseBody(responseBodyMap.get(ConstantType.RESPONSE_BODY_ENTITY));
				res.setBody(responseEntity);
				context.response().send(JSONObjectMapper.toJsonString(res));
			}
			MDCThreadUtil.clear();
		} catch (Throwable ex) {
			if(ObjectUtils.isEmpty(res)){
				res = resp.httpResponse();
			}
			VertxHttpResponseEntity responseEntity = new VertxHttpResponseEntity();
			Map<String, Object> jsonObject = BeanMapUtil.beanToMap(res.getBody());
			Object httpResponseBody = jsonObject.get(ConstantType.HTTP_RESPONSE_BODY);
			Map<String, Object> responseBodyMap = BeanMapUtil.beanToMap(httpResponseBody);
			responseEntity.setResponseHeader(responseBodyMap.get(ConstantType.RESPONSE_HEADER_ENTITY))
					.setResponseBody(responseBodyMap.get(ConstantType.RESPONSE_BODY_ENTITY));
			res = res != null ? res : HttpResponse.build()
					.setCode(DefaultVertxProperType.RESPONSE_FAIL_CODE)
					.setMessage(DefaultVertxProperType.RESPONSE_FAIL_MSG);
			res.setCode(DefaultVertxProperType.RESPONSE_FAIL_CODE)
					.setMessage(DefaultVertxProperType.RESPONSE_FAIL_MSG);
			res.setBody(responseEntity);
			context.put(ConstantType.RESPONSE_BODY, res);
			throw new RuntimeException(ex);
		}
	}
}
