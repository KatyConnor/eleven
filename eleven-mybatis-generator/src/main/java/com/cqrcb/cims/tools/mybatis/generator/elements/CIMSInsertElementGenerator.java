package com.cqrcb.cims.tools.mybatis.generator.elements;

import java.util.ArrayList;
import java.util.List;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.InsertElementGenerator;
import org.mybatis.generator.config.GeneratedKey;

public class CIMSInsertElementGenerator
  extends InsertElementGenerator
{
  private boolean isSimple;
  
  public CIMSInsertElementGenerator(boolean isSimple)
  {
    super(isSimple);
    this.isSimple = isSimple;
  }
  
  public void addElements(XmlElement parentElement)
  {
    XmlElement answer = new XmlElement("insert");
    

    String id = this.introspectedTable.getInsertStatementId();
    answer.addAttribute(new Attribute("id", id));
    FullyQualifiedJavaType parameterJavaType;
    if (this.isSimple) {
      parameterJavaType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
    } else {
      parameterJavaType = this.introspectedTable.getRules().calculateAllFieldsClass();
    }
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
    StringBuilder insertClause = new StringBuilder();
    StringBuilder valuesClause = new StringBuilder();
    
    insertClause.append("insert into ");
    insertClause.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
    insertClause.append(" (");
    
    valuesClause.append("values (");
    
    List<String> valuesClauses = new ArrayList<String>();
    List<IntrospectedColumn> columns = this.introspectedTable.getAllColumns();
    for (int i = 0; i < columns.size(); i++)
    {
      IntrospectedColumn introspectedColumn = (IntrospectedColumn)columns.get(i);
      if (!introspectedColumn.isIdentity())
      {
        insertClause.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
        

        String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
        parameterClause = parameterClause.indexOf(',') != -1 ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}' : parameterClause;
        
        valuesClause.append(parameterClause);
        if ((i + 1 < columns.size()) && 
          (!((IntrospectedColumn)columns.get(i + 1)).isIdentity()))
        {
          insertClause.append(", ");
          valuesClause.append(", ");
        }
        if (valuesClause.length() > 80)
        {
          answer.addElement(new TextElement(insertClause.toString()));
          insertClause.setLength(0);
          OutputUtilities.xmlIndent(insertClause, 1);
          
          valuesClauses.add(valuesClause.toString());
          valuesClause.setLength(0);
          OutputUtilities.xmlIndent(valuesClause, 1);
        }
      }
    }
    insertClause.append(')');
    answer.addElement(new TextElement(insertClause.toString()));
    
    valuesClause.append(')');
    valuesClauses.add(valuesClause.toString());
    for (String clause : valuesClauses) {
      answer.addElement(new TextElement(clause));
    }
    if (this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
      parentElement.addElement(answer);
    }
  }
}
