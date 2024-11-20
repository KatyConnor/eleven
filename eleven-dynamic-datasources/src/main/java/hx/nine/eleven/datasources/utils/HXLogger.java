package hx.nine.eleven.datasources.utils;

import hx.nine.eleven.commons.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;

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

    private static Logger init(String name){
        Logger logger = null;
        if (!loggerMap.containsKey(name)){
            Logger LOGGER = LoggerFactory.getLogger(name);
            loggerMap.put(name,LOGGER);
            return LOGGER;
        }
        logger = loggerMap.get(name);

        return logger;
    }

}
