package hx.nine.eleven.mybatisflex;

import hx.nine.eleven.core.annotations.Component;
import hx.nine.eleven.core.annotations.ConditionalOnBean;
import hx.nine.eleven.core.core.ElevenApplicationContextAware;
import hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import hx.nine.eleven.core.utils.ElevenLoggerFactory;
import hx.nine.eleven.jdbc.AbstractRoutingDataSource;

/**
 * @auth wml
 * @date 2024/9/27
 */
//@Component(init = "initMybatisFlex")
//@ConditionalOnBean(bean = "dataSource")
public class ElevenFlexAutoConfiguration {

	public void initMybatisFlex() {
		// jooq框架初始化事务管理
		ElevenLoggerFactory.build(this).info("开始初始化eleven mybatis flex");
		AbstractRoutingDataSource dataSource = ElevenApplicationContextAware.getBean("dataSource");
		//初始化jooq
		ElevenLoggerFactory.build(this).info("--------------------初始化eleven mybatis flex与数据库的初始链接--------------------");
	}
}
