package com.hx.vertx.jooq.jdbc.generator;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.CatalogDefinition;
import org.jooq.meta.Definition;
import org.jooq.meta.SchemaDefinition;
import org.jooq.tools.StringUtils;

/**
 * 自定义生成策略
 * @author wml
 * @date 2023-04-13
 *
 */
public class HXGeneratorStrategy extends DefaultGeneratorStrategy {


	@Override
	public String getGlobalReferencesFileHeader(Definition container, Class<? extends Definition> objectType) {
		return super.getGlobalReferencesFileHeader(container, objectType);
	}

	/**
	 * 重写class文件生成命名规则
	 * @param definition
	 * @param mode
	 * @return
	 */
	@Override
	public String getJavaClassName(Definition definition, Mode mode) {
		String name = getFixedJavaClassName(definition);
		return name != null ? name : this.getJavaClassName0(definition, mode);
	}

	/**
	 * 文件重命名
	 * @param definition
	 * @param mode
	 * @return
	 */
	private String getJavaClassName0(Definition definition, Mode mode) {
		StringBuilder result = new StringBuilder();
		result.append(StringUtils.toCamelCase(definition.getOutputName().replace(' ', '_').replace('-', '_').replace('.', '_')));
		if (mode == Mode.RECORD) {
			result.append("Record");
		} else if (mode == Mode.DAO) {
			result.append("Dao");
		} else if (mode == Mode.POJO) {
			result.append("Bo");
		} else if (mode == Mode.INTERFACE) {
			result.insert(0, "I");
		}
		return result.toString();
	}

	final String getFixedJavaClassName(Definition definition) {
		if (definition instanceof CatalogDefinition && ((CatalogDefinition) definition).isDefaultCatalog()) {
			return "DefaultCatalog";
		} else {
			return definition instanceof SchemaDefinition && ((SchemaDefinition) definition).isDefaultSchema() ? "DefaultSchema" : null;
		}
	}
}
