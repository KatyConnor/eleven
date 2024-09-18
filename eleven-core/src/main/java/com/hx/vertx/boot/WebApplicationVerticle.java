package com.hx.vertx.boot;

import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.core.context.DefaultVertxApplicationContext;
import com.hx.vertx.boot.properties.VertxApplicationProperties;
import com.hx.vertx.boot.task.HXVertxScheduledTask;
import com.hx.vertx.boot.task.HXVertxThreadTask;
import com.hx.vertx.boot.utils.HXVertxHttpclient;
import com.hx.vertx.boot.utils.MDCThreadUtil;
import com.hx.vertx.boot.utils.VertxObjectUtils;
import com.hx.vertx.boot.web.WebRouterInitializer;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;


/**
 * web服务Verticle启动类
 *
 * @author wml
 * @date 2023-03-24
 */
public class WebApplicationVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebApplicationVerticle.class);

	//声明Router路由
	Router router;
	//文件处理
	FileSystem fs;
	//httpclient客户端
	HttpClient httpClient;

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		//初始化Router
		// 配置静态文件
		VertxApplicationProperties vertxApplicationProperties = VertxApplicationContextAware.getVertxApplicationProperties();
		router = Router.router(vertx);
		JWTAuthOptions config = new JWTAuthOptions()
				.addPubSecKey(new PubSecKeyOptions()
						.setAlgorithm(vertxApplicationProperties.getAlgorithm())
						.setBuffer(vertxApplicationProperties.getJwtSecretKey()));
		JWTAuth authProvider = JWTAuth.create(vertx, config);
		WebRouterInitializer.build().setAuthProvider(authProvider).setRouter(router).webInit(vertx,config);
		//将Router与vertx HttpServer 绑定
		vertx.createHttpServer().requestHandler(router).listen(vertxApplicationProperties.getPort(), http -> {
			if (http.succeeded()) {
				startPromise.complete();
				LOGGER.info("-----------------HTTP server started on port" + vertxApplicationProperties.getPort());
			} else {
				startPromise.fail(http.cause());
			}
		});

		HXVertxScheduledTask scheduledTask = VertxApplicationContextAware.getSubTypesOfBean(HXVertxScheduledTask.class);
		if (ObjectUtils.isNotEmpty(scheduledTask)) {
			vertx.setPeriodic(7000, 1000, id -> {
				if (ObjectUtils.isNotEmpty(scheduledTask)) {
					vertx.executeBlocking(future -> {
						MDCThreadUtil.wrap();
						scheduledTask.runScheduleTask();
						future.complete();
						MDCThreadUtil.clear();
					}, true, ctx -> {
						if (ctx.succeeded()) {

						} else if (ctx.failed()) {

						}
					});
				}
			});
			HXVertxThreadTask threadTask = VertxApplicationContextAware.getSubTypesOfBean(HXVertxThreadTask.class);
			vertx.setPeriodic(8000, 200, id -> {
				if (ObjectUtils.isNotEmpty(threadTask)) {
					vertx.executeBlocking(future -> {
						MDCThreadUtil.wrap();
						threadTask.run();
						future.complete();
						MDCThreadUtil.clear();
					}, true, ctx -> {
						if (ctx.succeeded()) {

						} else if (ctx.failed()) {

						}
					});
				}
			});
		}

		fs = vertx.fileSystem();
		httpClient = vertx.createHttpClient();
		VertxObjectUtils.build().setFileSystem(fs);
		HXVertxHttpclient.build().setHttpClient(httpClient);
	}

//	public static void main(String[] args) {
//		System.out.println(String.format("[%s]任务执行成功，result=[%s]","thread-1","同步"));
//	}

	// 文件发送代码
//  MultipartForm multipartForm = MultipartForm.create();
//  //处理其他非文件属性
//  MultiMap attributes = routingContext.request()
//    .formAttributes();
//            attributes.forEach(attribute -> {
//    multipartForm.attribute(attribute.getKey(), attribute.getValue());
//  });
////处理文件上传参数
//            routingContext.fileUploads()
//              .forEach(fileUpload -> {
////注意这四个参数分别传什么
//    multipartForm.binaryFileUpload(fileUpload.name(), fileUpload.fileName(),
//      fileUpload.uploadedFileName(), fileUpload.contentType());
//  });
//request.timeout(10000).sendMultipartForm(multipartForm, ar -> {
//
//    if (ar.succeeded()) {
//      //
//    }else{
//      //
//    }
//
//  });
}
