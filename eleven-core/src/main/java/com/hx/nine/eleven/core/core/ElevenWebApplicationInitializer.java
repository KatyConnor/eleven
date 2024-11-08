package com.hx.nine.eleven.core.core;


import com.hx.nine.eleven.core.core.context.ElevenApplicationContext;

/**
 * 用于扩展处理，可以将处理后信息放入 ElevenApplicationContext 上下文中
 * @author wml
 * @Discription
 * @Date 2023-03-18
 */
public interface ElevenWebApplicationInitializer {

    /**
     * 服务启动扩展加载内容
     * @param context
     */
     void loadApplication(ElevenApplicationContext context);
}
