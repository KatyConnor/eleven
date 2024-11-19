package com.hx.nine.eleven.domain.service;

import com.hx.nine.eleven.domain.exception.DomainOperatorException;
import com.hx.nine.eleven.domain.obj.dto.HeaderDTO;
import com.hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import com.hx.nine.eleven.domain.context.DomainContextAware;
import com.hx.nine.eleven.domain.enums.WebRouteParamsEnums;
import com.hx.nine.eleven.domain.exception.ParamsValidationExcetion;
import com.hx.nine.eleven.domain.factory.DefaultDomainFactory;
import com.hx.nine.eleven.domain.function.DomainExecutor;
import com.hx.nine.eleven.domain.obj.dto.BaseDTO;
import com.hx.nine.eleven.domain.response.ResponseEntity;
import com.hx.nine.eleven.commons.entity.ValidationResultEntity;
import com.hx.nine.eleven.commons.utils.StringUtils;
import com.hx.nine.eleven.commons.utils.ValidationUtils;
import com.hx.nine.eleven.domain.context.DomainContext;
import com.hx.nine.eleven.domain.conver.BeanConvert;
import com.esotericsoftware.reflectasm.ConstructorAccess;
import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.jdbc.ElevenJdbcTransactionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.annotation.Resource;
import java.util.Optional;

/**
 * facade 接口调用入口
 * @author wangml
 * @Date 2019-08-29
 */
@Component
public class DomainExecuteService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DomainExecuteService.class);

    @Resource
    private DefaultDomainFactory factory;

    public DomainExecuteService(){
        this.factory = DefaultElevenApplicationContext.build().getBean(DefaultDomainFactory.class);
    }

    /**
     * 执行 Service，不生成domain对象，适用于普通service类型，包含事务
     * 适用于做增、删、改，操作
     * 捕获Exception异常，回滚事务
     *
     * @param executor Domain 执行器
     * @param <T>      方法返回实体（泛型）
     * @return 返回泛型T
     */
    public <T> T excuteTransaction(Class<T> response, DomainExecutor<T> executor) {
        ElevenJdbcTransactionManager transactionManager = ElevenApplicationContextAware.getBean(ElevenJdbcTransactionManager.class);
        boolean rollback = false;
        try{
            transactionManager.begin();
            DomainContext context = DomainContextAware.build().getDomainContext();
            validation(context.getRequestHeaderDTO());
            validation(context.getRequestBody());
            return exec(response, executor);
        }catch (Throwable ex){
            try{
                transactionManager.rollback();
            }catch (Exception exception){
                LOGGER.error("业务处理失败,事务已经回滚",exception);
            }
            rollback = true;
            throw new DomainOperatorException(DomainApplicationSysCode.B0100000004,ex);
        }finally {
            if (!rollback){
                transactionManager.commit();
            }
        }
    }

    /**
     * 执行 Service，不生成domain对象，适用于普通service类型，不包含事务
     * 适用于做 查询操作，如果是增、删、改 操作需要自己控制事务。
     *
     * @param executor 执行器
     * @param <T>      方法返回实体（泛型）
     * @return 返回泛型T
     */
    public <T> T excute(Class<T> response, DomainExecutor<T> executor) {
        DomainContext context = DomainContextAware.build().getDomainContext();
        validation(context.getRequestHeaderDTO());
        validation(context.getRequestBody());
        return exec(response, executor);
    }

    /**
     * 执行Service，生成domain对象，不包含事务
     * 适用于插叙操作
     *
     * @param executor 执行器
     * @param <T>      方法返回实体（泛型）
     * @return 返回泛型T
     */
    public <T> T query(Class<T> response, DomainExecutor<T> executor) {
        DomainContext context = DomainContextAware.build().getDomainContext();
        validation(context.getRequestHeaderDTO());
        validation(context.getRequestBody());
        return exec(response, executor);
    }

    /**
     *
     * @param responseClass
     * @param executor
     * @param <T>
     * @return
     */
    private <T> T exec(Class<T> responseClass, DomainExecutor<T> executor) {
        T response = createResponse(responseClass);
        executor.execute(response, this.factory);
        return response;
    }

    private <O extends BaseDTO> void validation(O dto) {
        Optional.ofNullable(dto).ifPresent(d ->{
            ValidationResultEntity result = ValidationUtils.validateEntity(dto);
            if (result.isHasErrors()) {
                throw new ParamsValidationExcetion(result.getErrorMsg());
            }
        });
    }

    /**
     * 设置返回实体
     *
     * @param response 返回接收实体对象
     * @param <T>      泛型类型
     * @return 返回DomainExecuteService对象本身
     */
    private <T> T createResponse(Class<T> response) {
        T obj = null;
        try {
            ConstructorAccess constructorAccess = ConstructorAccess.get(response);
            obj = (T)constructorAccess.newInstance();
            // 设置返回报文头参数
            if (ResponseEntity.class.isInstance(obj)){
                HeaderDTO requestHeaderDTO = DomainContextAware.build().getDomainContext().getRequestHeaderDTO();
                Object resHeader = BeanConvert.convert(requestHeaderDTO,null,requestHeaderDTO.getHeaderCode(),
                        WebRouteParamsEnums.HEADER_VO.getName());
                ((ResponseEntity)obj).setResponseHeader(resHeader);
            }
        } catch (Exception e) {
            LOGGER.error(StringUtils.format("Failed to initialize create Response object ,[{}]", response.getSimpleName()), e);
        }
        return obj;
    }
}
