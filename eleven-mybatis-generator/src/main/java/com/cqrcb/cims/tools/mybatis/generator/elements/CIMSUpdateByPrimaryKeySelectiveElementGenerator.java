package com.cqrcb.cims.tools.mybatis.generator.elements;

import com.cqrcb.cims.tools.mybatis.generator.elements.xml.CIMSXmlElement;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeySelectiveElementGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.rules.Rules;

public class CIMSUpdateByPrimaryKeySelectiveElementGenerator
  extends UpdateByPrimaryKeySelectiveElementGenerator
{
  public void addElements(XmlElement parentElement)
  {
    XmlElement answer = new XmlElement("update");
    

    String id = this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId();
    answer.addAttribute(new Attribute("id", id));
    String parameterType;
    if (this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
      parameterType = this.introspectedTable.getRecordWithBLOBsType();
    } else {
      parameterType = this.introspectedTable.getBaseRecordType();
    }
    parameterType = parameterType.indexOf('.') != -1 ? parameterType.substring(parameterType.lastIndexOf('.') + 1) : parameterType;
    
    answer.addAttribute(new Attribute("parameterType", parameterType));
    

    this.context.getCommentGenerator().addComment(answer);
    
    StringBuilder sb = new StringBuilder();
    
    sb.append("update ");
    sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    
    XmlElement dynamicElement = new XmlElement("set");
    answer.addElement(dynamicElement);
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getNonPrimaryKeyColumns())
    {
      XmlElement isNotNullElement = new CIMSXmlElement("if");
      sb.setLength(0);
      sb.append(introspectedColumn.getJavaProperty());
      sb.append(" != null");
      isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
      dynamicElement.addElement(isNotNullElement);
      
      sb.setLength(0);
      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      

      String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
      parameterClause = parameterClause.indexOf(',') != -1 ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}' : parameterClause;
      
      sb.append(parameterClause);
      sb.append(',');
      
      isNotNullElement.addElement(new TextElement(sb.toString()));
    }
    boolean and = false;
    for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns())
    {
      sb.setLength(0);
      if (and)
      {
        sb.append("  and ");
      }
      else
      {
        sb.append("where ");
        and = true;
      }
      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      

      String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
      parameterClause = parameterClause.indexOf(',') != -1 ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}' : parameterClause;
      
      sb.append(parameterClause);
      
      answer.addElement(new TextElement(sb.toString()));
    }
    if (this.context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
