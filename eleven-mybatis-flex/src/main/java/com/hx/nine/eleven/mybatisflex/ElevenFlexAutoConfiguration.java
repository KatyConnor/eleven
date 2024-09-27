package com.hx.nine.eleven.mybatisflex;

import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.annotations.ConditionalOnBean;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.jdbc.AbstractRoutingDataSource;

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
