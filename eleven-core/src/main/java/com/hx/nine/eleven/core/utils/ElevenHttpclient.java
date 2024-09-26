package com.hx.nine.eleven.core.utils;

import io.vertx.core.http.HttpClient;

public class ElevenHttpclient {

	private HttpClient httpClient;

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public static ElevenHttpclient build(){
		return Single.INSTANCE;
	}

	private final static class Single{
		private final static ElevenHttpclient INSTANCE = new ElevenHttpclient();
	}
}
