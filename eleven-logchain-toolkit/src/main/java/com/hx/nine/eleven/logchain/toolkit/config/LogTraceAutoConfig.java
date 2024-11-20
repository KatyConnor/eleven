package com.hx.nine.eleven.logchain.toolkit.config;

import com.hx.nine.eleven.logchain.toolkit.LogTraceProperties;
import com.hx.vertx.boot.annotations.ConditionalOnProperty;

@ConditionalOnProperty(prefix = "vertx.hx.log", name = {"enable"}, havingValue = "true",propertiesClass = LogTraceProperties.class)
public class LogTraceAutoConfig {

    private LogTraceProperties logTraceProperties;

    public LogTraceAutoConfig(LogTraceProperties logTraceProperties){
        this.logTraceProperties = logTraceProperties;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        HXLogger.build(this).info("初始化日志链路ID生成拦截器");
//        LogTraceRouterHandler logTranceInterceptor = new LogTraceRouterHandler();
//        registry.addInterceptor(logTranceInterceptor).addPathPatterns(logTraceProperties.getIncludePath())
//                .excludePathPatterns(logTraceProperties.getExcludePath());
//    }
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        WebMvcConfigurer.super.addCorsMappings(registry);
//    }
}
