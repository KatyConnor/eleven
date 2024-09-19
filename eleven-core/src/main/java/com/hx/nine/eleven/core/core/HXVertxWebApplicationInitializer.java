package com.hx.nine.eleven.core.core;


import com.hx.nine.eleven.core.core.context.HXVertxApplicationContext;

/**
 * @author wml
 * @Discription
 * @Date 2023-03-18
 */
public interface HXVertxWebApplicationInitializer {

    /**
     * 服务启动扩展加载内容
     * @param context
     */
     void loadApplication(HXVertxApplicationContext context);
}
