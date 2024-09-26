package com.hx.nine.eleven.mybatisflex;

import java.util.Set;

/**
 * @auth wml
 * @date 2024/9/25
 */
public class ElevenMybatisFlexBootStarter {

	public static void start(Set<Class<? extends ElevenBaseMapper>> type){
		ElevenMybatisFlexBootstrap.getInstance()
				.initDataSource()
				.addMappers(type)
				.addMapper(ElevenFlexMapper.class)
				.start();
	}
}
