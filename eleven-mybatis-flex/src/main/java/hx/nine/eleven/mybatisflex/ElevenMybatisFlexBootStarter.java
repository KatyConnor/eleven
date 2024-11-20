package hx.nine.eleven.mybatisflex;

import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.mybatisflex.mapper.ElevenBaseMapper;
import hx.nine.eleven.mybatisflex.mapper.ElevenFlexBaseMapper;
import hx.nine.eleven.mybatisflex.tx.ElevenFlexTransactionFactory;

import java.util.Set;

/**
 * @auth wml
 * @date 2024/9/25
 */
public class ElevenMybatisFlexBootStarter {

	public static void start(Set<Class<? extends ElevenBaseMapper>> type){
		ElevenLoggerFactory.build(ElevenMybatisFlexBootStarter.class).info("开始初始化eleven mybatis flex");
		ElevenMybatisFlexBootstrap.getInstance()
				.initDataSource()
				.addMappers(type)
				.setTransactionFactory(new ElevenFlexTransactionFactory())
				.addMapper(ElevenFlexBaseMapper.class)
				.start();
	}
}
