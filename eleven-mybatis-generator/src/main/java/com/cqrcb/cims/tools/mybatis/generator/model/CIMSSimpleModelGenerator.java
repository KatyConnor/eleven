package com.cqrcb.cims.tools.mybatis.generator.model;

import com.cqrcb.orm.physicaldb.GetPhysicalDB;
import com.cqrcb.orm.physicaldb.PhysicaldbOracleOper;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.Plugin.ModelClassType;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.codegen.mybatis3.model.SimpleModelGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JDBCConnectionConfiguration;
import org.mybatis.generator.internal.util.JavaBeansUtil;
import org.mybatis.generator.internal.util.messages.Messages;

public class CIMSSimpleModelGenerator
        extends SimpleModelGenerator {
    static GetPhysicalDB getPhysicalDb;

    public List<CompilationUnit> getCompilationUnits() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.8", table.toString()));
        Plugin plugins = this.context.getPlugins();


        CommentGenerator commentGenerator = this.context.getCommentGenerator();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);


        topLevelClass.addImportedType("javax.persistence.*");
        topLevelClass.addImportedType(getRootClass());
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(topLevelClass);
        if (getPhysicalDb == null) {
            getPhysicalDb = new GetPhysicalDB();
            String driverClassName = this.context.getJdbcConnectionConfiguration().getDriverClass();
            String url = this.context.getJdbcConnectionConfiguration().getConnectionURL();
            String userName = this.context.getJdbcConnectionConfiguration().getUserId();
            String passWord = this.context.getJdbcConnectionConfiguration().getPassword();
            try {
                getPhysicalDb.init(driverClassName, url, userName, passWord);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        PhysicaldbOracleOper physicaldbOracleOper = getPhysicalDb.getOper(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());


        List<String> classAnnotations = physicaldbOracleOper.getClassAnnotationList();
        if (classAnnotations != null) {
            for (String classAnnotation : classAnnotations) {
                if ((classAnnotation != null) && (classAnnotation.length() > 0)) {
                    topLevelClass.addAnnotation(classAnnotation);
                }
            }
        }
        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            topLevelClass.setSuperClass(superClass);
            topLevelClass.addImportedType(superClass);
        }
        commentGenerator.addModelClassComment(topLevelClass, this.introspectedTable);

        List<IntrospectedColumn> introspectedColumns = this.introspectedTable.getAllColumns();
        if (this.introspectedTable.isConstructorBased())
//    {
//      addParameterizedConstructor(topLevelClass);
//      if (!this.introspectedTable.isImmutable()) {
//        addDefaultConstructor(topLevelClass);
//      }
//    }
        addParameterizedConstructor(topLevelClass);
        if (!this.introspectedTable.isImmutable()) {
            addDefaultConstructor(topLevelClass);
        }
        String rootClass = getRootClass();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            if (!RootClassInfo.getInstance(rootClass, this.warnings).containsProperty(introspectedColumn)) {
                Field field = JavaBeansUtil.getJavaBeansField(introspectedColumn, this.context, this.introspectedTable);
                if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                    List<String> columnAnnotations = physicaldbOracleOper.getFieldAnnotationList(introspectedColumn.getActualColumnName());
                    if (columnAnnotations != null) {
                        for (String columnAnnotation : columnAnnotations) {
                            if ((columnAnnotation != null) && (columnAnnotation.length() > 0)) {
                                field.addAnnotation(columnAnnotation);
                            }
                        }
                    }
                    topLevelClass.addField(field);
                    topLevelClass.addImportedType(field.getType());
                }
                Method method = JavaBeansUtil.getJavaBeansGetter(introspectedColumn, this.context, this.introspectedTable);
                if (plugins.modelGetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                    topLevelClass.addMethod(method);
                }
                if (!this.introspectedTable.isImmutable()) {
                    method = JavaBeansUtil.getJavaBeansSetter(introspectedColumn, this.context, this.introspectedTable);
                    if (plugins.modelSetterMethodGenerated(method, topLevelClass, introspectedColumn, this.introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                        topLevelClass.addMethod(method);
                    }
                }
            }
        }
        List<CompilationUnit> answer = new ArrayList();
        if (this.context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, this.introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private FullyQualifiedJavaType getSuperClass() {
        String rootClass = getRootClass();
        FullyQualifiedJavaType superClass;
//    FullyQualifiedJavaType superClass;
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        } else {
            superClass = null;
        }
        return superClass;
    }

    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());

        List<IntrospectedColumn> constructorColumns = this.introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), introspectedColumn.getJavaProperty()));
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);

        StringBuilder sb = new StringBuilder();
        List<IntrospectedColumn> introspectedColumns = this.introspectedTable.getAllColumns();
        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            sb.setLength(0);
            sb.append("this.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = ");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }
        topLevelClass.addMethod(method);
    }
}
