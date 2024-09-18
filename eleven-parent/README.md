#  hx-springboot-parent

#### 介绍
springboot 项目中统一封装父 pom，包含基本必须jar依赖，项目打包方式，maven私服地址等

#### 软件架构
	整个项目自有一个pom文件，hx-boot-dependencies版本控制，封装maven 打包的plugin


#### 安装教程
	
	无需安装， pom继承
	

#### 使用说明

	项目中直接parent继承既可
	
	<parent>
        <groupId>com.hx.framework.spring.boot</groupId>
        <artifactId>hx-springboot-parent</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </parent>

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


#### 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md