#  hx-springboot-dependencies

#### 介绍
maven 依赖版本控制，统一配置依赖包版本，子类继承

#### 软件架构
软件架构说明


#### 安装教程

    无需安装， pom文件配置 hx-springboot-dependencies 

#### 使用说明

	 hx-springboot-parent 中已引入 hx-springboot-dependencies，可以直接继承 hx-springboot-parent

	<parent>
        <groupId>com.hx.framework.spring.boot</groupId>
        <artifactId>hx-springboot-parent</artifactId>
        <version>1.1.0-SNAPSHOT</version>
    </parent>

	或者在dependencyManagement 引入，如下：
	
	<!-- 版本管理 -->
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.hx.springboot.dependencies</groupId>
                <artifactId>hx-springboot-dependencies</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>


	配置之后项目引入maven依赖包如果已经在hx-boot-dependencies中统一配置，那该项目中不用再配置<version>
	例如：

	 <dependency>
        <groupId>javax.persistence</groupId>
        <artifactId>persistence-api</artifactId>
    </dependency>
	

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


#### 特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md