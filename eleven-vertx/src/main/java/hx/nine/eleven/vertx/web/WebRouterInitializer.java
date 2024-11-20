package hx.nine.eleven.vertx.web;

import hx.nine.eleven.vertx.auth.HXJWTAuthHandler;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.properties.ElevenBootApplicationProperties;
import hx.nine.eleven.vertx.handler.FileBodyHandlerImpl;
import hx.nine.eleven.vertx.handler.GlobalDefaultExceptionHandler;
import hx.nine.eleven.vertx.handler.HttpRequestBodyHandler;
import hx.nine.eleven.vertx.properties.VertxApplicationProperties;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;
import java.util.HashSet;
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
	private VertxApplicationProperties vertxApplicationProperties;

	public WebRouterInitializer(){
		elevenBootApplicationProperties = ElevenApplicationContextAware.getProperties(ElevenBootApplicationProperties.class);
		vertxApplicationProperties = ElevenApplicationContextAware.getProperties(VertxApplicationProperties.class);
	}

	public void webInit(Vertx vertx, JWTAuthOptions config) {
		// 所有请求都要验证权限,权限通过才进行下一个router
		router.route(vertxApplicationProperties.getAuthInterceptPath())
				.handler(this.createBodyHandler())
				.handler(HXJWTAuthHandler.create(vertx,authProvider,config));
		//配置Router路由,POST请求
		router.route(HttpMethod.POST, elevenBootApplicationProperties.getServletPath())
				.handler(HttpRequestBodyHandler.build());
		router.route(HttpMethod.GET, elevenBootApplicationProperties.getServletPath()+"/"+ vertxApplicationProperties.getDoGetUrlPath())
				.handler(HttpRequestBodyHandler.build());
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
				.setHandleFileUploads(vertxApplicationProperties.getHandleFileUploads())
				.setBodyLimit(vertxApplicationProperties.getBodyLimit()) //设置body接受数据大小，如果要控制网络传输速度可以控制大小
				.setPreallocateBodyBuffer(vertxApplicationProperties.getPreallocateBodyBuffer())
				.setUploadsDirectory(vertxApplicationProperties.getUploadsDirectory())  //自定义文件上传到哪
				.setMergeFormAttributes(vertxApplicationProperties.getMergeFormAttributes())
				.setDeleteUploadedFilesOnEnd(vertxApplicationProperties.getDeleteUploadedFilesOnEnd()); //请求结束后是否删除上传文件
	}

	private Handler<RoutingContext> createPostRouteHandler(){
		return ctx->{

		};
	}

	//
	@Deprecated
	private Handler<RoutingContext> createGetRouteHandler(){
		return ctx->{

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
