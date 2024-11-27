package hx.nine.eleven.vertx.handler;

import hx.nine.eleven.commons.utils.JSONObjectMapper;
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
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

/**
 *  接受处理POST请求信息
 * @auth wml
 * @date 2024/11/6
 */
public class HttpRequestBodyHandler implements Handler<RoutingContext> {

	public static HttpRequestBodyHandler build(){
		return new HttpRequestBodyHandler();
	}

	@Override
	public void handle(RoutingContext context) {
		HttpServlet httpServlet = ElevenApplicationContextAware.getBean(HttpServlet.class);
		HttpResponse res = null;
		try {
			MDCThreadUtil.wrap();
			HttpServletRequest req = HttpServletRequest.build();
			HttpServletResponse resp = HttpServletResponse.build();
			req.setAttribute(DefaultVertxProperType.VERTX_CONTEXT,context);
			MultiMap multiMap = context.request().headers();
			if (multiMap != null && multiMap.size() > 0){
				multiMap.forEach((k,v)->{
					req.addHeader(k,v);
				});
			}
			String authenticate = multiMap.get(DefaultProperType.AUTHENTICATE);
			if (StringUtils.isNotEmpty(authenticate)){
				req.setAuthenticate(Boolean.valueOf(authenticate));
			}
			String isLogin = multiMap.get(DefaultProperType.IS_LOGIN);
			if (StringUtils.isNotEmpty(isLogin)){
				req.setLoginStatus(Boolean.valueOf(isLogin));
			}
			String authToken = multiMap.get(DefaultProperType.AUTH_TOKEN);
			if (StringUtils.isNotEmpty(authToken)){
				req.setToken(authToken);
			}
			String userData = multiMap.get(DefaultProperType.USER_DATA);
			if (StringUtils.isNotEmpty(userData)){
				req.setAuthorityPermission(userData);
			}
			httpServlet.service(req, resp);
			res = resp.httpResponse();
			//
			Map<String, String> header = resp.getHeaderMap();
			if (header != null && header.size() > 0){
				header.forEach((k,v) ->{
					context.response().putHeader(k,v);
				});
			}

			// 判断是否有文件流返回,如果有则直接返回下载的文件流
			JsonObject jsonObject = JsonObject.mapFrom(res.getBody());
			Boolean fileDownload = jsonObject.getBoolean(ConstantType.FILE_STREAM);
			Object  fileDownloadPath = jsonObject.getValue(ConstantType.FILE_DOWNLOAD_PATH);
			if (fileDownload){
				context.response().sendFile(String.valueOf(fileDownloadPath));
			}else {
				context.response().send(JSONObjectMapper.toJsonString(res));
			}
			MDCThreadUtil.clear();
		} catch (Throwable ex) {
			res = res != null?res:HttpResponse.build()
					.setCode(DefaultVertxProperType.RESPONSE_FAIL_CODE)
					.setMessage(DefaultVertxProperType.RESPONSE_FAIL_MSG);
			context.put(ConstantType.RESPONSE_BODY,res);
			throw new RuntimeException(ex);
		}
	}
}
