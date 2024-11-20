package com.hx.nine.eleven.jobtask.cron;

import com.hx.lang.commons.utils.ObjectUtils;
import com.hx.lang.commons.utils.StringUtils;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 定时任务 cron 缓存，定时刷新
 * @author wml
 * @Discription
 * @Date 2023-06-06
 */
public class ScheduleCron {

    private final static String NEXT_TIME = "NEXT_TIME";
    private final static String NEXT_INTERVAL_TIME = "NEXT_INTERVAL_TIME";
    private final static String CURRENT_DATE = "CURRENT_DATE";
    private volatile ConcurrentHashMap<String,Object> scheduleCronMap = new ConcurrentHashMap<>();

    /**
     * 添加一个新的cron对象，同时添加下一次执行时间
     * @param key　　　　主键
     * @param cronKit　　cron对象
     * @return
     */
    public ScheduleCron addCron(String key, CronKit cronKit){
        scheduleCronMap.put(key,cronKit);
        scheduleCronMap.put(NEXT_TIME+key,cronKit.next());
        return this;
    }

    public boolean notHave(String key){
        CronKit cronKit = (CronKit)scheduleCronMap.get(key);
        return ObjectUtils.isEmpty(cronKit);
    }

    /**
     * １　首先判断当前任务是否满足可执行条件，即当前时间大于等于本次执行时间
     * @param key　　　　　　任务主键key
     * @param date         当前时间
     * @param preExeDate 　上一次执行时间
     * @return
     */
    public boolean haveNext(String key, Date date, Date preExeDate){
        CronKit cronKit = (CronKit)scheduleCronMap.get(key);
        if (ObjectUtils.isNotEmpty(cronKit)){
            Date nextTime = (Date)scheduleCronMap.get(NEXT_TIME+key);
            boolean next = nextTime.compareTo(date) != 1;
            int timeDiff = calculateCronTimeInterval(cronKit.getCron(),key);
            long nowDiffTime = -1;
            if (preExeDate != null){
                long timeInterval = date.getTime() - preExeDate.getTime();
                nowDiffTime = (long) timeInterval / 1000;
            }
            if (next && (nowDiffTime == -1 || nowDiffTime >= timeDiff)){
                Date nextExeTime = cronKit.next();
                scheduleCronMap.put(NEXT_TIME+key,nextExeTime);
                scheduleCronMap.put(CURRENT_DATE+key,date);
                return true;
            }
        }
        return false;
    }

    public CronKit getCronKit(String key){
        return (CronKit) scheduleCronMap.get(key);
    }

    public static ScheduleCron build(){
        return Single.INSTANCE;
    }

    private int calculateCronTimeInterval(String cron,String key){
        if (StringUtils.isEmpty(cron)){
            return -1;
        }
        Object interTime = scheduleCronMap.get(NEXT_INTERVAL_TIME+key);
        if (interTime != null){
            return Integer.valueOf(interTime.toString());
        }
        CronKit cronKit = new CronKit(cron);
        Date date1 = cronKit.next();
        Date date2 = cronKit.next();
        long time = date2.getTime() - date1.getTime();
        int res = (int) time / 1000;
        scheduleCronMap.put(NEXT_INTERVAL_TIME+key,res);
        return res;
    }

    private static final class Single{
        private static final ScheduleCron INSTANCE = new ScheduleCron();
    }




}
