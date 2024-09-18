package com.cqrcb.cims.tools.mybatis.generator.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.SelectByPrimaryKeyElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

public class CIMSSelectByPrimaryKeyElementGenerator extends SelectByPrimaryKeyElementGenerator {
	private static String CREATE_USRE_FIELD_NAME = "";
	private static String CREATE_ORG_FIELD_NAME = "";
	private static String MANAGE_USRE_FIELD_NAME = "";
	private static String MANAGE_ORG_FIELD_NAME = "";
	public void addElements(XmlElement parentElement) {
		System.out.println("-----------------------addSelectByPrimaryKeyElement--------   addElements  --------------");

		XmlElement answer = new XmlElement("select");

		String id = this.introspectedTable.getSelectByPrimaryKeyStatementId();
		answer.addAttribute(new Attribute("id", id));

		String parameterType = this.introspectedTable.getBaseRecordType();
		parameterType = parameterType.indexOf('.') != -1 ? parameterType.substring(parameterType.lastIndexOf('.') + 1)
				: parameterType;

		answer.addAttribute(new Attribute("parameterType", parameterType));

		answer.addAttribute(new Attribute("resultMap", "BaseResultMap"));

		this.context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("select ");
		if (StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
			sb.append('\'');
			sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
			sb.append("' as QUERYID,");
		}
		answer.addElement(new TextElement(sb.toString()));
		answer.addElement(getBaseColumnListElement());

		sb.setLength(0);
		sb.append("from ");
		sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
		answer.addElement(new TextElement(sb.toString()));

		boolean and = false;
		for (IntrospectedColumn introspectedColumn : this.introspectedTable.getPrimaryKeyColumns()) {
			sb.setLength(0);
			if (and) {
				sb.append("  and ");
			} else {
				sb.append("where ");
				and = true;
			}
			sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
			sb.append(" = ");

			String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
			parameterClause = parameterClause.indexOf(',') != -1
					? parameterClause.substring(0, parameterClause.indexOf(',')) + '}'
					: parameterClause;

			sb.append(parameterClause);

			answer.addElement(new TextElement(sb.toString()));
		}
		//新增查詢條件

		XmlElement ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myself4Creator == true"));
		ifElement.addElement(new TextElement("and (CREATE_USER_NO = #{opUserNo} OR EXECUTIVE_USER_NO = #{opUserNo})"));
		answer.addElement(ifElement);

		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myself4Manager == true"));
		ifElement.addElement(new TextElement("and EXECUTIVE_USER_NO = #{opUserNo}"));
		answer.addElement(ifElement);

		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myOrg4Creator == true"));
		ifElement.addElement(new TextElement("and (CREATE_ORG_NO = #{opOrgNo} OR EXECUTIVE_ORG_NO = #{opOrgNo})"));
		answer.addElement(ifElement);

		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myOrg4Manager == true"));
		ifElement.addElement(new TextElement("and EXECUTIVE_ORG_NO = #{opOrgNo}"));
		answer.addElement(ifElement);

		ifElement = new XmlElement("if");
		ifElement.addAttribute(new Attribute("test", "myOrgAndBranch == true"));
		ifElement.addElement(new TextElement("and EXECUTIVE_ORG_NO in (select ORG_NO from TC_RGT_ORG where LOCATION like concat(concat('%', #{opOrgNo}), '%')) "));
		answer.addElement(ifElement);
		  
//		sb.setLength(0);
//		sb.append("<if test=\"forUpdate == true\">for update</if>");
//		answer.addElement(new TextElement(sb.toString()));
		if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
			parentElement.addElement(answer);
		}
	}
}
