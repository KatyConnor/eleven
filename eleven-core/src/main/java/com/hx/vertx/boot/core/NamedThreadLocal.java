package com.hx.vertx.boot.core;

import com.hx.vertx.boot.utils.Assert;

public class NamedThreadLocal<T> extends ThreadLocal<T>{

	private final String name;

	public NamedThreadLocal(String name) {
		Assert.hasText(name, "Name must not be empty");
		this.name = name;
	}

	public String toString() {
		return this.name;
	}
}
