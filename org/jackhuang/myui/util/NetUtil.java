package org.jackhuang.myui.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;

public class NetUtil {
	
	public static String getNetContent(String url) throws IOException {
		URL u = new URL(url);
		return getStreamContent(u.openStream());
	}
	
	public static String getStreamContent(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
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
