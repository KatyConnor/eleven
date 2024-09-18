package com.cqrcb.cims.tools.mybatis.generator.impl;

import com.cqrcb.cims.tools.mybatis.generator.model.CIMSSimpleModelGenerator;
import com.cqrcb.cims.tools.mybatis.generator.xmlmapper.CIMSXMLMapperGenerator;
import java.util.List;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3SimpleImpl;

public class CIMSIntrospectedTableMyBatis3SimpleImpl
  extends IntrospectedTableMyBatis3SimpleImpl
{
  protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback)
  {
    this.xmlMapperGenerator = new CIMSXMLMapperGenerator();
    initializeAbstractGenerator(this.xmlMapperGenerator, warnings, progressCallback);
  }
  
  protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback)
  {
    AbstractJavaGenerator javaGenerator = new CIMSSimpleModelGenerator();
    initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
    this.javaModelGenerators.add(javaGenerator);
  }
}
