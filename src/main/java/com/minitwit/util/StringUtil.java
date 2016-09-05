package com.minitwit.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {

	@FunctionalInterface
	interface IWhenInt {
		void apply(int integer);
	}
	@FunctionalInterface
	interface IWhenNotInt {
		void apply();
	}
	public static void isInt(
			String str,
			IWhenInt doWhenInt,
			IWhenNotInt doWhenNotInt) {

		if (isInteger(str, 10)) {
			doWhenInt.apply(Integer.parseInt(str));
		} else {
			doWhenNotInt.apply();
		}
	}

	public static boolean isInt(String str) {
		return isInteger(str, 10);
	}

	private static boolean isInteger(String s, int radix) {
		if(s==null || s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) {
			if(i == 0 && s.charAt(i) == '-') {
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}
}
