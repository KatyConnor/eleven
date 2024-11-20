package hx.nine.eleven.core.core;

import hx.nine.eleven.core.utils.Assert;

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
