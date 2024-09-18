package com.hx.vertx.file.monitor.context;

import com.hx.vertx.file.monitor.log.HXLogger;

/**
 * 容器初始化
 * @author wml
 * @date 2023-03-09
 */
public class ApplicationBeanInitializer {

    public static void initApplication(String ScanPackage){
        // 扫描class,加载配置Component
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(ScanPackage);
        scanner.initClass();
        HXLogger.build(ApplicationBeanInitializer.class).info("容器初始化完成！");
    }
}
