package hx.nine.eleven.jobtask;

import hx.nine.eleven.jobtask.entity.ScheduleTaskEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auth wml
 * @date 2024/11/26
 */
public class ScheduleQueue {

	private final static Logger LOGGER = LoggerFactory.getLogger(ScheduleQueue.class);

	//默认队列处理容量
	private int maxWorkQueueSize = 10000;
	private AtomicInteger workQueueSize = new AtomicInteger();

	// 待消费任务队列，需要设置队列容量大小，否则在高并发访问量交大时会耗尽内存
	private final static ConcurrentLinkedQueue<ScheduleTaskEntity> bucketQueue = new ConcurrentLinkedQueue();

	public boolean push(ScheduleTaskEntity domain){
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
	public  ScheduleTaskEntity peek(){
		//队列中获取消费数据
		ScheduleTaskEntity entity =  bucketQueue.peek();
		//将任务放入处理中队列
//		this.addProccess(entity);
		// 删除队列中已消费的domain
		this.poll(entity);
		return  entity;
	}

	/**
	 * 检查队列是否有需要处理的数据
	 * @return
	 */
	public boolean checkbucketQueue(){
		return bucketQueue.peek() == null?false:true;
	}

	/**
	 * 删除队列中的数据
	 * @param entity
	 * @return
	 */
	public  boolean poll(ScheduleTaskEntity entity){
		return bucketQueue.remove(entity);
	}
}
