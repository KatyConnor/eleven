package com.hx.nine.eleven.jooq.jdbc.generator;

import com.hx.nine.eleven.jooq.jdbc.utils.FileCopyUtils;
import org.jooq.codegen.GeneratorStrategy;
import org.jooq.codegen.JavaGenerator;
import org.jooq.codegen.JavaWriter;
import org.jooq.meta.*;
import org.jooq.tools.JooqLogger;
import org.jooq.tools.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义Java代码生成器
 *
 * @author wml
 * @date 2023-04-20
 */
public class HXDefaultJavaGenerator extends JavaGenerator {

	private static final JooqLogger log = JooqLogger.getLogger(HXDefaultJavaGenerator.class);
	private static final List<String> fieldList = Arrays.asList(new String[]{"id", "createTime", "updateTime", "version", "effective"});

	/**
	 * 重写了 generateDao， 具体的生成逻辑还是调用父级的方法，只是在生成完成后，获取文件内容，
	 * 然后对文件指定的内容进行替换操作
	 *
	 * @param table
	 */
	@Override
	protected void generateDao(TableDefinition table) {
		super.generateDao(table);
		File file = getFile(table, GeneratorStrategy.Mode.DAO);
		if (file.exists()) {
			try {
				String fileContent = new String(FileCopyUtils.copyToByteArray(file));
				String oldExtends = " extends DAOImpl";
				String newExtends = " extends AbstractBaseMapperDAO";
				fileContent = fileContent.replace("import org.jooq.impl.DAOImpl;\n", "");
				fileContent = fileContent.replace(oldExtends, newExtends);
				FileCopyUtils.copy(fileContent.getBytes(), file);
			} catch (IOException e) {
				log.error("generate Mapper Dao error: {}", file.getAbsolutePath(), e);
			}
		}
	}

	@Override
	protected void generateDao(TableDefinition table, JavaWriter out) {
		// 用于生成 import com.hx.vertx.jooq.jdbc.mapper.AbstractBaseMapperDAO 内容
//		out.ref(JooqAbstractBaseMapperDAO.class);
		super.generateDao(table, out);
	}

	@Override
	protected void generateUDTPojoClassFooter(UDTDefinition udt, JavaWriter out) {
		super.generateUDTPojoClassFooter(udt, out);
	}

	@Override
	protected void generatePojo(TableDefinition table) {
		super.generatePojo(table);
		File file = getFile(table, GeneratorStrategy.Mode.POJO);
		if (file.exists()) {
			try {
				String fileContent = new String(FileCopyUtils.copyToByteArray(file));
				String oldExtends = " implements Serializable";
				String newExtends = " extends BasePO<String> implements Serializable";
				fileContent = fileContent.replace(oldExtends, newExtends);
				// 处理多余字段
				fileContent = fileContent.replace("String effective,", "Boolean effective,");

				fileContent = fileContent.replace("this.id = value.id;", "super.setId(value.getId());");
				fileContent = fileContent.replace("this.effective = value.effective;", "super.setEffective(value.getEffective());");
				fileContent = fileContent.replace("this.version = value.version;", "super.setVersion(value.getVersion());");
				fileContent = fileContent.replace("this.createTime = value.createTime;", "super.setCreateTime(value.getCreateTime());");
				fileContent = fileContent.replace("this.updateTime = value.updateTime;", "super.setUpdateTime(value.getUpdateTime());");

				fileContent = fileContent.replace("this.id = id;", "super.setId(id);");
				fileContent = fileContent.replace("this.effective = effective;", "super.setEffective(effective);");
				fileContent = fileContent.replace("this.version = version;", "super.setVersion(version);");
				fileContent = fileContent.replace("this.createTime = createTime;", "super.setCreateTime(createTime);");
				fileContent = fileContent.replace("this.updateTime = updateTime;", "super.setUpdateTime(updateTime);");

				System.out.println(fileContent);
				FileCopyUtils.copy(fileContent.getBytes(), file);
			} catch (IOException e) {
				log.error("generate pojo error: {}", file.getAbsolutePath(), e);
			}
		}
	}

	@Override
	protected void generatePojoToString(Definition tableOrUDT, JavaWriter out) {
		out.println();
		out.println("@Override");
		out.println("public String toString() {");
		out.println("return ObjectUtils.toString(this);");
		out.println("}");
	}

