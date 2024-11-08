package com.hx.nine.eleven.commons.utils;

import java.util.Locale;

/**
 * @auth wml
 * @date 2024/11/8
 */
public class SystemUtils {

	public static int osName() {
		int result = -1;
		String osName = System.getProperty("os.name");
		String osNameLowerCase = osName.toLowerCase(Locale.ROOT);
		if (!osNameLowerCase.startsWith("mac") && !osNameLowerCase.startsWith("darwin")) {
			if (osNameLowerCase.startsWith("wind")) {
				result = 1;
			} else if (osNameLowerCase.startsWith("linux")) {
				result = 2;
			}
		} else {
			result = 0;
		}

		return result;
	}
}
