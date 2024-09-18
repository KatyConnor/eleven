package com.hx.logchain.toolkit;

import com.hx.logchain.toolkit.util.MDCThreadUtil;
import org.slf4j.MDC;
import java.util.Map;

import static com.hx.logchain.toolkit.common.LogTraceCommon.TRACE_ID;

public abstract class HXLogThread extends Thread implements HXLogRunnable {

    private Map<String, String> context;

    public Map<String, String> getContext() {
        return context;
    }

    public void setContext(Map<String, String> context) {
        this.context = context;
    }

    public HXLogThread() {
        super();
    }

    public HXLogThread(Runnable target) {
        super(target);
    }

    public HXLogThread(ThreadGroup group, Runnable target) {
        super(group, target);
    }

    public HXLogThread(String name) {
        super(name);
    }

    public HXLogThread(ThreadGroup group, String name) {
        super(group, name);
    }

    public HXLogThread(Runnable target, String name) {
        super(target, name);
    }

    public HXLogThread(ThreadGroup group, Runnable target, String name) {
        super(group, target, name);
    }

    public HXLogThread(ThreadGroup group, String name, long stackSize) {
        super(group, null, name, stackSize);
    }

    public HXLogThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        super(group, target, name, stackSize);
    }

    @Override
    public void run() {
        if (context == null) {
            MDC.clear();
        } else {
            MDC.setContextMap(context);
        }

        if (MDC.get(TRACE_ID) == null) {
            MDC.put(TRACE_ID, MDCThreadUtil.generateTraceId());
        }

        try {
            this.doRun();
        } finally {
            MDC.clear();
        }
    }
}
