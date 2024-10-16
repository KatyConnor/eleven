package com.hx.nine.eleven.vertx.web;

import com.hx.nine.eleven.vertx.auth.HXJWTAuthHandler;
import com.hx.nine.eleven.core.constant.ConstantType;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.properties.ElevenBootApplicationProperties;
import com.hx.nine.eleven.core.utils.MDCThreadUtil;
import com.hx.nine.eleven.vertx.handler.DefaultHttpRequestServletRouterHandler;
import com.hx.nine.eleven.vertx.handler.FileBodyHandlerImpl;
import com.hx.nine.eleven.vertx.handler.GetHttpRequestServletRouterHandler;
import com.hx.nine.eleven.vertx.handler.GlobalDefaultExceptionHandler;
import com.hx.nine.eleven.vertx.handler.HttpRequestServletRouterHandler;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 初始化web路由
 *
 * @author wml
 * @date 2023-03-24
 */
public class WebRouterInitializer {

	private Router router;
	private JWTAuth authProvider;
	private ElevenBootApplicationProperties elevenBootApplicationProperties;

	public WebRouterInitializer(){
		elevenBootApplicationProperties = DefaultElevenApplicationContext.build().getProperties(ElevenBootApplicationProperties.class);
	}

	public void webInit(Vertx vertx, JWTAuthOptions config) {
		// 所有请求都要验证权限,权限通过才进行下一个router
		router.route(elevenBootApplicationProperties.getAuthInterceptPath())
				.handler(this.createBodyHandler())
				.handler(HXJWTAuthHandler.create(vertx,authProvider,config));
		//配置Router路由,POST请求
		router.route(HttpMethod.POST, elevenBootApplicationProperties.getServletPath())
				.handler(this.createRouteHandler());
		router.route(HttpMethod.GET, elevenBootApplicationProperties.getServletPath()+"/"+ elevenBootApplicationProperties.getDoGetUrlPath())
				.handler(this.createGetRouteHandler());
		router.route().handler(this.createCorsHandler());
		router.route(elevenBootApplicationProperties.getStaticPath()).handler(this.createStaticHandler());
		router.route().last().failureHandler(this.createGlobalDefaultExceptionHandler());
	}

	public static WebRouterInitializer build() {
		return new WebRouterInitializer();
	}

	public Router getRouter() {
		return router;
	}

	public WebRouterInitializer setRouter(Router router) {
		this.router = router;
		return this;
	}

	public JWTAuth getAuthProvider() {
		return authProvider;
	}

	public WebRouterInitializer setAuthProvider(JWTAuth authProvider) {
		this.authProvider = authProvider;
		return this;
	}

	private BodyHandler createBodyHandler(){
		return FileBodyHandlerImpl.createBodyHandler()
				.setHandleFileUploads(elevenBootApplicationProperties.getHandleFileUploads())
				.setBodyLimit(elevenBootApplicationProperties.getBodyLimit()) //设置body接受数据大小，如果要控制网络传输速度可以控制大小
				.setPreallocateBodyBuffer(elevenBootApplicationProperties.getPreallocateBodyBuffer())
				.setUploadsDirectory(elevenBootApplicationProperties.getUploadsDirectory())  //自定义文件上传到哪
				.setMergeFormAttributes(elevenBootApplicationProperties.getMergeFormAttributes())
				.setDeleteUploadedFilesOnEnd(elevenBootApplicationProperties.getDeleteUploadedFilesOnEnd()); //请求结束后是否删除上传文件
	}

	private Handler<RoutingContext> createRouteHandler(){
		return ctx->{
			HttpRequestServletRouterHandler servletRouterHandler = ElevenApplicationContextAware.
					getBean(DefaultHttpRequestServletRouterHandler.class);
			Object res = null;
			try {
				MDCThreadUtil.wrap();
				if (Optional.of(servletRouterHandler).isPresent()){
					servletRouterHandler.preRouter(ctx);
					res = servletRouterHandler.doRouter(ctx);
					servletRouterHandler.afterRouter(ctx, res);
				}else {
					res = servletRouterHandler.doRouter(ctx);
				}
				if(res == null){
					ctx.response().send("返回body为null");
					return;
				}
				// 判断是否有文件流返回,如果有则直接返回下载的文件流
				JsonObject jsonObject = JsonObject.mapFrom(res);
				Boolean fileDownload = jsonObject.getBoolean(ConstantType.FILE_STREAM);
				Object  fileDownloadPath = jsonObject.getValue(ConstantType.FILE_DOWNLOAD_PATH);
				if (fileDownload){
					ctx.response().sendFile(String.valueOf(fileDownloadPath));
				}else {
					ctx.response().send(jsonObject.toString());
				}
				MDCThreadUtil.clear();
			} catch (Throwable ex) {
				if (res != null){
					ctx.put(ConstantType.RESPONSE_BODY,res);
				}
				throw ex;
			}
		};
	}

	private Handler<RoutingContext> createGetRouteHandler(){
		return ctx->{
			HttpRequestServletRouterHandler servletRouterHandler = ElevenApplicationContextAware.
					getBean(GetHttpRequestServletRouterHandler.class);
			Object res = null;
			try {
				servletRouterHandler.preRouter(ctx);
				res = servletRouterHandler.doRouter(ctx);
				servletRouterHandler.afterRouter(ctx, res);
				if(res == null){
					ctx.response().send("返回body为null");
					return;
				}
				JsonObject jsonObject = JsonObject.mapFrom(res);
				Boolean fileDownload = jsonObject.getBoolean(ConstantType.FILE_STREAM);
				Object  fileDownloadPath = jsonObject.getValue(ConstantType.FILE_DOWNLOAD_PATH);
				if (fileDownload){
					ctx.response().sendFile(String.valueOf(fileDownloadPath));
				}else {
					ctx.response().send(jsonObject.toString());
				}
			} catch (Throwable ex) {
				ctx.put(ConstantType.RESPONSE_BODY,res);
				throw ex;
			}
		};
	}

	private CorsHandler createCorsHandler(){
		return CorsHandler.create("*").allowedHeaders(setHeaders()).allowedMethods(setMethods());
	}

	private StaticHandler createStaticHandler(){
		return StaticHandler.create(elevenBootApplicationProperties.getStaticRoot());
	}

	private GlobalDefaultExceptionHandler createGlobalDefaultExceptionHandler(){
		return GlobalDefaultExceptionHandler.of();
	}


	private Set<String> setHeaders() {
		Set<String> allowHeader = new HashSet<>();
		allowHeader.add("x-requested-with");
		allowHeader.add("Access-Control-Allow-Origin");
		allowHeader.add("origin");
		allowHeader.add("content-type");
		allowHeader.add("accept");
		allowHeader.add("X-PINGARUNER");
		return allowHeader;
	}

	private Set<HttpMethod> setMethods() {
		Set<HttpMethod> allowMethods = new HashSet<>();
		allowMethods.add(HttpMethod.GET);
		allowMethods.add(HttpMethod.POST);
		allowMethods.add(HttpMethod.OPTIONS);
		allowMethods.add(HttpMethod.DELETE);
		allowMethods.add(HttpMethod.PATCH);
		allowMethods.add(HttpMethod.PUT);
		return allowMethods;
	}

}