	@Override
	protected void generatePojo(TableDefinition table, JavaWriter out) {
		out.ref("com.hx.domain.framework.obj.po.BasePO");
		out.ref(" com.hx.nine.eleven.commons.utils.ObjectUtils");
//		super.generatePojo(table, out);
		this.generatePojo0(table, out);
	}

	private final void generatePojo0(Definition tableUdtOrEmbeddable, JavaWriter out) {
		final String className = getStrategy().getJavaClassName(tableUdtOrEmbeddable, GeneratorStrategy.Mode.POJO);
		final String interfaceName = generateInterfaces()
				? out.ref(getStrategy().getFullJavaClassName(tableUdtOrEmbeddable, GeneratorStrategy.Mode.INTERFACE))
				: "";
		final String superName = out.ref(getStrategy().getJavaClassExtends(tableUdtOrEmbeddable, GeneratorStrategy.Mode.POJO));
		final List<String> interfaces = out.ref(getStrategy().getJavaClassImplements(tableUdtOrEmbeddable, GeneratorStrategy.Mode.POJO));

		if (generateInterfaces()) {
			interfaces.add(interfaceName);
		}

		final List<String> superTypes = list(superName, interfaces);
		printPackage(out, tableUdtOrEmbeddable, GeneratorStrategy.Mode.POJO);

		if (tableUdtOrEmbeddable instanceof TableDefinition) {
			generatePojoClassJavadoc((TableDefinition) tableUdtOrEmbeddable, out);
		} else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition) {
			generateEmbeddableClassJavadoc((EmbeddableDefinition) tableUdtOrEmbeddable, out);
		} else {
			generateUDTPojoClassJavadoc((UDTDefinition) tableUdtOrEmbeddable, out);
		}

		printClassAnnotations(out, tableUdtOrEmbeddable, GeneratorStrategy.Mode.POJO);

		if (tableUdtOrEmbeddable instanceof TableDefinition) {
			printTableJPAAnnotation(out, (TableDefinition) tableUdtOrEmbeddable);
		}

		int maxLength = 0;
		for (TypedElementDefinition<?> column : getTypedElements(tableUdtOrEmbeddable)) {
			maxLength = Math.max(maxLength, out.ref(getJavaType(column.getType(resolver(out, GeneratorStrategy.Mode.POJO)), out, GeneratorStrategy.Mode.POJO)).length());
		}


		out.println("public class %s[[before= extends ][%s]][[before= implements ][%s]] {", className, list(superName), interfaces);

		if (generateSerializablePojos() || generateSerializableInterfaces()) {
			out.printSerial();
		}

		out.println();

		for (TypedElementDefinition<?> column : getTypedElements(tableUdtOrEmbeddable)) {
			String columnName = getStrategy().getJavaMemberName(column, GeneratorStrategy.Mode.POJO);
			if (!fieldList.contains(columnName)) {
				out.println("private %s%s %s;",
						generateImmutablePojos() ? "final " : "",
						StringUtils.rightPad(out.ref(getJavaType(column.getType(resolver(out, GeneratorStrategy.Mode.POJO)), out, GeneratorStrategy.Mode.POJO)), maxLength),
						columnName);
			}
		}


		// Constructors
		// ---------------------------------------------------------------------

		// Default constructor
		if (!generateImmutablePojos()) {
			generatePojoDefaultConstructor(tableUdtOrEmbeddable, out);
		}

		// [#1363] [#7055] copy constructor
		generatePojoCopyConstructor(tableUdtOrEmbeddable, out);

		// Multi-constructor
		generatePojoMultiConstructor(tableUdtOrEmbeddable, out);

		List<? extends TypedElementDefinition<?>> elements = getTypedElements(tableUdtOrEmbeddable);
		for (int i = 0; i < elements.size(); i++) {
			TypedElementDefinition<?> column = elements.get(i);
			String columnName = getStrategy().getJavaMemberName(column, GeneratorStrategy.Mode.POJO);
			if (!fieldList.contains(columnName)) {
				// Getter
				if (tableUdtOrEmbeddable instanceof TableDefinition){
					generatePojoGetter(column, i, out);
				} else{
					generateUDTPojoGetter(column, i, out);
				}

				// Setter
				if (!generateImmutablePojos()){
					if (tableUdtOrEmbeddable instanceof TableDefinition){
						generatePojoSetter(column, i, out);
					} else{
						generateUDTPojoSetter(column, i, out);
					}
				}
			}
		}

