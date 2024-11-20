package hx.nine.eleven.core.utils;

import java.util.Locale;

public abstract class SystemUtils {

	public static int osName() {
		int result = -1;
		String osName = System.getProperty("os.name");
		String osNameLowerCase = osName.toLowerCase(Locale.ROOT);
		if (osNameLowerCase.startsWith("mac") || osNameLowerCase.startsWith("darwin")) {
			result = 0;
		} else if (osNameLowerCase.startsWith("wind")) {
			result = 1;
		} else if (osNameLowerCase.startsWith("linux")) {
			result = 2;
		}
		return result;
	}
}
