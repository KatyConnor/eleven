package com.hx.vertx.boot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class UUIDGenerator {

	private static final int[] intNumber = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String temp = uuid.toString().replaceAll("-", "");
		return temp;
	}

	public static String getUUID(String name) {
		UUID uuid = UUID.fromString(name);
		String temp = uuid.toString().replaceAll("-", "");
		return temp;
	}

	public static String getUUID(byte[] name) {
		UUID uuid = UUID.nameUUIDFromBytes(name);
		String temp = uuid.toString().replaceAll("-", "");
		return temp;
	}

	public static String getUUIDNumber() {
		Long current = System.currentTimeMillis();
		CharSequence charSequence = new StringBuffer(System.currentTimeMillis() + getUUID() + "");
		int sequence = charSequence.hashCode();
		String str = getTime() + Long.toString((long)(Math.random() * 9.223372036854776E18D), 64) + current + sequence;
		long length = (long)str.length();
		StringBuffer stringBuffer = new StringBuffer();
		if (length < 64L) {
			long difference = 64L - length;

			for(int i = 0; (long)i < difference; ++i) {
				String random = Long.toString((long)(Math.random() * 9.223372036854776E18D)).substring(0, 1);
				stringBuffer.append(intNumber[Integer.valueOf(random)]);
			}
		}

		return str + stringBuffer.toString();
	}

	private static String getTime() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		return simpleDateFormat.format(new Date());
	}
}
