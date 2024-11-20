package hx.nine.eleven.thread.pool.executor;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wml
 * 2021-12-02
 */
@Deprecated
public class HXApplicationContext {

    private static volatile Map<String,Object> beanContainer = new ConcurrentHashMap<>();

    public void add(String name,Object value){
        beanContainer.put(name, value);
    }

    public Object get(String name){
        return beanContainer.get(name);
    }

    private HXApplicationContext(){

    }

    public static HXApplicationContext build(){
        return Single.NEWSTANCE;
    }

    private static final class Single{
        private static final HXApplicationContext NEWSTANCE = new HXApplicationContext();
    }
}
