package hx.nine.eleven.core.enums;

/**
 * HTTP 请求method类型
 * @auth wml
 * @date 2024/11/6
 */
public enum HttpMethodEnum {
	METHOD_DELETE("DELETE"),
	METHOD_HEAD("HEAD"),
	METHOD_GET("GET"),
	METHOD_OPTIONS("OPTIONS"),
	METHOD_POST("POST"),
	METHOD_PUT("PUT"),
	METHOD_TRACE("TRACE");

	private String code;

	HttpMethodEnum(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
