package com.hx.nine.eleven.vertx.handler.servlet;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

public interface ServletHandler {

	Object doRequest(RoutingContext context);

	default void setHeader(RoutingContext context, Map<String,String> headers){
		HttpServerResponse response = context.response();
		if (headers != null && headers.size() > 0){
			headers.forEach((k,v) ->{
				response.putHeader(k,v);
			});
		}
	}

}
