package com.hx.nine.eleven.logchain.toolkit.handler;


import com.hx.nine.eleven.logchain.toolkit.util.MDCThreadUtil;
import com.hx.nine.eleven.logchain.toolkit.common.LogTraceCommon;
import com.hx.vertx.boot.handler.HttpRequestServletRouterHandler;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.MDC;
import java.util.Optional;

/**
 * 日志链拦截器
 *
 * @Author yeshan
 * @Date 2023/1/3
 */
public class LogTraceRouterHandler implements HttpRequestServletRouterHandler {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        return true;
//    }

//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//
//    }

    @Override
    public void preRouter(RoutingContext routingContext) {
        String traceId = Optional.ofNullable(routingContext.request().
                getHeader(LogTraceCommon.TRACE_ID)).
                orElse(MDCThreadUtil.generateTraceId());
        MDC.put(LogTraceCommon.TRACE_ID, traceId);
    }

    @Override
    public Object doRouter(RoutingContext routingContext) {
        return null;
    }

    @Override
    public void afterRouter(RoutingContext routingContext, Object o) {
        MDC.remove(LogTraceCommon.TRACE_ID);
    }
}
