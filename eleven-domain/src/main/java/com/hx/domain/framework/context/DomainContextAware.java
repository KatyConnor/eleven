package com.hx.domain.framework.context;

import com.hx.domain.framework.BeanFactoryLocator;
import com.hx.domain.framework.constant.WebHttpBodyConstant;
import com.hx.domain.framework.context.thread.DomainContextThreadPoolEvent;
import com.hx.domain.framework.obj.dto.HeaderDTO;
import com.hx.domain.framework.obj.vo.ErrorVO;
import com.hx.domain.framework.properties.DomainEventListenerHandlerProperties;
import com.hx.domain.framework.conver.BeanConvert;
import com.hx.domain.framework.enums.WebRouteParamsEnums;
import com.hx.domain.framework.obj.bo.BaseOrderBO;
import com.hx.domain.framework.obj.dto.BaseDTO;
import com.hx.domain.framework.obj.form.HeaderForm;
import com.hx.domain.framework.service.WebServiceFacade;
import com.hx.domain.framework.utils.MessageCodeUtils;
import com.hx.domain.framework.web.WebServletRoutor;
import com.hx.etx.sync.fiber.FiberCheck;
import com.hx.etx.sync.fiber.HXFiberSync;
import com.hx.lang.commons.utils.BeanUtils;
import com.hx.domain.framework.exception.DomainOperatorException;
import com.hx.domain.framework.request.WebHttpRequest;
import com.hx.domain.framework.response.ResponseEntity;
import com.hx.domain.framework.syscode.DomainApplicationSysCode;
import com.github.f4b6a3.ulid.UlidCreator;
import com.hx.lang.commons.utils.Builder;
import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;
import com.hx.thread.pool.executor.pool.ThreadPoolService;
import com.hx.vertx.boot.core.VertxApplicationContextAware;
import com.hx.vertx.boot.core.entity.FileUploadEntity;
import io.vertx.core.Future;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.FutureTask;

/**
 * DomainContext上下文处理类
 * @author wml
 * @date 2022-06-12
 */
public class DomainContextAware {

	private final static Logger LOGGER = LoggerFactory.getLogger(DomainContextAware.class);

	private DomainContextAware() {
	}

	public static DomainContextAware build() {
		return BeanFactory.NEW_INSTANCE;
	}

	/**
	 * 初始化上下文,初始化上下文时会将报文头、报文体直接放入上下文
	 * 默认情况下 OpenFiber 为false,主流程进入请求分配给服务线程处理
	 *
	 * @param webHttpRequest
	 * @return
	 */
	public DomainContextAware initDomainContext(WebHttpRequest webHttpRequest) {
		DomainContext domainContext = new DomainContext();
		domainContext.setContextId(UlidCreator.getUlid().toString());
		if (!ObjectUtils.isEmpty(webHttpRequest.getFileUploadEntities())) {
			domainContext.putDomainContext(WebHttpBodyConstant.FILE_UPLOAD, webHttpRequest.getFileUploadEntities());
		}
		domainContext.setInputStream(webHttpRequest.getInputStream());
		HeaderForm headerForm = (HeaderForm) webHttpRequest.getRequestHeader();
		String tradeCode = headerForm.getTradeCode();
		String subTradeCode = headerForm.getSubTradeCode();
		String headerCode = headerForm.getHeaderCode();
		Object requestHeaderDTO = BeanConvert.convert(webHttpRequest.getRequestHeader(), StringUtils.append(tradeCode, subTradeCode), headerCode, WebRouteParamsEnums.HEADER_DTO.getName());
		Object requestBodyDTO = BeanConvert.convert(webHttpRequest.getRequestBody(), StringUtils.append(tradeCode, subTradeCode), headerCode, WebRouteParamsEnums.BODY_DTO.getName());
		domainContext.setRequestHeaderDTO((HeaderDTO) requestHeaderDTO);
		if (requestBodyDTO!=null){
			domainContext.setRequestBody((BaseDTO) requestBodyDTO);
		}
		LOGGER.info("初始化领域上下文完成，domainContext: [{}]", domainContext.getContextId());
		// 判断HTTP外部访问进入应用，默认放入当前线程，如果是应用内部模拟API访问，需要判断是否开启协程处理，如果开启协程则将上下文放入协程中
		return this.refreshContext(domainContext);
	}

