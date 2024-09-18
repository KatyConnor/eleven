package com.hx.vertx.boot.web;

import java.util.Map;

/**
 * 添加HTTP请求头
 * @author wml
 * @date 2023-03-27
 */
public interface HttpHeader {

	HttpHeader addHeader(String name,String value);

	Map<String,String> getHeaders();
}