		if (tableUdtOrEmbeddable instanceof TableDefinition || tableUdtOrEmbeddable.getClass().isAssignableFrom(TableDefinition.class)) {
			List<EmbeddableDefinition> embeddables = ((TableDefinition) tableUdtOrEmbeddable).getReferencedEmbeddables();

			for (int i = 0; i < embeddables.size(); i++) {
				EmbeddableDefinition embeddable = embeddables.get(i);
				String fieldName = getStrategy().getJavaMemberName(embeddable);
				if (!fieldList.contains(fieldName)) {
					generateEmbeddablePojoSetter(embeddable, i, out);
					generateEmbeddablePojoGetter(embeddable, i, out);
				}
			}
		}

		if (generatePojosEqualsAndHashCode()) {
			generatePojoEqualsAndHashCode(tableUdtOrEmbeddable, out);
		}

		if (generatePojosToString()) {
			generatePojoToString(tableUdtOrEmbeddable, out);
		}

		if (generateInterfaces() && !generateImmutablePojos()) {
			printFromAndInto(out, tableUdtOrEmbeddable, GeneratorStrategy.Mode.POJO);
		}
		if (tableUdtOrEmbeddable instanceof TableDefinition) {
			generatePojoClassFooter((TableDefinition) tableUdtOrEmbeddable, out);
		} else if (tableUdtOrEmbeddable instanceof EmbeddableDefinition) {
			generateEmbeddableClassFooter((EmbeddableDefinition) tableUdtOrEmbeddable, out);
		} else {
			generateUDTPojoClassFooter((UDTDefinition) tableUdtOrEmbeddable, out);
		}
		out.println("}");
		closeJavaWriter(out);
	}

	private static final <T> List<T> list(T first, List<T> remaining) {
		List<T> result = new ArrayList<>();

		result.addAll(list(first));
		result.addAll(remaining);

		return result;
	}

	@SafeVarargs
	private static final <T> List<T> list(T... objects) {
		List<T> result = new ArrayList<>();
		if (objects != null) {
			for (T object : objects) {
				if (object != null && !"".equals(object)) {
					result.add(object);
				}
			}
		}
		return result;
	}

	private List<? extends TypedElementDefinition<? extends Definition>> getTypedElements(Definition definition) {
		if (definition instanceof TableDefinition) {
			return ((TableDefinition) definition).getColumns();
		} else if (definition instanceof EmbeddableDefinition) {
			return ((EmbeddableDefinition) definition).getColumns();
		} else if (definition instanceof UDTDefinition) {
			return ((UDTDefinition) definition).getAttributes();
		} else if (definition instanceof RoutineDefinition) {
			return ((RoutineDefinition) definition).getAllParameters();
		} else {
			throw new IllegalArgumentException("Unsupported type : " + definition);
		}
	}

	private void printFromAndInto(JavaWriter out, Definition tableOrUDT, GeneratorStrategy.Mode mode) {
		String qualified = out.ref(getStrategy().getFullJavaClassName(tableOrUDT, GeneratorStrategy.Mode.INTERFACE));

		out.header("FROM and INTO");
		boolean override = generateInterfaces() && !generateImmutableInterfaces();
		out.overrideInheritIf(override);
		out.println("public void from(%s from) {", qualified);

		for (TypedElementDefinition<?> column : getTypedElements(tableOrUDT)) {
			final String setter = getStrategy().getJavaSetterName(column, GeneratorStrategy.Mode.INTERFACE);
			final String getter = getStrategy().getJavaGetterName(column, GeneratorStrategy.Mode.INTERFACE);

			// TODO: Use appropriate Mode here
			final String member = getStrategy().getJavaMemberName(column, GeneratorStrategy.Mode.POJO);

			out.println("%s(from.%s());", setter, getter);
		}

		out.println("}");

		if (override) {
			// [#10191] Java and Kotlin can produce overloads for this method despite
			// generic type erasure, but Scala cannot, see
			// https://twitter.com/lukaseder/status/1262652304773259264
			out.overrideInherit();
			out.println("public <E extends %s> E into(E into) {", qualified);
			out.println("into.from(this);");
			out.println("return into;");
			out.println("}");
		}
	}
}
