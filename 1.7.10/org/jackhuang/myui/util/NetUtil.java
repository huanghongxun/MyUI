/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "MyUI" is distributed under the MIT License.
 * Please check the contents of the license.
 */
package org.jackhuang.myui.util;

public class NetUtil {
	
	public static String getNetContent(String url) throws IOException {
		URL u = new URL(url);
		return getStreamContent(u.openStream());
	}
	
	public static String getStreamContent(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String c = "", line;
		while((line = reader.readLine()) != null)
			c += "\n" + line;
		return c.substring(1);
	}
	
	public static void writeToStream(OutputStream os, String content) throws IOException {
		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(content);
		writer.flush(); writer.close();
	}
	
	public static String post(String url, String content) throws IOException {
		URL u = new URL(url);
		URLConnection c = u.openConnection();
		c.setDoOutput(true);
		writeToStream(c.getOutputStream(), content);
		return getStreamContent(c.getInputStream());
	}

}
