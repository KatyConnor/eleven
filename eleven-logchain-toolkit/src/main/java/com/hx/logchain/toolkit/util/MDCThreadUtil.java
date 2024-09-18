package com.hx.logchain.toolkit.util;

import com.hx.logchain.toolkit.HXLogRunnable;
import com.github.f4b6a3.ulid.UlidCreator;
import org.slf4j.MDC;

import java.util.Map;

import static com.hx.logchain.toolkit.common.LogTraceCommon.TRACE_ID;

/**
 * MDC 线程相关处理工具
 *
 * @Author yeshan
 * @Date 2023/1/4
 */
public class MDCThreadUtil {
    /**
     * 生成traceId
     *
     * @return traceId
     */
    public static String generateTraceId() {
        return UlidCreator.getUlid().toString();
    }

    /**
     * 父线程trace_id给子线程
     */
    public static void wrap() {
        Map<String, String> context = MDC.getCopyOfContextMap();
        if (context == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }
        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, generateTraceId());
        }
    }

    /**
     * 复杂父线程trace_id给子线程
     *
     * @param runnable 子线程
     * @param context  MDC上下文数据
     * @return
     */
    public static Runnable wrap(Runnable runnable, Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }

            if (MDC.get(TRACE_ID) == null) {
                MDC.put(TRACE_ID, generateTraceId());
            }

            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }

    /**
     * 复杂父线程trace_id给子线程
     *
     * @param runnable 子线程
     * @param context  MDC上下文数据
     * @return
     */
    public static Runnable logwrap(HXLogRunnable runnable, Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }

            if (MDC.get(TRACE_ID) == null) {
                MDC.put(TRACE_ID, generateTraceId());
            }

            try {
                runnable.doRun();
            } finally {
                MDC.clear();
            }
        };
    }
}
