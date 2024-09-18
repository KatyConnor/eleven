package com.cqrcb.cims.tools.mybatis.generator.elements.xml;

import java.util.Collections;
import java.util.List;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class CIMSXmlElement
  extends XmlElement
{
  public CIMSXmlElement(String name)
  {
    super(name);
  }
  
  public String getFormattedContent(int indentLevel)
  {
    String name = getName();
    List<Attribute> attributes = getAttributes();
    List<Element> elements = getElements();
    
    StringBuilder sb = new StringBuilder();
    
    OutputUtilities.xmlIndent(sb, indentLevel);
    sb.append('<');
    sb.append(name);
    
    Collections.sort(attributes);
    for (Attribute att : attributes)
    {
      sb.append(' ');
      sb.append(att.getFormattedContent());
    }
    if (elements.size() > 0)
    {
      sb.append(">");
      for (Element element : elements) {
        sb.append(element.getFormattedContent(indentLevel + 1));
      }
      OutputUtilities.xmlIndent(sb, indentLevel);
      sb.append("</");
      sb.append(name);
      sb.append('>');
    }
    else
    {
      sb.append(" />");
    }
    return sb.toString();
  }
}
