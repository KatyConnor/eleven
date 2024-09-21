package com.hx.nine.eleven.jooq.jdbc.enums;

import org.jooq.SQLDialect;

import java.util.Arrays;

public enum SQLDialectEnums {
	DEFAULT(":default", SQLDialect.DEFAULT),
	ORACLE(":oracle:", SQLDialect.DEFAULT),
	DERBY(":derby:", SQLDialect.DERBY),
	FIREBIRD(":firebirdsql:", SQLDialect.FIREBIRD),
	H2(":h2:", SQLDialect.H2),
	HSQLDB(":hsqldb:", SQLDialect.HSQLDB),
	MARIADB(":mariadb:", SQLDialect.MARIADB),
	MYSQL(":mysql:", SQLDialect.MYSQL),
	POSTGRES(":postgresql:", SQLDialect.POSTGRES),
	PGSQL(":pgsql:", SQLDialect.POSTGRES),
	SQLITE(":sqlite:", SQLDialect.SQLITE),
	SQL_DROID(":sqldroid:", SQLDialect.SQLITE);

	private String code;
	private SQLDialect dialect;

	SQLDialectEnums(String code, SQLDialect dialect) {
		this.code = code;
		this.dialect = dialect;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public SQLDialect getDialect() {
		return dialect;
	}

	public void setDialect(SQLDialect dialect) {
		this.dialect = dialect;
	}

	public static SQLDialectEnums getByCode(String codeStr){
		return Arrays.stream(values()).filter(o -> o.code.equals(codeStr)).findFirst().orElse(null);
	}

	public static SQLDialectEnums containsByCode(String codeStr){
		return Arrays.stream(values()).filter(o -> codeStr.contains(o.code)).findFirst().orElse(null);
	}
}
