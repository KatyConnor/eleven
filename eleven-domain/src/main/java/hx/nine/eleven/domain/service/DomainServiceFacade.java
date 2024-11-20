package hx.nine.eleven.domain.service;

import hx.nine.eleven.domain.response.ResponseEntity;

/**
 * 异步任务处理
 * @author wml
 * @date 200-12-01
 */
@Deprecated
public interface DomainServiceFacade {

    ResponseEntity doServiceCall();

    void doService();
}
