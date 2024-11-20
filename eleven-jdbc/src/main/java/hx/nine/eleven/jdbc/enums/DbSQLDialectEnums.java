package hx.nine.eleven.jdbc.enums;

import java.util.Arrays;

/**
 * 数据库类型
 */
public enum DbSQLDialectEnums {

	MYSQL("mysql", "MySql 数据库"),
	MARIADB("mariadb", "MariaDB 数据库"),
	ORACLE("oracle", "Oracle11g 及以下数据库"),
	ORACLE_12C("oracle12c", "Oracle12c 及以上数据库"),
	DB2("db2", "DB2 数据库"),
	DB2_1005("db2_1005", "DB2 10.5版本数据库"),
	H2("h2", "H2 数据库"),
	HSQL("hsql", "HSQL 数据库"),
	SQLITE("sqlite", "SQLite 数据库"),
	POSTGRE_SQL("postgresql", "PostgreSQL 数据库"),
	SQLSERVER("sqlserver", "SQLServer 数据库"),
	SQLSERVER_2005("sqlserver_2005", "SQLServer 数据库"),
	DM("dm", "达梦数据库"),
	XUGU("xugu", "虚谷数据库"),
	KINGBASE_ES("kingbasees", "人大金仓数据库"),
	PHOENIX("phoenix", "Phoenix HBase 数据库"),
	GAUSS("gauss", "Gauss 数据库"),
	CLICK_HOUSE("clickhouse", "clickhouse 数据库"),
	GBASE("gbase", "南大通用(华库)数据库"),
	GBASE_8S("gbase-8s", "南大通用数据库 GBase 8s"),
	OSCAR("oscar", "神通数据库"),
	SYBASE("sybase", "Sybase ASE 数据库"),
	OCEAN_BASE("oceanbase", "OceanBase 数据库"),
	FIREBIRD("Firebird", "Firebird 数据库"),
	DERBY("derby", "Derby 数据库"),
	HIGH_GO("highgo", "瀚高数据库"),
	CUBRID("cubrid", "CUBRID 数据库"),
	GOLDILOCKS("goldilocks", "GOLDILOCKS 数据库"),
	CSIIDB("csiidb", "CSIIDB 数据库"),
	SAP_HANA("hana", "SAP_HANA 数据库"),
	IMPALA("impala", "impala 数据库"),
	VERTICA("vertica", "vertica数据库"),
	XCloud("xcloud", "行云数据库"),
	REDSHIFT("redshift", "亚马逊 redshift 数据库"),
	OPENGAUSS("openGauss", "华为 openGauss 数据库"),
	TDENGINE("TDengine", "TDengine 数据库"),
	INFORMIX("informix", "Informix 数据库"),
	SINODB("sinodb", "SinoDB 数据库"),
	UXDB("uxdb", "优炫数据库"),
	GREENPLUM("greenplum", "greenplum 数据库"),
	LEALONE("lealone", "lealone 数据库"),
	HIVE("Hive", "Hive SQL"),
	DORIS("doris", "doris 数据库"),
	OTHER("other", "其他数据库");

	private String code;
	private String name;

	DbSQLDialectEnums(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static DbSQLDialectEnums getByCode(String code){
		return Arrays.stream(values()).filter(o -> o.code.equals(code)).findFirst().orElse(null);
	}

	public static DbSQLDialectEnums containsByCode(String code){
		return Arrays.stream(values()).filter(o -> code.contains(o.code)).findFirst().orElse(null);
	}
}
