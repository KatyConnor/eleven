package com.hx.thread.pool.executor.pool;

import com.hx.thread.pool.executor.factory.ThreadPoolFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author mingliang
 * @Date 2018-01-17 17:31
 */
//@Component
public class ThreadPoolExcutorService {


    private static Map<String,ThreadPoolManageEntity> entityMap;

    static {
        entityMap = new LinkedHashMap<>();
        entityMap.put("test",new ThreadPoolManageEntity(50,100,60, TimeUnit.SECONDS));
        entityMap.put("test2",new ThreadPoolManageEntity(50,100,60, TimeUnit.SECONDS));
    }

    public void initThreadPool(){
        ThreadPoolFactory.addThreadPool(entityMap.get("test"));
        ThreadPoolFactory.addThreadPool(entityMap.get("test2"));
    }
}
