package com.hx.nine.eleven.core.handler;

import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.handler.servlet.request.ApplicationJsonGetRequestServletHandler;
import io.vertx.ext.web.RoutingContext;

import java.util.Optional;
import java.util.Set;

/**
 * @author wml
 * @Discription
 * @Date 2023-07-13
 */
public class GetHttpRequestServletRouterHandler implements HttpRequestServletRouterHandler{

    private Set<HttpRequestServletRouterHandler> routerHandlers;

    @Override
    public void preRouter(RoutingContext context) {
        routerHandlers = ElevenApplicationContextAware.getSubTypesOfBeans(HttpRequestServletRouterHandler.class);
        routerHandlers.forEach(handler -> {
            if (!(handler instanceof GetHttpRequestServletRouterHandler)){
                handler.preRouter(context);
            }
        });
    }

    @Override
    public Object doRouter(RoutingContext context) {
        return ApplicationJsonGetRequestServletHandler.build().doRequest(context);
    }

    @Override
    public void afterRouter(RoutingContext context, Object res) {
        Optional.ofNullable(routerHandlers).ifPresent(handlers -> {
            handlers.forEach(handler -> {
                if (!(handler instanceof GetHttpRequestServletRouterHandler)){
                    handler.afterRouter(context, res);
                }
            });
        });
    }
}
