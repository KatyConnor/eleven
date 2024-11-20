package hx.nine.eleven.domain.context;

import hx.nine.eleven.domain.properties.DomainEventListenerHandlerProperties;
import hx.nine.eleven.core.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 上下文中待处理
 * @author wml
 * @date 2022-11-28
 */
@Component(init = "init")
public class DomainContextEvent {

    private final static Logger LOGGER = LoggerFactory.getLogger(DomainContextEvent.class);

    //默认队列处理容量
    private int maxWorkQueueSize = 10000;
    private AtomicInteger workQueueSize = new AtomicInteger();
    private String threadGroupName;
    private Boolean enableDomainSupport;

    // 待消费任务队列，需要设置队列容量大小，否则在高并发访问量交大时会耗尽内存
    private final static ConcurrentLinkedQueue<DomainContextEventEntity> bucketQueue = new ConcurrentLinkedQueue();
    //消费中有返回结果，只要有返回结果集的任务都会放入此队列
//    private final static ConcurrentLinkedQueue<FutureTaskEntity> processRsultQueue= new ConcurrentLinkedQueue();
    //任务消费处理完成（成功、失败）
    private final static ConcurrentHashMap<String, Object> processDone = new ConcurrentHashMap<>();
    //任务处于消费处理中的
    private final static ConcurrentLinkedQueue<DomainContextEventEntity> processing = new ConcurrentLinkedQueue();

    @Resource
    private DomainEventListenerHandlerProperties handlerProperties;

    public void init() {
        LOGGER.info("初始化DomainContextEvent上下文处理事件对象");
        if (this.handlerProperties.getMaxWorkQueueSize() > 0){
            this.maxWorkQueueSize = this.handlerProperties.getMaxWorkQueueSize();
        }
        this.threadGroupName = this.handlerProperties.getThreadGroupName();
        this.enableDomainSupport = this.handlerProperties.getEnableDomainSupport();
    }

    /**
     * 添加一个处理对象到待处理队列池
     * @param domain
     * @return
     */
    @Deprecated
    public boolean push(DomainContextEventEntity domain){
        if (Optional.ofNullable(domain).isPresent() && workQueueSize.get() <= maxWorkQueueSize){
            workQueueSize.getAndIncrement();
            return bucketQueue.add(domain);
        }
        LOGGER.warn(domain == null?"提交处理数据为null":"当前处理队列已达到[{}]上限，请稍后提交处理。",this.maxWorkQueueSize);
        return false;
    }

    /**
     * 消费处理队列中的数据
     * @return
     */
    @Deprecated
    public  DomainContextEventEntity peek(){
        //队列中获取消费数据
        DomainContextEventEntity entity =  bucketQueue.peek();
        //将任务放入处理中队列
        this.addProccess(entity);
        // 删除队列中已消费的domain
        this.poll(entity);
        return  entity;
    }

    /**
     * 检查队列是否有需要处理的数据
     * @return
     */
    @Deprecated
    public boolean checkbucketQueue(){
        return bucketQueue.peek() == null?false:true;
    }

    /**
     * 删除队列中的数据
     * @param domain
     * @return
     */
    @Deprecated
    public  boolean poll(DomainContextEventEntity domain){
       return bucketQueue.remove(domain);
    }

    /**
     * 添加到处理中队列
     * @param entity
     */
    @Deprecated
    public boolean addProccess(DomainContextEventEntity entity){
        return processing.add(entity);
    }

    /**
     * 获取处理中队列数据
     * @param entity
     */
    @Deprecated
    public DomainContextEventEntity getProccess(DomainContextEventEntity entity){
        return processing.stream().filter(e->e.getEventProccessId().equals(entity.getEventProccessId())).findFirst().orElse(null);
    }

    /**
     * 获取队列大小
     * @return
     */
    @Deprecated
    public int proccessSize(){
        return processing.size();
    }
    /**
     * 删除处理中队列已完成的数据
     * @param entity
     */
    @Deprecated
    public boolean removeProccess(DomainContextEventEntity entity){
       return processing.remove(entity);
    }

    /**
     * 添加处理完成的任务结果到结果集合中存储
     * @param eventProccessId
     * @param obj
     */
    public void addProcessDone(String eventProccessId,Object obj){
        processDone.put(eventProccessId,obj);
    }

    /**
     * 删除处理完成的结果
     * @param eventProccessId
     */
    public boolean removeProcessDone(String eventProccessId){
        return processDone.remove(eventProccessId) !=null;
    }

    /**
     * 获取处理完成的结果
     * @param eventProccessId
     * @return
     */
    public Object peekProcessDone(String eventProccessId){
        return processDone.get(eventProccessId);
    }

    /**
     * 检查任务是否已经处理完成
     * @param eventProccessId
     * @return
     */
    public boolean isDone(String eventProccessId){
        return processDone.containsKey(eventProccessId);
    }

    public String getThreadGroupName() {
        return threadGroupName;
    }

    public void setThreadGroupName(String threadGroupName) {
        this.threadGroupName = threadGroupName;
    }

    public Boolean getEnableDomainSupport() {
        return enableDomainSupport;
    }

    public void setEnableDomainSupport(Boolean enableDomainSupport) {
        this.enableDomainSupport = enableDomainSupport;
    }

    /**
     * 检查是否需要扩容
     */
//    private void checkDomain(){
//        bucketQueue.poll();
//        int length = bucket.length;
//        if (this.index >= length && this.eventStoreDomain[length] != null){
//            this.eventStoreDomain = Arrays.copyOf(this.eventStoreDomain,this.eventStoreDomain.length << 1);
//        }
//    }
}
