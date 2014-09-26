/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
 */
package org.jackhuang.myui.util;

public class NetUtil {
	
	public static String getNetContent(String url) {
		URL u = new URL(url);
		return getStreamContent(u.openStream());
	}
	
	public static String getStreamContent(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String c = "", line;
		while((line = reader.readLine()) != null)
			c += "\n" + line;
		return c.substring(1);
	}
	
	public static void writeToStream(OutputStream os, String content) {
		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(content);
		writer.flush(); writer.close();
	}
	
	public static String post(String url, String content) {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		c.setDoOutput(true);
		writeToStream(c.getOutputStream(), content);
		return getStreamContent(c.getInputStream());
	}

}
