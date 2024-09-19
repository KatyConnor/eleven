package com.hx.nine.eleven.core.handler.servlet.request;

import com.hx.nine.eleven.core.core.context.DefaultVertxApplicationContext;
import com.hx.nine.eleven.core.handler.WebRequestServiceHandler;
import com.hx.nine.eleven.core.handler.servlet.ServletHandler;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import java.util.Optional;

/**
 * @author wml
 * @Discription
 * @Date 2023-07-13
 */
public class ApplicationJsonGetRequestServletHandler implements ServletHandler {
    @Override
    public Object doRequest(RoutingContext context) {
        JsonObject jsonObject = new JsonObject();
        MultiMap multiMap = context.request().params();
        if (Optional.ofNullable(multiMap).isPresent() && multiMap.entries().size() > 0) {
            multiMap.entries().forEach(e -> {
                jsonObject.put(e.getKey(), e.getValue());
            });
        }
        // 调用路由
        WebRequestServiceHandler servletHandler = DefaultVertxApplicationContext.build().getBean(WebRequestServiceHandler.class);
        if(!Optional.of(servletHandler).isPresent()){
            throw new RuntimeException("could not find class implement WebRequestServiceHandler");
        }
        return servletHandler.doService(context, jsonObject);
    }

    public static ApplicationJsonGetRequestServletHandler build() {
        return new ApplicationJsonGetRequestServletHandler();
    }

}
