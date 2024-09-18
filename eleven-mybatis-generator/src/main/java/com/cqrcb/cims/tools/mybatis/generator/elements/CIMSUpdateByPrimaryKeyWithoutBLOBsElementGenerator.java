package com.cqrcb.cims.tools.mybatis.generator.elements;

import java.util.Iterator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByPrimaryKeyWithoutBLOBsElementGenerator;

public class CIMSUpdateByPrimaryKeyWithoutBLOBsElementGenerator
  extends UpdateByPrimaryKeyWithoutBLOBsElementGenerator
{
  private boolean isSimple;
  
  public CIMSUpdateByPrimaryKeyWithoutBLOBsElementGenerator(boolean isSimple)
  {
    super(isSimple);
    this.isSimple = isSimple;
  }
  
  public void addElements(XmlElement parentElement)
  {
    XmlElement answer = new XmlElement("update");
    

    String id = this.introspectedTable.getUpdateByPrimaryKeyStatementId();
    answer.addAttribute(new Attribute("id", id));
    

    String parameterType = this.introspectedTable.getBaseRecordType();
    parameterType = parameterType.indexOf('.') != -1 ? parameterType.substring(parameterType.lastIndexOf('.') + 1) : parameterType;
    
    answer.addAttribute(new Attribute("parameterType", parameterType));
    

    this.context.getCommentGenerator().addComment(answer);
    
    StringBuilder sb = new StringBuilder();
    sb.append("update ");
    sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
    answer.addElement(new TextElement(sb.toString()));
    
    sb.setLength(0);
    sb.append("set ");
    Iterator<IntrospectedColumn> iter;
//    Iterator<IntrospectedColumn> iter;
    if (this.isSimple) {
      iter = this.introspectedTable.getNonPrimaryKeyColumns().iterator();
    } else {
      iter = this.introspectedTable.getBaseColumns().iterator();
    }
    while (iter.hasNext())
    {
      IntrospectedColumn introspectedColumn = (IntrospectedColumn)iter.next();
      sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
      sb.append(" = ");
      

      String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
      parameterClause = parameterClause.indexOf(',') != -1 ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}' : parameterClause;
      
      sb.append(parameterClause);
      if (iter.hasNext())
      {
        sb.append(',');
        if (sb.length() > 80)
        {
          answer.addElement(new TextElement(sb.toString()));
          sb.setLength(0);
          OutputUtilities.xmlIndent(sb, 1);
        }
      }
      else
      {
        answer.addElement(new TextElement(sb.toString()));
      }
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
    if (this.context.getPlugins().sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
