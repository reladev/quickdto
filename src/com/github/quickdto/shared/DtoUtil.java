package com.github.quickdto.shared;

public class DtoUtil {
	public static boolean safeEquals(Object s1, Object s2) {
		if (s1 != null && s2 != null) {
			return s1.equals(s2);

		} else if (s1 == null && s2 == null) {
			return true;

		} else {
			return false;
		}
	}
}
