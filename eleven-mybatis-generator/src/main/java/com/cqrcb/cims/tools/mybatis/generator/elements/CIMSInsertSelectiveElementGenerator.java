package com.cqrcb.cims.tools.mybatis.generator.elements;

import com.cqrcb.cims.tools.mybatis.generator.elements.xml.CIMSXmlElement;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertSelectiveElementGenerator;
import org.mybatis.generator.config.GeneratedKey;

public class CIMSInsertSelectiveElementGenerator
  extends InsertSelectiveElementGenerator
{
  public void addElements(XmlElement parentElement)
  {
    XmlElement answer = new XmlElement("insert");
    

    String id = this.introspectedTable.getInsertSelectiveStatementId();
    answer.addAttribute(new Attribute("id", id));
    

    FullyQualifiedJavaType parameterJavaType = this.introspectedTable.getRules().calculateAllFieldsClass();
    String parameterType = parameterJavaType.getFullyQualifiedName();
    parameterType = parameterType.indexOf('.') != -1 ? parameterType.substring(parameterType.lastIndexOf('.') + 1) : parameterType;
    
    answer.addAttribute(new Attribute("parameterType", parameterType));
    

    this.context.getCommentGenerator().addComment(answer);
    
    GeneratedKey gk = this.introspectedTable.getGeneratedKey();
    if (gk != null)
    {
      IntrospectedColumn introspectedColumn = this.introspectedTable.getColumn(gk.getColumn());
      if (introspectedColumn != null) {
        if (gk.isJdbcStandard())
        {
          answer.addAttribute(new Attribute("useGeneratedKeys", "true"));
          answer.addAttribute(new Attribute("keyProperty", introspectedColumn.getJavaProperty()));
        }
        else
        {
          answer.addElement(getSelectKey(introspectedColumn, gk));
        }
      }
    }
    StringBuilder sb = new StringBuilder();
    
    sb.append("insert into ");
    sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    
    XmlElement insertTrimElement = new XmlElement("trim");
    insertTrimElement.addAttribute(new Attribute("prefix", "("));
    insertTrimElement.addAttribute(new Attribute("suffix", ")"));
    insertTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
    answer.addElement(insertTrimElement);
    
    XmlElement valuesTrimElement = new XmlElement("trim");
    valuesTrimElement.addAttribute(new Attribute("prefix", "values ("));
    valuesTrimElement.addAttribute(new Attribute("suffix", ")"));
    valuesTrimElement.addAttribute(new Attribute("suffixOverrides", ","));
    answer.addElement(valuesTrimElement);
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
      if (!introspectedColumn.isIdentity()) {
        if ((introspectedColumn.isSequenceColumn()) || (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()))
        {
          sb.setLength(0);
          sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
          sb.append(',');
          insertTrimElement.addElement(new TextElement(sb.toString()));
          
          sb.setLength(0);
          sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
          sb.append(',');
          valuesTrimElement.addElement(new TextElement(sb.toString()));
        }
        else
        {
          XmlElement insertNotNullElement = new CIMSXmlElement("if");
          sb.setLength(0);
          sb.append(introspectedColumn.getJavaProperty());
          sb.append(" != null");
          insertNotNullElement.addAttribute(new Attribute("test", sb.toString()));
          
          sb.setLength(0);
          sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
          sb.append(',');
          insertNotNullElement.addElement(new TextElement(sb.toString()));
          insertTrimElement.addElement(insertNotNullElement);
          
          XmlElement valuesNotNullElement = new CIMSXmlElement("if");
          sb.setLength(0);
          sb.append(introspectedColumn.getJavaProperty());
          sb.append(" != null");
          valuesNotNullElement.addAttribute(new Attribute("test", sb.toString()));
          

          sb.setLength(0);
          String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
          parameterClause = parameterClause.indexOf(',') != -1 ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}' : parameterClause;
          
          sb.append(parameterClause);
          sb.append(',');
          valuesNotNullElement.addElement(new TextElement(sb.toString()));
          valuesTrimElement.addElement(valuesNotNullElement);
        }
      }
    }
    if (this.context.getPlugins().sqlMapInsertSelectiveElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
