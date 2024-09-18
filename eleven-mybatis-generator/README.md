##### cims-mybatis-generator
	本项目用于生成entity及mapper文件，代码参考自ncbs-generator:1.0.7。
###### current version： 1.0.0-SNAPSHOT
###### last update: 2018-10-24

features:
​	1、新增生成selectList。
​	2、查询新增机构、用户权限条件。

example:

```xml
<select id="selectList" parameterType="DataPermission" resultMap="BaseResultMap">
    select
    <include refid="Base_Column_List" />
    from DATA_PERMISSION
    <where>
      <if test="conTableName != null">        and CON_TABLE_NAME = #{conTableName}      </if>
      <if test="managerModify != null">        and MANAGER_MODIFY = #{managerModify}      </if>
      <if test="managerDelete != null">        and MANAGER_DELETE = #{managerDelete}      </if>
      <if test="creatorModify != null">        and CREATOR_MODIFY = #{creatorModify}      </if>
      <if test="creatorDelete != null">        and CREATOR_DELETE = #{creatorDelete}      </if>
      <if test="validState != null">        and VALID_STATE = #{validState}      </if>
      <if test="createUserNo != null">        and CREATE_USER_NO = #{createUserNo}      </if>
      <if test="manageUserNo != null">        and MANAGE_USER_NO = #{manageUserNo}      </if>
      <if test="updateUserNo != null">        and UPDATE_USER_NO = #{updateUserNo}      </if>
      <if test="myself == true">
        and ( CREATE_USER_NO = #{opUserNo} or MANAGE_USER_NO = {opUserNo})
      </if>
      <if test="myOrg == true">
        and ( CREATE_ORG_NO = #{opOrgNo} or MANAGE_ORG_NO = #{opOrgNo})
      </if>
      <if test="myOrgAndBranch == true">
        and ( CREATE_ORG_NO in (select ORG_NO from TC_RGT_ORG where LOCATION like contact('%', #{opOgrNo}, '%')) OR MANAGE_ORG_NO in (select ORG_NO from TC_RGT_ORG where LOCATION like contact('%', #{opOrgNo}, '%')) 
      </if>
    </where>
    <if test="orderByClause != null">
      order by ${orderByClause}
    </if>
</select>
```

使用方法：

1、本地jar包cqrcb-orm-0.0.1-SNAPSHOT.jar

2、pom.xml配置插件，配置信息如下：

```xml
<plugin>
    <groupId>org.mybatis.generator</groupId>
    <artifactId>mybatis-generator-maven-plugin</artifactId>
    <version>1.3.5</version>
    <dependencies>
        <dependency>
            <groupId>com.oracle</groupId>
            <artifactId>ojdbc6</artifactId>
            <version>11.2.0.3</version>
        </dependency>
        <dependency>
            <groupId>com.cqrcb.cims</groupId>
            <artifactId>cims-mybatis-generator</artifactId>
            <version>1.0.0-SNAPSHOT</version>
        </dependency>
        <dependency>
            <groupId>com.cqrcb.orm</groupId>
            <artifactId>cqrcb-orm</artifactId>
            <version>0.0.1</version>
            <scope>system</scope>
            <systemPath>${basedir}/lib/cqrcb-orm-0.0.1-SNAPSHOT.jar</systemPath>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis-spring</artifactId>
            <version>1.2.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.1.6.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>4.1.6.RELEASE</version>
        </dependency>
    </dependencies>
    <configuration>
		<!-- mybatis用于生成代码的配置文件 -->
        <configurationFile>
            src/test/resources/conf/NCBSGeneratorConfig_loan.xml
        </configurationFile>
        <verbose>true</verbose>
        <overwrite>true</overwrite>
    </configuration>
</plugin>
```

3、配置GeneratorConfig.xml，示例：
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE generatorConfiguration PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN" "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd" >
<generatorConfiguration>
	<context id="para" targetRuntime="com.cqrcb.cims.tools.mybatis.generator.impl.CIMSIntrospectedTableMyBatis3SimpleImpl">
		<property name="javaFileEncoding" value="UTF-8" />
		<commentGenerator type="com.cqrcb.cims.tools.mybatis.generator.generators.CIMSCommentGenerator" />
		<jdbcConnection driverClass="oracle.jdbc.driver.OracleDriver"
			connectionURL="jdbc:oracle:thin:@10.9.166.38:1521:devutf12c" 
			userId="cims_wholeflow" password="Cqrcb256">
			<property name="remarksReporting" value="true" />
		</jdbcConnection>
		<javaTypeResolver type="com.cqrcb.cims.tools.mybatis.generator.types.CIMSJavaTypeResolver"/>
		<javaModelGenerator targetPackage="com.cqrcb.cims.common.db.entity" targetProject="src/main/java">
			<property name="constructorBased" value="true" />
		 	<property name="rootClass" value="com.cqrcb.cims.common.db.entity.BaseControlledEntity"/>
		</javaModelGenerator>
		<sqlMapGenerator targetPackage="com.cqrcb.cims.common.db.mapper"
			targetProject="src/main/java" />
		<table tableName="DATA_PERMISSION" schema="CIMS_WHOLEFLOW"><property name="ignoreQualifiersAtRuntime" value="true"/></table>
		</context>
</generatorConfiguration>
```

4、运行maven命令：mybatis-generator:generate -X