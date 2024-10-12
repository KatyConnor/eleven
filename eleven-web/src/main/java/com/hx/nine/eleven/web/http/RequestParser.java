package com.hx.nine.eleven.web.http;

//import cn.fh.http.exception.BaseCheckedException;
//import cn.fh.http.exception.MethodNotSupportedException;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  HTTP请求参数解析器, 支持GET, POST
 * @auth wml
 * @date 2024/10/8
 */
public class RequestParser {

	private FullHttpRequest fullReq;

	/**
	 * 构造一个解析器
	 * @param req
	 */
	public RequestParser(FullHttpRequest req) {
		this.fullReq = req;
	}

	/**
	 * 解析请求参数
	 * @return 包含所有请求参数的键值对, 如果没有参数, 则返回空Map
	 *
	 * @throws BaseCheckedException
	 * @throws IOException
	 */
	public Map<String, String> parse() throws  IOException {
		HttpMethod method = fullReq.method();

		Map<String, String> parmMap = new HashMap<>();

		if (HttpMethod.GET == method) {
			// 是GET请求
			QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
			decoder.parameters().entrySet().forEach( entry -> {
				// entry.getValue()是一个List, 只取第一个元素
				parmMap.put(entry.getKey(), entry.getValue().get(0));
			});
		} else if (HttpMethod.POST == method) {
			// 是POST请求
			HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
			decoder.offer(fullReq);

			List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

			for (InterfaceHttpData parm : parmList) {

				Attribute data = (Attribute) parm;
				parmMap.put(data.getName(), data.getValue());
			}

		} else {
			// 不支持其它方法
//			throw new MethodNotSupportedException(""); // 这是个自定义的异常, 可删掉这一行
		}

		return parmMap;
	}
}
