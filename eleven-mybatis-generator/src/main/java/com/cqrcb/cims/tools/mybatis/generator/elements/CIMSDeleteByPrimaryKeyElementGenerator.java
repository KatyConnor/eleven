package com.cqrcb.cims.tools.mybatis.generator.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.DeleteByPrimaryKeyElementGenerator;

public class CIMSDeleteByPrimaryKeyElementGenerator extends DeleteByPrimaryKeyElementGenerator {

	private boolean isSimple;

	public CIMSDeleteByPrimaryKeyElementGenerator(boolean isSimple) {
		super(isSimple);
		// TODO Auto-generated constructor stub
		this.isSimple = isSimple;
	}

	public void addElements(XmlElement parentElement) {
		XmlElement answer = new XmlElement("delete");

		answer.addAttribute(new Attribute("id", this.introspectedTable.getDeleteByPrimaryKeyStatementId()));
		
		String parameterType = this.introspectedTable.getBaseRecordType();
		parameterType = parameterType.indexOf('.') != -1 ? parameterType.substring(parameterType.lastIndexOf('.') + 1)
				: parameterType;
		answer.addAttribute(new Attribute("parameterType", parameterType));

		this.context.getCommentGenerator().addComment(answer);

		StringBuilder sb = new StringBuilder();
		sb.append("delete from ");
		sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
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
			sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
			sb.append(" = ");
			String parameterClause = MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
	        parameterClause = parameterClause.indexOf(',') != -1 ? parameterClause.substring(0, parameterClause.indexOf(',')) + '}' : parameterClause;
			sb.append(parameterClause);
			answer.addElement(new TextElement(sb.toString()));
		}
		if (this.context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
			parentElement.addElement(answer);
		}
	}

}
