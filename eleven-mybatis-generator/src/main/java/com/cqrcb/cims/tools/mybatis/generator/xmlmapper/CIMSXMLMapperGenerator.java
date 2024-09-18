package com.cqrcb.cims.tools.mybatis.generator.xmlmapper;

import com.cqrcb.cims.tools.mybatis.generator.elements.*;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.messages.Messages;

public class CIMSXMLMapperGenerator extends XMLMapperGenerator {

  protected XmlElement getSqlMapElement()
  {
    FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
    this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
    XmlElement answer = new XmlElement("mapper");
    String namespace = this.introspectedTable.getBaseRecordType();
    namespace = namespace.indexOf('.') != -1 ? namespace.substring(namespace.lastIndexOf('.') + 1) : namespace;
    answer.addAttribute(new Attribute("namespace", namespace));
    
    this.context.getCommentGenerator().addRootComment(answer);
    
    addResultMapWithoutBLOBsElement(answer);
    addBaseColumnListElement(answer);
    addSelectByPrimaryKeyElement(answer);
    addInsertElement(answer);
    addUpdateByPrimaryKeyWithoutBLOBsElement(answer);
    addInsertSelectiveElement(answer);
    addUpdateByPrimaryKeySelectiveElement(answer);
    
    addSelectByExampleWithoutBLOBsElement(answer);
    addDeleteByPrimaryKeyElement(answer);
    addUpdateByParamsSelectiveElement(answer);
    
    return answer;
  }
  
  protected void addResultMapWithoutBLOBsElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateBaseResultMap())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSResultMapWithoutBLOBsElementGenerator(true);
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addSelectByPrimaryKeyElement(XmlElement parentElement)
  {
    System.out.println("-----------------------addSelectByPrimaryKeyElement-----------"+this.introspectedTable.getRules().generateSelectByPrimaryKey()+"-----------");
    if (this.introspectedTable.getRules().generateSelectByPrimaryKey())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSSelectByPrimaryKeyElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addInsertElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateInsert())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSInsertElementGenerator(true);
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addInsertSelectiveElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateInsertSelective())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSInsertSelectiveElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSUpdateByPrimaryKeyWithoutBLOBsElementGenerator(true);
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSUpdateByPrimaryKeySelectiveElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addSelectByExampleWithoutBLOBsElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSSelectByExampleWithoutBLOBsElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }
  
  protected void addDeleteByPrimaryKeyElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSDeleteByPrimaryKeyElementGenerator(true);
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }

  protected void addUpdateByParamsSelectiveElement(XmlElement parentElement)
  {
    if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective())
    {
      AbstractXmlElementGenerator elementGenerator = new CIMSUpdateByParamsSelectiveElementGenerator();
      initializeAndExecuteGenerator(elementGenerator, parentElement);
    }
  }



}
