package com.cqrcb.cims.tools.mybatis.generator.elements;

import java.util.List;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.rules.Rules;
import org.mybatis.generator.internal.util.StringUtility;

public class CIMSResultMapWithoutBLOBsElementGenerator
  extends ResultMapWithoutBLOBsElementGenerator
{
  private boolean isSimple;
  
  public CIMSResultMapWithoutBLOBsElementGenerator(boolean isSimple)
  {
    super(isSimple);
    this.isSimple = isSimple;
  }
  
  public void addElements(XmlElement parentElement)
  {
    XmlElement answer = new XmlElement("resultMap");
    

    String id = this.introspectedTable.getBaseResultMapId();
    answer.addAttribute(new Attribute("id", id));
    String returnType;
    if (this.isSimple)
    {
      returnType = this.introspectedTable.getBaseRecordType();
    }
    else
    {
//      String returnType;
      if (this.introspectedTable.getRules().generateBaseRecordClass()) {
        returnType = this.introspectedTable.getBaseRecordType();
      } else {
        returnType = this.introspectedTable.getPrimaryKeyType();
      }
    }
    returnType = returnType.indexOf('.') != -1 ? returnType.substring(returnType.lastIndexOf('.') + 1) : returnType;
    answer.addAttribute(new Attribute("type", returnType));
    
    this.context.getCommentGenerator().addComment(answer);
    if (this.introspectedTable.isConstructorBased()) {
      addResultMapConstructorElements(answer);
    } else {
      addResultMapElements(answer);
    }
    if (this.context.getPlugins().sqlMapResultMapWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
  
  private void addResultMapElements(XmlElement answer)
  {
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns())
    {
      XmlElement resultElement = new XmlElement("id");
      
      resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
      
      resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
      resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
      if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
        resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
      }
      answer.addElement(resultElement);
    }
    List<IntrospectedColumn> columns;
//    List<IntrospectedColumn> columns;
    if (this.isSimple) {
      columns = this.introspectedTable.getNonPrimaryKeyColumns();
    } else {
      columns = this.introspectedTable.getBaseColumns();
    }
    for (IntrospectedColumn introspectedColumn : columns)
    {
      XmlElement resultElement = new XmlElement("result");
      
      resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
      
      resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
      resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
      if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
        resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
      }
      answer.addElement(resultElement);
    }
  }
  
  private void addResultMapConstructorElements(XmlElement answer)
  {
    XmlElement constructor = new XmlElement("constructor");
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns())
    {
      XmlElement resultElement = new XmlElement("idArg");
      
      resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
      
      resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
      resultElement.addAttribute(new Attribute("javaType", introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
      if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
        resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
      }
      constructor.addElement(resultElement);
    }
    List<IntrospectedColumn> columns;
//    List<IntrospectedColumn> columns;
    if (this.isSimple) {
      columns = this.introspectedTable.getNonPrimaryKeyColumns();
    } else {
      columns = this.introspectedTable.getBaseColumns();
    }
    for (IntrospectedColumn introspectedColumn : columns)
    {
      XmlElement resultElement = new XmlElement("arg");
      
      resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
      
      resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));
      resultElement.addAttribute(new Attribute("javaType", introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
      if (StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
        resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
      }
      constructor.addElement(resultElement);
    }
    answer.addElement(constructor);
  }
}
