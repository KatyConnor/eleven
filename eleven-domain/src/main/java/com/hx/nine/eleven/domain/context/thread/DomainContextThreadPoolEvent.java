package com.hx.nine.eleven.domain.context.thread;

import co.paralleluniverse.fibers.SuspendExecution;
import com.hx.nine.eleven.domain.context.DomainContext;
import com.hx.nine.eleven.domain.context.DomainContextListenerHandlerProcess;
import com.hx.nine.eleven.domain.response.ResponseEntity;
import com.hx.nine.eleven.thread.pool.executor.event.ThreadPoolEvent;

public class DomainContextThreadPoolEvent extends ThreadPoolEvent<DomainContext,ResponseEntity> {

    public DomainContextThreadPoolEvent(DomainContext domainContext) {
        super(domainContext);
    }

    /**
     * 无返回值
     */
    @Override
    public void executeEvent() {
        DomainContextListenerHandlerProcess process = new DomainContextListenerHandlerProcess();
        process.domainContextProcessing(this.getEntity());
    }

    @Override
    public ResponseEntity run() throws SuspendExecution, InterruptedException {
        return null;
    }

    /**
     * 获取返回值
     * @return
     */
    @Override
    public ResponseEntity executeCallEvent() {
        DomainContextListenerHandlerProcess process = new DomainContextListenerHandlerProcess();
        return process.domainContextProcessing(this.getEntity());
    }
}
