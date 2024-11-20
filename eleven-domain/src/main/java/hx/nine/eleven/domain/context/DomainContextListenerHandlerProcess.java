package hx.nine.eleven.domain.context;

import co.paralleluniverse.fibers.Suspendable;
import hx.nine.eleven.domain.BeanFactoryLocator;
import hx.nine.eleven.domain.syscode.DomainApplicationSysCode;
import hx.nine.eleven.domain.utils.MessageCodeUtils;
import hx.nine.eleven.domain.obj.vo.ErrorVO;
import hx.nine.eleven.commons.utils.Builder;
import hx.nine.eleven.commons.utils.StringUtils;
import hx.nine.eleven.domain.response.ResponseEntity;
import hx.nine.eleven.domain.service.WebServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

/**
 * 上下文任务队列消费处理,借助线程池处理
 * @since 1.0
 * 添加协程来处理任务
 * @since 2.0
 * @author wml
 * @date 2022-11-29
 */
public class DomainContextListenerHandlerProcess implements DomainContextListenerHandler{

    private final static Logger LOGGER = LoggerFactory.getLogger(DomainContextListenerHandlerProcess.class);

    /**
     * 消费处理domainContextEvent的领域对象
     */

    @Override
    @Suspendable
    public ResponseEntity domainContextProcessing(DomainContext context) {
        // 1、消费处理
        if (!checkDomainContext(context)){
            return null;
        }
        LOGGER.info("开始消费处理DomainContextEvent");
        // 如果是线程池，需要将当前线程池执行所需要的上下文放入容器中，便于在后续操作中可以获取当前线程的上下文进行处理
        DomainContextAware.build().refreshContext(context);
        // 2、执行消费处理
        return proccess(context);
    }

    @Suspendable
    private boolean checkDomainContext(DomainContext context){
        if (context == null){
            LOGGER.info("需要处理 DomainContext 为null");
            return false;
        }
        return true;
    }

    /**
     * 消费处理任务
     * @param context
     * @return
     */
    @Suspendable
    private ResponseEntity proccess(DomainContext context){
        ResponseEntity responseEntity = null;
        try{
            String tradeCode = context.getRequestHeaderDTO().getTradeCode();
            String subTradeCode = context.getRequestHeaderDTO().getSubTradeCode();
            WebServiceFacade serviceFacade = BeanFactoryLocator.getBean(tradeCode);
            if (!Optional.ofNullable(serviceFacade).isPresent()) {
                LOGGER.info("不支持 [{}] 交易",tradeCode);
                ErrorVO errorVO = Builder.of(ErrorVO::new).
                        with(ErrorVO::setErrorMessageCode, MessageCodeUtils.format(DomainApplicationSysCode.A0300000005,tradeCode)).build();
                return ResponseEntity.build().addData(errorVO).failure();
            }

            if (DomainContextAware.build().getDomainContextEvent().getEnableDomainSupport()
                    && StringUtils.isNotBlank(subTradeCode)){
                responseEntity = (ResponseEntity) DomainServiceRouteSupport.invokDomainService(subTradeCode,serviceFacade);
            }else {
                responseEntity = serviceFacade.doService();
            }
            LOGGER.info("消费处理DomainContextEvent完成");
        }catch (Exception ex){
            LOGGER.error("消费处理DomainContextEvent异常:",ex);
            responseEntity = responseEntity == null?ResponseEntity.build().failure():responseEntity.failure();
        }finally {
            // 处理完毕执行，销毁当前线程的上下文
            DomainContextAware.build().destroyContext();
        }
        return responseEntity;
    }
}
