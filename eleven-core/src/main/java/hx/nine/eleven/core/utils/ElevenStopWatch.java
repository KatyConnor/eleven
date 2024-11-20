package hx.nine.eleven.core.utils;

import com.google.common.base.Stopwatch;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * 计算执行时间
 * @auth wml
 * @date 2024/11/6
 */
public class ElevenStopWatch {

	private static final ConcurrentMap<Thread,Stopwatch> stopWatch = new ConcurrentHashMap();

	public static void start(){
		Stopwatch stopwatch = Stopwatch.createStarted();
		stopWatch.put(Thread.currentThread(),stopwatch);
	}

	public static long getTime(TimeUnit timeUnit){
		Stopwatch stopwatch = stopWatch.get(Thread.currentThread());
		return stopwatch.stop().elapsed(timeUnit);
	}
}
