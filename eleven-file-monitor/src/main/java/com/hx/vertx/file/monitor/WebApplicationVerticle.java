package com.hx.vertx.file.monitor;

import com.hx.vertx.file.monitor.enums.ContentTypeEnums;
import com.hx.vertx.file.monitor.router.handler.HttpRequestBodyHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class WebApplicationVerticle extends AbstractVerticle {
  //声明Router
  Router router;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    //初始化Router
    try {
      router = Router.router(vertx);
      router.route().handler(CorsHandler.create("*").allowedHeaders(setHeaders()).allowedMethods(setMethods()));
      //配置Router路由,POST请求
      router.route(HttpMethod.POST, "/home/trade").handler(BodyHandler.create()
          .setHandleFileUploads(true)
          .setBodyLimit(2024 * 1024 * 1024) //设置body接受数据大小，如果要控制网络传输速度可以控制大小
          .setPreallocateBodyBuffer(true)
          .setUploadsDirectory("D:\\qycache\\temp_cache")  //自定义文件上传到哪
          .setMergeFormAttributes(true).setDeleteUploadedFilesOnEnd(true) //请求结束后是否删除上传文件
        )
        .handler(context -> {
          String contentType = context.request().getHeader("content-type");
          if (ContentTypeEnums.MULTIPART_FORM_DATA == ContentTypeEnums.getByCode(contentType.split(";")[0])) {
            MultiMap multiMap = context.request().params();
            multiMap.forEach((k, v) -> {
              System.out.println("-------------请求参数 start--------------");
              System.out.println(k + "=" + v);
              System.out.println("-------------请求参数 end--------------");
            });
            List<FileUpload> set = context.fileUploads();
            System.out.println("上传文件数：" + set.size());
            if (set.isEmpty()) {
              context.response().end("文件为空！");
              return;
            }
            try {
              set.forEach(f -> {
                System.out.println("name: " + f.name());
                System.out.println("fileName: " + f.fileName());
                System.out.println("uploadedFileName: " + f.uploadedFileName());
                try {
                  Files.copy(Paths.get(f.uploadedFileName()), Paths.get(f.uploadedFileName()
                    .substring(0, f.uploadedFileName().lastIndexOf(osName()==1?"\\":"/")) + (osName()==1?"\\":"/")+ f.fileName()));
                } catch (IOException e) {
                  e.printStackTrace();
                }
              });
              context.response().putHeader("Content-Type", "multipart/form-data").send("上传成功");
            } catch (Exception e) {
              context.response().putHeader("Content-Type", "application/json; charset=UTF-8")
                .send("上传失败");
              e.printStackTrace();
            }


          } else {// 普通的post JSON请求
            context.request().bodyHandler((buff) -> {
              // 文件上传
              try {
                JsonObject jsonObject = buff.toJsonObject();
                System.out.println(jsonObject.getString("requestHeader"));
                Object requestBody = jsonObject.getString("requestBody");
                System.out.println(requestBody);
                Object resBody = HttpRequestBodyHandler.build().route(requestBody);
                context.response().putHeader("Content-Type", "application/json; charset=UTF-8").send(resBody.toString());
              } catch (Exception e) {
                context.response().putHeader("Content-Type", "application/json; charset=UTF-8")
                  .send("上传失败");
                e.printStackTrace();
              }
            });
          }
        });

      // 配置静态文件
      router.route("/*").handler(StaticHandler.create("webroot/web"));
      //将Router与vertx HttpServer 绑定
      vertx.createHttpServer().requestHandler(router).listen(20888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 20888");
        } else {
          startPromise.fail(http.cause());
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
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

  private int osName() {
    int result = -1;
    String osName = System.getProperty("os.name");
    String osNameLowerCase = osName.toLowerCase(Locale.ROOT);
    if (osNameLowerCase.startsWith("mac") || osNameLowerCase.startsWith("darwin")) {
      result = 0;
    } else if (osNameLowerCase.startsWith("wind")) {
      result = 1;
    } else if (osNameLowerCase.startsWith("linux")) {
      result = 2;
    }
    return result;
  }

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
