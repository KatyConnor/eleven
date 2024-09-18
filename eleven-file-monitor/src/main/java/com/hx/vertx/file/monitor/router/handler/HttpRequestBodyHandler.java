package com.hx.vertx.file.monitor.router.handler;

import io.vertx.core.buffer.Buffer;

/**
 * 请求信息处理
 */
public class HttpRequestBodyHandler {

  private HttpRequestBodyHandler(){};

  public Object body(Buffer buffer){
    return null;
  }

  public Object route(Object body){

    return "请求成功";
  }

  public static HttpRequestBodyHandler build(){
    return Single.INSTANCE;
  }

  private final static class Single {
    public final static HttpRequestBodyHandler INSTANCE = new HttpRequestBodyHandler();
  }
}
