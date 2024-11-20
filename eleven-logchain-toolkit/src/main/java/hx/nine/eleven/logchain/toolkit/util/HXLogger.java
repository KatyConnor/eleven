package hx.nine.eleven.logchain.toolkit.util;

import hx.nine.eleven.logchain.toolkit.Logger;
import hx.nine.eleven.logchain.toolkit.mapper.LoggerPO;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于slf4j 实现日志统一输出
 * @TODO 后续添加自定义日志输出以及日志入库
 *
 * @Author wml
 * @Date 2023/2/6
 */
public class HXLogger implements Logger {

    /**
     * The global logger threshold.
     */
    private static volatile Logger.Level globalThreshold = Logger.Level.TRACE;

    // 老版本兼容写法
    private static org.slf4j.Logger LOGGER =  LoggerFactory.getLogger(HXLogger.class);
    // V1.1.0 升级版本写法
    private org.slf4j.Logger slfLogger;

    private static Map<String, org.slf4j.Logger> loggerMap = new HashMap<>();


    /**
     * Whether calls to {@link #trace(Object)} are possible.
     */
    private boolean                   supportsTrace   = true;

    /**
     * Whether calls to {@link #debug(Object)} are possible.
     */
    private boolean                   supportsDebug   = true;

    /**
     * Whether calls to {@link #info(Object)} are possible.
     */
    private boolean                   supportsInfo    = true;

    private HXLogger(){

    }

    public static HXLogger getHxLogger(Class<?> clazz){
        HXLogger hxLogger = new HXLogger();
        hxLogger.slfLogger = LoggerFactory.getLogger(clazz);
        try {
            hxLogger.isInfoEnabled();
        }
        catch (Throwable e) {
            hxLogger.supportsInfo = false;
        }

        try {
            hxLogger.isDebugEnabled();
        }
        catch (Throwable e) {
            hxLogger.supportsDebug = false;
        }

        try {
            hxLogger.isTraceEnabled();
        }
        catch (Throwable e) {
            hxLogger.supportsTrace = false;
        }
        return hxLogger;
    }

    public static org.slf4j.Logger build(String name){
        if (StringUtils.isEmpty(name)){
            LOGGER.warn("日志输出目标类为null，将使用默认记录");
            return LOGGER;
        }
        return init(name);
    }

    public static org.slf4j.Logger build(Class<?> clazz){
        if (clazz == null){
            LOGGER.warn("日志输出目标类为null，将使用默认日志记录");
            return LOGGER;
        }

        return init(clazz.getName());
    }

    public static org.slf4j.Logger build(Object obj){
        if (obj == null){
            LOGGER.warn("日志输出目标类为null，将使用默认日志记录");
            return LOGGER;
        }
        return init(obj.getClass().getName());
    }

    /**
     * 日志信息入库
     */
    public void saveLog(LoggerPO loggerPO){
//        org.slf4j.Logger logger = init(JooqBaseMapperFactory.class);
//        logger.info("{}",loggerPO.toString());
//        insert(loggerPO);
    }

    private static org.slf4j.Logger init(String name){
        return getLogger(name);
    }

    private static org.slf4j.Logger init(Class<?> clazz){
        return getLogger(clazz);
    }

    private static org.slf4j.Logger getLogger(String name){
        if (!loggerMap.containsKey(name)){
            loggerMap.put(name,LoggerFactory.getLogger(name));
        }
        return loggerMap.get(name);
    }

    private static org.slf4j.Logger getLogger(Class<?> clazz){
        String name = clazz.getName();
        if (!loggerMap.containsKey(name)){
            loggerMap.put(name,LoggerFactory.getLogger(clazz));
        }
        return loggerMap.get(name);
    }

    /**
     * 日志入库
     * @param
     */
//    private void insert(LoggerPO loggerPO){
//        JooqBaseMapperFactory.build(null,null).insert(loggerPO);
//    }

    @Override
    public boolean isTraceEnabled() {
        if (!globalThreshold.supports(Logger.Level.TRACE)){
            return false;
        }

       if (!supportsTrace){
           return false;
       }

        return slfLogger.isTraceEnabled();
    }

    @Override
    public void trace(Object message) {
        trace(message, (Object) null);
    }

    @Override
    public void trace(Object message, Object details) {
        if (!globalThreshold.supports(Logger.Level.TRACE)){
            return;
        }
        slfLogger.trace(getMessage(message, details));
    }

    @Override
    public void trace(Object message, Throwable throwable) {
        trace(message, null, throwable);
    }

    @Override
    public void trace(Object message, Object details, Throwable throwable) {
        if (!globalThreshold.supports(Logger.Level.TRACE)){
            return;
        }
        slfLogger.trace(getMessage(message, details), throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void debug(Object message) {

    }

    @Override
    public void debug(Object message, Object details) {

    }

    @Override
    public void debug(Object message, Throwable throwable) {

    }

    @Override
    public void debug(Object message, Object details, Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void info(Object message) {

    }

    @Override
    public void info(Object message, Object details) {

    }

    @Override
    public void info(Object message, Throwable throwable) {

    }

    @Override
    public void info(Object message, Object details, Throwable throwable) {

    }

    @Override
    public void warn(Object message) {

    }

    @Override
    public void warn(Object message, Object details) {

    }

    @Override
    public void warn(Object message, Throwable throwable) {

    }

    @Override
    public void warn(Object message, Object details, Throwable throwable) {

    }

    @Override
    public void error(Object message) {

    }

    @Override
    public void error(Object message, Object details) {

    }

    @Override
    public void error(Object message, Throwable throwable) {

    }

    @Override
    public void error(Object message, Object details, Throwable throwable) {

    }

    @Override
    public void log(Logger.Level level, Object message) {

    }

    @Override
    public void log(Logger.Level level, Object message, Object details) {

    }

    @Override
    public void log(Logger.Level level, Object message, Throwable throwable) {

    }

    @Override
    public void log(Logger.Level level, Object message, Object details, Throwable throwable) {

    }
    private String getMessage(Object message, Object details) {
        StringBuilder sb = new StringBuilder();
        sb.append(hx.nine.eleven.logchain.toolkit.util.StringUtils.rightPad("" + message, 25));
        if (details != null) {
            sb.append(": ");
            sb.append(details);
        }
        return sb.toString();
    }
}
