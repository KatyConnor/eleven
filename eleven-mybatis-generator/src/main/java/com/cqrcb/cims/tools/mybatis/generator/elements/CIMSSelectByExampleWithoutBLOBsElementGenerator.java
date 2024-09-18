package com.cqrcb.cims.tools.mybatis.generator.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByExampleWithoutBLOBsElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import com.cqrcb.cims.tools.mybatis.generator.elements.xml.CIMSXmlElement;

public class CIMSSelectByExampleWithoutBLOBsElementGenerator extends SelectByExampleWithoutBLOBsElementGenerator {
	public void addElements(XmlElement parentElement) {

		XmlElement answer = new XmlElement("select");

		answer.addAttribute(new Attribute("id", "selectList"));
		answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));

		String parameterType = this.introspectedTable.getBaseRecordType();
		parameterType = parameterType.indexOf('.') != -1 ? parameterType.substring(parameterType.lastIndexOf('.') + 1)
				: parameterType;

		answer.addAttribute(new Attribute("parameterType", parameterType));

		this.context.getCommentGenerator().addComment(answer);

		answer.addElement(new TextElement("select"));
		XmlElement ifElement = new XmlElement("if");

		StringBuilder sb = new StringBuilder();
		if (StringUtility.stringHasValue(this.introspectedTable.getSelectByExampleQueryId())) {
			sb.append('\'');
			sb.append(this.introspectedTable.getSelectByExampleQueryId());
			sb.append("' as QUERYID,");
			answer.addElement(new TextElement(sb.toString()));
		}
		answer.addElement(getBaseColumnListElement());

		sb.setLength(0);
		sb.append("from ");
		sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));
//		answer.addElement(getExampleIncludeElement());

		XmlElement dynamicElement = new XmlElement("where");
		answer.addElement(dynamicElement);
		for (IntrospectedColumn introspectedColumn : this.introspectedTable.getAllColumns()) {
			XmlElement isNotNullElement = new CIMSXmlElement("if");
			sb.setLength(0);
			sb.append(introspectedColumn.getJavaProperty());
			sb.append(" != null");
			isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
			dynamicElement.addElement(isNotNullElement);

			sb.setLength(0);
			sb.append("and ");
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = ");

			String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
			parameterClause = parameterClause.indexOf(',') != -1
					? parameterClause.substring(0, parameterClause.indexOf(',')) + '}'
					: parameterClause;

			sb.append(parameterClause);

			isNotNullElement.addElement(new TextElement(sb.toString()));
		}
		ifElement.addAttribute(new Attribute("test", "myself4Creator == true"));
		ifElement.addElement(new TextElement("and (CREATE_USER_NO = #{opUserNo} OR EXECUTIVE_USER_NO = #{opUserNo})"));
		dynamicElement.addElement(ifElement);

		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myself4Manager == true"));
		ifElement.addElement(new TextElement("and EXECUTIVE_USER_NO = #{opUserNo}"));
		dynamicElement.addElement(ifElement);
		
		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myOrg4Creator == true"));
		ifElement.addElement(new TextElement("and (CREATE_ORG_NO = #{opOrgNo} OR EXECUTIVE_ORG_NO = #{opOrgNo})"));
		dynamicElement.addElement(ifElement);

		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myOrg4Manager == true"));
		ifElement.addElement(new TextElement("and EXECUTIVE_ORG_NO = #{opOrgNo}"));
		dynamicElement.addElement(ifElement);
		
		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myOrgAndBranch == true"));
		ifElement.addElement(new TextElement("and EXECUTIVE_ORG_NO in (select ORG_NO from TC_RGT_ORG where LOCATION like concat(concat('%', #{opOrgNo}), '%')) "));
		dynamicElement.addElement(ifElement);
		
//		ifElement = new XmlElement("if");
//		ifElement.addAttribute(new Attribute("test", "orderByClause != null"));
//		ifElement.addElement(new TextElement("order by ${orderByClause}"));
//		answer.addElement(ifElement);
		if (this.context.getPlugins().sqlMapSelectByExampleWithoutBLOBsElementGenerated(answer,
				this.introspectedTable)) {
			parentElement.addElement(answer);
		}
	}
}
