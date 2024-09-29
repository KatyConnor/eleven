package com.hx.nine.eleven.vertx.handler.servlet.request;

import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.vertx.handler.WebRequestServiceHandler;
import com.hx.nine.eleven.vertx.handler.servlet.ServletHandler;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.core.web.DefaultHttpHeader;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * JSON格式报文数据处理
 */
public class ApplicationJsonRequestServletHandler implements ServletHandler {
    @Override
    public Object doRequest(RoutingContext context) {
        Object res = null;
        // 读取Body数据
        Buffer bodyBuffer = context.getBody();
        JsonObject jsonObject = null;
        if (!Optional.ofNullable(bodyBuffer).isPresent()) {
           try{
               AtomicReference<JsonObject> jsonObjectBuff = new AtomicReference<>();
               context.request().bodyHandler((buff) -> {
                   jsonObjectBuff.set(buff.toJsonObject());
               });
               jsonObject = jsonObjectBuff.get();
           }catch (Exception exception){
               // body数据为空
               ElevenLoggerFactory.build(this).error("request body is null,or Request has already been read");
           }
        } else {
            jsonObject = bodyBuffer.toJsonObject();
        }

        try {
            // 调用路由
            WebRequestServiceHandler servletHandler = DefaultElevenApplicationContext.build().getBean(WebRequestServiceHandler.class);
            if (!Optional.of(servletHandler).isPresent()) {
                throw new RuntimeException("could not find class implement WebRequestServiceHandler");
            }
            // 设置返回信息
            setHeader(context, DefaultHttpHeader.build().setApplicationJsonResHeader().getHeaders());
            res = servletHandler.doService(context, jsonObject);
        } catch (Throwable ex) {
            ElevenLoggerFactory.build(this).error("------POST请求交易处理失败");
            throw ex;
        }
        return res;
    }

    public static ApplicationJsonRequestServletHandler build() {
        return new ApplicationJsonRequestServletHandler();
    }
}