	/**
	 * 异步处理任务，从service服务层开始处理
	 * 提交并刷新上下文异步处理队列，上下文监听器监听到有DomainContextEventEntity对象时，将会从队列拿去任务进行执行
	 * 防重复刷新提交
	 * @TODO 1、根据领域编号和子编号判断确认，
	 * @TODO 2、如果编号一致，则判断childDomainContext参数是否调整，如果参数不一致放入队列执行，如果参数一致这提示重复提交，忽略本体提交任务
	 * @param asyncCall 是否获取处理结果
	 * @return 返回处理唯一编号, 可以通过这个编号获取返回值
	 */
	public ResponseEntity commitDomainContextEvent(Boolean asyncCall) {
		LOGGER.info("提交领域异步处理任务");
		DomainContext domainContext = this.getDomainContext();
		// 1、上下文非空判断
		if (domainContext == null) {
			throw new DomainOperatorException(DomainApplicationSysCode.A0300000001);
		}
		// 2、上下文传递参数非空验证
		Map<String, Object> childDomainContextMap = domainContext.getDomainContext();
		if (childDomainContextMap == null || childDomainContextMap.size() <= 0) {
			throw new DomainOperatorException(DomainApplicationSysCode.A0300000002);
		}
		DomainContext childDomainContext = setDomainContext(childDomainContextMap);
		ResponseEntity res = null;
		if (this.checkOpenFiber()){
			if (!FiberCheck.checkIsFiber()){
				if (asyncCall){
					res = HXFiberSync.awaitResult(h -> {
						try {
							LOGGER.info("执行提交领域处理任务");
							DomainContextListenerHandlerProcess process = new DomainContextListenerHandlerProcess();
							ResponseEntity result = process.domainContextProcessing(childDomainContext);
							h.handle(Future.succeededFuture(result));
						} catch (Exception e) {
							h.handle(Future.failedFuture(e));
						}
					});
				}else{
					HXFiberSync.fiberExecute(ctx -> {
						DomainContextListenerHandlerProcess process = new DomainContextListenerHandlerProcess();
						process.domainContextProcessing(ctx);
					},childDomainContext);
				}
			}else {
				DomainContextListenerHandlerProcess process = new DomainContextListenerHandlerProcess();
				res = process.domainContextProcessing(childDomainContext);
				// 如果是在同一个fiber中，由于DomainContextListenerHandlerProcess.domainContextProcessing会刷新domainContext，需要在此处还原domainContext
				this.refreshContext(domainContext);
			}
		}else {
			// 线程池执行
			FutureTask futureTask = doThread(childDomainContext,asyncCall);
			res = ResponseEntity.build().addData(futureTask);
		}
		LOGGER.info("提交领域异步处理任务成功");
		domainContext.getDomainContext().clear();
		return res;
	}


	/**
	 * 同步处理任务，从service服务层开始处理
	 * 提交并刷新上下文异步处理队列，上下文监听器监听到有DomainContextEventEntity对象时，将会从队列拿去任务进行执行
	 * 防重复刷新提交
	 * @TODO 1、根据领域编号和子编号判断确认，
	 * @TODO 2、如果编号一致，则判断childDomainContext参数是否调整，如果参数不一致放入队列执行，如果参数一致这提示重复提交，忽略本体提交任务
	 * @return 返回处理唯一编号, 可以通过这个编号获取返回值
	 */
	public ResponseEntity commitDomainContextService() {
		LOGGER.info("--------------提交领域处理任务---------------");
		DomainContext domainContext = this.getDomainContext();
		ResponseEntity responseEntity = null;
		try{
			// 2、上下文传递参数非空验证
			Map<String, Object> childDomainContextMap = domainContext.getDomainContext();
			if (childDomainContextMap == null || childDomainContextMap.size() <= 0) {
				throw new DomainOperatorException(DomainApplicationSysCode.A0300000002);
			}
			DomainContext childDomainContext = setDomainContext(childDomainContextMap);

			String tradeCode = childDomainContext.getRequestHeaderDTO().getTradeCode();
			String subTradeCode = childDomainContext.getRequestHeaderDTO().getSubTradeCode();
			WebServiceFacade serviceFacade = BeanFactoryLocator.getBean(tradeCode);
			if (!Optional.ofNullable(serviceFacade).isPresent()) {
				LOGGER.info("不支持 [{}] 交易",tradeCode);
				ErrorVO errorVO = Builder.of(ErrorVO::new).
						with(ErrorVO::setErrorMessageCode, MessageCodeUtils.format(DomainApplicationSysCode.A0300000005,tradeCode)).build();
				return ResponseEntity.build().addData(errorVO).failure();
			}
			this.refreshContext(childDomainContext);
			if (DomainContextAware.build().getDomainContextEvent().getEnableDomainSupport()
					&& StringUtils.isNotBlank(subTradeCode)){
				responseEntity = (ResponseEntity) DomainServiceRouteSupport.invokDomainService(subTradeCode,serviceFacade);
			}else {
				responseEntity = serviceFacade.doService();
			}
			this.refreshContext(domainContext);
			LOGGER.info("子DomainContextService处理完成");
		}catch (Exception ex){
			LOGGER.error("子DomainContextService处理失败",ex);
			responseEntity = responseEntity == null?ResponseEntity.build().failure():responseEntity.failure();
		}
		return responseEntity;
	}

