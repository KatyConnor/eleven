package com.cqrcb.cims.tools.mybatis.generator.elements;

import com.cqrcb.cims.tools.mybatis.generator.elements.xml.CIMSXmlElement;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.UpdateByExampleSelectiveElementGenerator;

import java.util.Iterator;

public class CIMSUpdateByParamsSelectiveElementGenerator extends UpdateByExampleSelectiveElementGenerator {
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", "updateByParamsSelective"));
        answer.addAttribute(new Attribute("parameterType", "map"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);
        Iterator columnIterator = this.introspectedTable.getAllColumns().iterator();

        while(columnIterator.hasNext()) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn)columnIterator.next();
            XmlElement isNotNullElement = new CIMSXmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("record."));
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
//            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record."));

            String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "record.");
            parameterClause = parameterClause.indexOf(',') != -1
                    ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}'
                    : parameterClause;

            sb.append(parameterClause);

            sb.append(',');
            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        XmlElement dynamicElement4where = new XmlElement("where");
        answer.addElement(dynamicElement4where);
        columnIterator = this.introspectedTable.getAllColumns().iterator();
        while(columnIterator.hasNext()) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn)columnIterator.next();
            XmlElement isNotNullElement = new CIMSXmlElement("if");
            sb.setLength(0);
            sb.append(introspectedColumn.getJavaProperty("params."));
            sb.append(" != null");
            isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement4where.addElement(isNotNullElement);
            sb.setLength(0);
            sb.append("and ");
            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = ");
//            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "params."));

            String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "params.");
            parameterClause = parameterClause.indexOf(',') != -1
                    ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}'
                    : parameterClause;

            sb.append(parameterClause);

            isNotNullElement.addElement(new TextElement(sb.toString()));
        }

        if (this.context.getPlugins().sqlMapUpdateByExampleSelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
