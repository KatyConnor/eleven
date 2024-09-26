package com.hx.nine.eleven.mybatisflex;

import com.hx.nine.eleven.core.factory.ApplicationAnnotationFactory;
import com.hx.nine.eleven.core.factory.ApplicationSubTypesInitFactory;
import com.hx.nine.eleven.core.utils.HXLogger;
import org.reflections.Reflections;

import java.util.Set;

/**
 * 扫描所有实现了ElevenBaseMapper接口的实现类，注入到mybatis中
 * @auth wml
 * @date 2024/9/25
 */
public class MapperScanFactory implements ApplicationSubTypesInitFactory {

	@Override
	public void loadSubTypesObject(Reflections reflections) throws Throwable {
		Set<Class<? extends ElevenBaseMapper>> mapperSet = reflections.getSubTypesOf(ElevenBaseMapper.class);
		if (mapperSet == null){
			HXLogger.build(MapperScanFactory.class).warn("项目没有添加 MapperScans ，无法自动注入 mapper");
			return;
		}
		ElevenMybatisFlexBootStarter.start(mapperSet);
	}
}