	/**
	 * 同步调用 api 链路
	 *
	 * @param baseOrderBO 请求参数
	 * @param <B>         参数类型
	 * @return 返回处理之后的ResponseEntity对象
	 * @issue 当前链路上线文将会覆盖原上下文，
	 */
	public <B extends BaseOrderBO> ResponseEntity webServletRoute(B baseOrderBO) {
		return BeanFactoryLocator.getBean(WebServletRoutor.class).route(baseOrderBO);
	}

	/**
	 * 获取当前线程中领域上下文,首先从当前线程获取，获取到上下文之后判断上下文中必传参数是否存在，如果未有则检查是否开启协程，
	 * 如果开启协程则进入协程队列获取。
	 *
	 * @return DomainContext
	 * @see DomainContextHolder
	 * DOMAIN_CONTEXT 和 FIBER_DOMAIN_CONTEXT 容器中默认赋值空DomainContext对象
	 */
	public DomainContext getDomainContext() {
		return FiberCheck.checkIsFiber()?this.getFiberDomainContext():DomainContextHolder.getDomainContext();
	}

	/**
	 * 获取当前协程中领域上下文
	 *
	 * @return DomainContext
	 */
	public DomainContext getFiberDomainContext() {
		return DomainContextHolder.getFiberDomainContext();
	}

	/**
	 * 获取上下文
	 * 1、http请求进入的，在路由入口{@sine com.hx.domain.framework.web.WebServletRoutor.doService}生成上下文
	 * 2、非http入口，则生成当前线程上下文，使用完之后必须销毁
	 *
	 * @return DomainContext
	 */
	public DomainContext getDomainContext(WebHttpRequest webHttpRequest) {
		DomainContext domainContext = DomainContextHolder.getDomainContext();
		if (domainContext == null) {
			domainContext = initDomainContext(webHttpRequest).getDomainContext();
		}
		return domainContext;
	}

	/**
	 * 刷新当前线程上下文
	 */
	public DomainContextAware refreshContext(DomainContext context) {
		if (FiberCheck.checkIsFiber()) {
			return this.refreshFiberContext(context);
		}
		DomainContextHolder.setDomainContext(context);
		LOGGER.info("刷新领域上下文完成，domainContext: [{}]", context.getContextId());
		// 检查是否有需要保存的数据
		return this;
	}

	/**
	 * 刷新当前协程上下文
	 */
	public DomainContextAware refreshFiberContext(DomainContext context) {
		DomainContextHolder.setFiberDomainContext(context);
		LOGGER.info("刷新领域上下文完成，domainContext: [{}]", context.getContextId());
		// 检查是否有需要保存的数据
		return this;
	}

	/**
	 * 销毁当前线程上下文
	 */
	public DomainContextAware destroyContext() {
		if (FiberCheck.checkIsFiber()) {
			DomainContext context = getDomainContext();
			DomainContextHolder.removeDomainContext();
			LOGGER.info("销毁领域上下文完成，domainContext: [{}]", context.getContextId());
		}else {
			return this.destroyFiberContext();
		}
		return this;
	}

	/**
	 * 销毁当前协程上下文
	 */
	public DomainContextAware destroyFiberContext() {
		DomainContext context = getFiberDomainContext();
		DomainContextHolder.removeFiberDomainContext();
		LOGGER.info("销毁领域上下文完成，domainContext: [{}]", context.getContextId());
		return this;
	}

	/**
	 * 获取异步线程处理结果
	 *
	 * @param eventProccessId
	 * @return
	 */
	public Object doCall(String eventProccessId) {
		// 检查任务是否处理完
		if (!IsDone(eventProccessId)) {
			LOGGER.warn("任务处理未完成，请等待处理完成再读取");
			return null;
		}
		Object result = this.getDomainContextEvent().peekProcessDone(eventProccessId);
		// 清除已获取的结果
		this.getDomainContextEvent().removeProcessDone(eventProccessId);
		return result;
	}

