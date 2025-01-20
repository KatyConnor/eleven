package hx.nine.eleven.vertx.web;

import hx.nine.eleven.commons.utils.ObjectUtils;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.properties.ElevenBootApplicationProperties;
import hx.nine.eleven.core.task.ElevenScheduledTask;
import hx.nine.eleven.core.task.ElevenThreadTask;
import hx.nine.eleven.vertx.properties.VertxApplicationProperties;
import hx.nine.eleven.vertx.utils.ElevenHttpclient;
import hx.nine.eleven.core.utils.MDCThreadUtil;
import hx.nine.eleven.vertx.utils.ElevenObjectUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.file.FileSystem;
import io.vertx.core.http.HttpClient;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;

import java.util.Set;


/**
 * web服务Verticle启动类
 *
 * @author wml
 * @date 2023-03-24
 */
public class VertxWebApplicationVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(VertxWebApplicationVerticle.class);

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
		VertxApplicationProperties properties = ElevenApplicationContextAware.getProperties(VertxApplicationProperties.class);
		ElevenBootApplicationProperties elevenBootApplicationProperties = ElevenApplicationContextAware.getElevenApplicationProperties();
		router = Router.router(vertx);
		JWTAuthOptions config = new JWTAuthOptions().addPubSecKey(new PubSecKeyOptions().setAlgorithm(properties.getAlgorithm())
				.setBuffer(properties.getJwtSecretKey()));
		JWTAuth authProvider = JWTAuth.create(vertx, config);
		WebRouterInitializer.build().setAuthProvider(authProvider).setRouter(router).webInit(vertx, config);
		//将Router与vertx HttpServer 绑定
		vertx.createHttpServer().requestHandler(router).listen(elevenBootApplicationProperties.getPort(), http -> {
			if (http.succeeded()) {
				startPromise.complete();
				LOGGER.info("-----------------HTTP server started on port: " + elevenBootApplicationProperties.getPort());
			} else {
				startPromise.fail(http.cause());
			}
		});

		try{
			Set<ElevenScheduledTask> scheduledTask = ElevenApplicationContextAware.getSubTypesOfBeans(ElevenScheduledTask.class);
			Set<ElevenThreadTask> threadTask = ElevenApplicationContextAware.getSubTypesOfBeans(ElevenThreadTask.class);
			if (ObjectUtils.isNotEmpty(scheduledTask)) {
				vertx.setPeriodic(30000, 1000, id -> {
					if (ObjectUtils.isNotEmpty(scheduledTask)) {
						scheduledTask.forEach(s -> {
							vertx.executeBlocking(future -> {
								MDCThreadUtil.wrap();
								s.runScheduleTask();
								future.complete();
								MDCThreadUtil.clear();
							}, true, ctx -> {
								if (ctx.succeeded()) {

								} else if (ctx.failed()) {

								}
							});
						});
					}
				});
			}
			if (ObjectUtils.isNotEmpty(threadTask)) {
				vertx.setPeriodic(30000, 1000, id -> {
					if (ObjectUtils.isNotEmpty(threadTask)) {
						threadTask.forEach(s -> {
							vertx.executeBlocking(future -> {
								MDCThreadUtil.wrap();
								s.run();
								future.complete();
								MDCThreadUtil.clear();
							}, true, ctx -> {
								if (ctx.succeeded()) {

								} else if (ctx.failed()) {

								}
							});
						});

					}
				});
			}
		}catch (Exception e){
			LOGGER.error("启动定时任务异常");
		}

		fs = vertx.fileSystem();
		httpClient = vertx.createHttpClient();
		ElevenObjectUtils.build().setFileSystem(fs);
		ElevenHttpclient.build().setHttpClient(httpClient);
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
