package com.hx.nine.eleven.core.core;


import com.hx.nine.eleven.core.core.context.ElevenApplicationContext;

/**
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
