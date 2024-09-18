package com.hx.vertx.file.monitor.log;

import com.hx.vertx.file.monitor.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于slf4j 实现日志统一输出
 * @TODO 后续添加自定义日志输出
 *
 * @Author wml
 * @Date 2023/2/6
 */
public class HXLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(HXLogger.class);

    private static Map<String,Logger> loggerMap = new HashMap<>();

    private HXLogger(){

    }

    public static Logger build(String name){
        if (StringUtils.isEmpty(name)){
            LOGGER.warn("日志输出目标类为null，将使用默认记录");
            return LOGGER;
        }
        return init(name);
    }

    public static Logger build(Class<?> clazz){
        if (clazz == null){
            LOGGER.warn("日志输出目标类为null，将使用默认日志记录");
            return LOGGER;
        }

        return init(clazz.getName());
    }

    public static Logger build(Object obj){
        if (obj == null){
            LOGGER.warn("日志输出目标类为null，将使用默认日志记录");
            return LOGGER;
        }
        return init(obj.getClass().getName());
    }

    /**
     * 日志信息入库
     */
//    public void saveLog(LoggerPO loggerPO){
//        Logger logger = init(LoggerBaseMapper.class);
//        logger.info("{}",loggerPO.toString());
//        insert(loggerPO);
//    }

    private static Logger init(String name){
        return getLogger(name);
    }

    private static Logger init(Class<?> clazz){
        return getLogger(clazz);
    }

    private static Logger getLogger(String name){
        if (!loggerMap.containsKey(name)){
            loggerMap.put(name,LoggerFactory.getLogger(name));
        }
        return loggerMap.get(name);
    }

    private static Logger getLogger(Class<?> clazz){
        String name = clazz.getName();
        if (!loggerMap.containsKey(name)){
            loggerMap.put(name,LoggerFactory.getLogger(clazz));
        }
        return loggerMap.get(name);
    }

//    private void insert(LoggerPO loggerPO){
//        LoggerBaseMapper loggerBaseMapper = BeanFactoryLocator.getBean(LoggerBaseMapper.class);
//        loggerBaseMapper.insert(loggerPO);
//    }
}