	/**
	 * 检查任务是否已经处理完成
	 *
	 * @param eventProccessId
	 * @return
	 */
	public boolean IsDone(String eventProccessId) {
		return this.getDomainContextEvent().isDone(eventProccessId);
	}

	public DomainContextEvent getDomainContextEvent() {
		return BeanFactoryLocator.getBean(DomainContextEvent.class);
	}

	/**
	 * 是否开启应用内部文件自动同步
	 *
	 * @return
	 */
	public boolean isAutoSyncFile() {
		DomainEventListenerHandlerProperties properties = VertxApplicationContextAware
				.getProperties(DomainEventListenerHandlerProperties.class);
		return properties.getAutoSync();
	}

	/**
	 * 检查是否开启fiber
	 * @return
	 */
	public boolean checkOpenFiber(){
		DomainEventListenerHandlerProperties properties = VertxApplicationContextAware.getProperties(DomainEventListenerHandlerProperties.class);
		Boolean openFiber = properties.getOpenFiber();
		return !ObjectUtils.isEmpty(openFiber) || openFiber;
	}

	/**
	 * 获取客户端上传的文件信息实体
	 *
	 * @return
	 */
	public List<FileUploadEntity> fileUploadEntity() {
		return (List<FileUploadEntity>) DomainContextAware.build().getDomainContext()
				.getDomainContext().get(WebHttpBodyConstant.FILE_UPLOAD_ENTITIES);
	}

	private final static class BeanFactory {
		public final static DomainContextAware NEW_INSTANCE = new DomainContextAware();
	}

	private DomainContext setDomainContext(Map<String, Object> childDomainContextMap) {
		// 需要根据交易码获取header和body
		Object requestHeaderDTO = childDomainContextMap.get(WebHttpBodyConstant.ASYN_HEADER_DTO);
		if (ObjectUtils.isEmpty(requestHeaderDTO)) {
			throw new RuntimeException("");
		}

		Map<String, Object> requestHeaderDTOMap = requestHeaderDTO instanceof Map ?
				(Map<String, Object>) requestHeaderDTO : BeanMap.create(requestHeaderDTO);

		String tradeCode = StringUtils.valueOf(requestHeaderDTOMap.get(WebHttpBodyConstant.TRADE_CODE));
		String subTradeCode = StringUtils.valueOf(requestHeaderDTOMap.get(WebHttpBodyConstant.SUB_TRADE_CODE));
		String headerCode = StringUtils.valueOf(requestHeaderDTOMap.get(WebHttpBodyConstant.HEADER_CODE));
		Object requestHeader = BeanConvert.convert(childDomainContextMap.get(WebHttpBodyConstant.ASYN_HEADER_DTO), StringUtils.append(tradeCode,
				subTradeCode), headerCode, WebRouteParamsEnums.HEADER_DTO.getName());
		Object requestBody = BeanConvert.convert(childDomainContextMap.get(WebHttpBodyConstant.ASYN_BODY_DTO), StringUtils.append(tradeCode,
				subTradeCode), headerCode, WebRouteParamsEnums.BODY_DTO.getName());
		DomainContext childDomainContext = BeanUtils.copyProperties(childDomainContextMap, DomainContext.class);
		// 3、报文头非空验证
		if (childDomainContext.getRequestHeaderDTO() == null) {
			throw new DomainOperatorException(DomainApplicationSysCode.A0300000003);
		}
		childDomainContext.setRequestHeaderDTO((HeaderDTO) requestHeader);
		childDomainContext.setRequestBody((BaseDTO) requestBody);
		return childDomainContext;
	}

	/**
	 *
	 * @param asyncCall 是否有返回值
	 * @return
	 */
	private FutureTask doThread(DomainContext domainContext,boolean asyncCall) {
		DomainContextThreadPoolEvent domainContextThreadPoolEvent = new DomainContextThreadPoolEvent(domainContext);
		domainContextThreadPoolEvent.setThreadGroupName(this.getDomainContextEvent().getThreadGroupName());
		if (!asyncCall) {
			ThreadPoolService.build().execute(domainContextThreadPoolEvent);
			return null;
		} else {
			// 设置线程唯一编号，便于获取异步处理结果
			return ThreadPoolService.build().submit(domainContextThreadPoolEvent);
		}
	}
}
