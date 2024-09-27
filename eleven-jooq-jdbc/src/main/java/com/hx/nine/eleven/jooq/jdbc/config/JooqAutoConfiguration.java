package com.hx.nine.eleven.jooq.jdbc.config;

/**
 * @auth wml
 * @date 2024/9/27
 */

import com.hx.nine.eleven.core.annotations.Component;
import com.hx.nine.eleven.core.annotations.ConditionalOnBean;
import com.hx.nine.eleven.core.core.ElevenApplicationContextAware;
import com.hx.nine.eleven.core.core.context.DefaultElevenApplicationContext;
import com.hx.nine.eleven.core.utils.ElevenLoggerFactory;
import com.hx.nine.eleven.jdbc.AbstractRoutingDataSource;
import com.hx.nine.eleven.jooq.jdbc.HXDSL;
import com.hx.nine.eleven.jooq.jdbc.tx.JooqTransactionManager;

@Component(init = "initJooq")
@ConditionalOnBean(bean = "dataSource")
public class JooqAutoConfiguration {

	public void initJooq() {
		// jooq框架初始化事务管理
		ElevenLoggerFactory.build(this).info("开始初始化jooq事务管理器");
		AbstractRoutingDataSource dataSource = ElevenApplicationContextAware.getBean("dataSource");
		JooqTransactionManager jooqTransactionManager = new JooqTransactionManager(dataSource);
		DefaultElevenApplicationContext.build().addBean(jooqTransactionManager);
		//初始化jooq
		ElevenLoggerFactory.build(this).info("--------------------初始化jooq与数据库的初始链接--------------------");
		HXDSL.using(dataSource).selectCount();
	}
}
