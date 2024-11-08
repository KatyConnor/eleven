package com.hx.nine.eleven.vertx.handler;

import com.hx.nine.eleven.commons.utils.JSONObjectMapper;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.utils.MDCThreadUtil;
import com.hx.nine.eleven.core.web.http.HttpResponse;
import com.hx.nine.eleven.core.web.http.HttpServlet;
import com.hx.nine.eleven.core.web.http.HttpServletRequest;
import com.hx.nine.eleven.core.web.http.HttpServletResponse;
import com.hx.nine.eleven.vertx.constant.DefualtProperType;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

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
			req.setAttribute(DefualtProperType.VERTX_CONTEXT,context);
			httpServlet.service(req, resp);
			res = resp.httpResponse();
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
					.setCode(DefualtProperType.RESPONSE_FAIL_CODE)
					.setMessage(DefualtProperType.RESPONSE_FAIL_MSG);
			context.put(ConstantType.RESPONSE_BODY,res);
			throw new RuntimeException(ex);
		}
	}
}
