package hx.nine.eleven.jdbc;

import com.esotericsoftware.reflectasm.ConstructorAccess;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import org.reflections.Reflections;

import java.util.Set;

/**
 * 扫描所有实现了 ElevenDataSource 接口的实现类，注入到 eleven 容器中
 * @auth wml
 * @date 2024/9/26
 */
public class ElevenDataSourceSubLoadFactory implements ApplicationAnnotationFactory {

	@Override
	public void loadAnnotations(Reflections reflections) throws Throwable {
		Set<Class<? extends ElevenDataSource>> mapperSet = reflections.getSubTypesOf(ElevenDataSource.class);
		if (mapperSet == null || mapperSet.size() > 1){
			ElevenLoggerFactory.build(this).warn(mapperSet == null?
					"项目没有查找到 com.hx.nine.eleven.jdbc.ElevenDataSource实现类":
					"项目没有查找到多个 com.hx.nine.eleven.jdbc.ElevenDataSource 实现类");
			return;
		}
		Class<? extends ElevenDataSource> classes = mapperSet.stream().findFirst().get();
		ElevenDataSource elevenDataSource = ConstructorAccess.get(classes).newInstance();
		DefaultElevenApplicationContext.build().addBean(ElevenDataSource.class.getName(),elevenDataSource);
	}
}
