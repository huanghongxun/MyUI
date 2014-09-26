/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
 */
package org.jackhuang.myui.util;

public class MD5Util {
	
	public static int tryParseInt(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch(Throwable t) {
			return def;
		}
	}
	
	public static double tryParseDouble(String s, double def) {
		try {
			return Double.parseDouble(s);
		} catch(Throwable t) {
			return def;
		}
	}
}