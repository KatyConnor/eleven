package com.hx.nine.eleven.mybatis.flex;

import java.util.Set;

/**
 * 全局配置
 * @auth wml
 * @date 2024/9/23
 */
public class GlobalConfig {
	/**
	 * 数据库连接地址
	 */
	private String jdbcUrl;
	/**
	 *  数据库连接用户名
	 */
	private volatile String username;
	/**
	 *  数据库链接密码
	 */
	private volatile String password;
	/**
	 *  设置生成代码的根路径
	 */
	private String basePackage = "com.hx.nine.eleven.mybatis.flex";
	/**
	 *  设置生数据库表前缀
	 */
	private String tablePrefix;
	/**
	 *  设置需要逆向生成的那些表
	 */
	private Set<String> generateTables;
	/**
	 *  设置忽略不需要逆向生成的那些表
	 */
	private Set<String> unGenerateTables;
	/**
	 *  设置表中那些列忽略掉，不生成到逆向实体代码中
	 */
	private Set<String> ignoreColumns;
	/**
	 *  数据库（只设置这个参数时，默认会生成数据库所有表）
	 */
	private String generateSchema;

	/***
	 *  是否生成实体类
	 */
	private boolean entityGenerateEnable;
	/***
	 *  是否生成 Mapper
	 */
	private boolean mapperGenerateEnable;
	/***
	 *  是否生成 service服务类
	 */
	private boolean serviceGenerateEnable;
	/***
	 *  是否生成 service服务类实现
	 */
	private boolean serviceImplGenerateEnable;
	/***
	 *  是否生成 controller 控制类
	 */
	private boolean controllerGenerateEnable;
	/***
	 *  是否生成表定义辅助类
	 */
	private boolean tableDefGenerateEnable;
	/***
	 *  是否生成 mapper Xml 文件
	 */
	private boolean mapperXmlGenerateEnable;
	/***
	 *  是否生成 package-info.java 文件
	 */
	private boolean packageInfoGenerateEnable;

	/** --------------------------------entity实体class配置-------------------------------------- */
	/**
	 *  设置实体class添加的前缀
	 */
	private String classPrefix = "";
	/**
	 *  设置实体class添加的后缀
	 */
	private String classSuffix = "";
	/**
	 *  设置实体class继承父类
	 */
	private Class<?> superClass;
	/**
	 *  是否启用 Lombok
	 */
	private boolean withLombok;
	/**
	 *  是否启用 Swagger
	 */
	private boolean withSwagger;
	/**
	 *  基础实体类后缀
	 */
	private String withBaseClassSuffix = "Base";
	/**
	 *  entity基础package路径
	 */
	private String withBasePackage;
	/**
	 *  设置JDK版本了
	 */
	private int jdkVersion;
	/**
	 *  是否启用基础类继承/实现
	 */
	private boolean withBaseClassEnable = false;

}
