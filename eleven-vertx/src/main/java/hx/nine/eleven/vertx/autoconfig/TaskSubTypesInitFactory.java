package hx.nine.eleven.vertx.autoconfig;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.factory.ApplicationSubTypesInitFactory;
import hx.nine.eleven.core.task.ElevenScheduledTask;
import hx.nine.eleven.core.task.ElevenThreadTask;
import org.reflections.Reflections;

import java.util.HashSet;
import java.util.Set;

/**
 * 初始化定时任务执行子类
 * @auth wml
 * @date 2024/11/26
 */
public class TaskSubTypesInitFactory implements ApplicationSubTypesInitFactory {

	@Override
	public void loadSubTypesObject(Reflections reflections) throws Throwable {
		Set<Class<? extends ElevenScheduledTask>> elevenScheduledTask = reflections.getSubTypesOf(ElevenScheduledTask.class);
		Set<Class<? extends ElevenThreadTask>> elevenThreadTask = reflections.getSubTypesOf(ElevenThreadTask.class);
		if (elevenScheduledTask != null && elevenScheduledTask.size() > 0){
			Set<ElevenScheduledTask> subTypesOfBeans = new HashSet<>(elevenScheduledTask.size());
			elevenScheduledTask.forEach(t->{
				ConstructorAccess constructorAccess = ConstructorAccess.get(t);
				ElevenScheduledTask obj = (ElevenScheduledTask)constructorAccess.newInstance();
				subTypesOfBeans.add(obj);

			});
			DefaultElevenApplicationContext.build().addSubTypesOfBean(ElevenScheduledTask.class,subTypesOfBeans);
		}

		if (elevenThreadTask != null && elevenThreadTask.size() > 0){
			Set<ElevenThreadTask> subTypesOfBeans = new HashSet<>(elevenThreadTask.size());
			elevenThreadTask.forEach(t->{
				ConstructorAccess constructorAccess = ConstructorAccess.get(t);
				ElevenThreadTask obj = (ElevenThreadTask)constructorAccess.newInstance();
				subTypesOfBeans.add(obj);

			});
			DefaultElevenApplicationContext.build().addSubTypesOfBean(ElevenThreadTask.class,subTypesOfBeans);
		}
	}
}
