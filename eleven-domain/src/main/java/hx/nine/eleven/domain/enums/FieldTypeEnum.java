package hx.nine.eleven.domain.enums;

import java.util.Arrays;

public enum FieldTypeEnum {
	LIST("java.util.List","List集合"),
	MAP("java.util.Map","Map集合");

	FieldTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	private String code;
	private String name;

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

	public static FieldTypeEnum getByCode(String code){
		return Arrays.stream(values()).filter(o -> o.code.equals(code)).findFirst().orElse(null);
	}
}
