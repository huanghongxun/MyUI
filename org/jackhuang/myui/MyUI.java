/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "MyUI" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.myui;

@Mod(modid = "MyUI", name = "MyUI", version = "1.1")
public class MyUI {
	
	public String version;
	public double versionDouble;
	
	public static Configuration config;

	public String serverURL1 = "", serverURL2 = "", serverURL3 = "", totalURL = "";
	public String announcementURL = "", pictureURL = "", webiteURL = "", contactURL = "";
	public String announcementContent = "", announcementTitle = "";
	public int announcementPictureID;
	public String newestVersion = "", minecraftTitle = "";
	public String qGroupURL = "", updateURL = "", announcementPictureURL = "";
	
	private String getValue(String s) {
		String[] strings = s.split("=");
		if(strings.length < 1) return "";
		else {
			s = "";
			for(int i = 1; i < strings.length - 1; i++)
				s += strings[i] + "=";
			s += strings[strings.length - 1];
			return s;
		}
	}

	@EventHandler
	public void preLoad(FMLPreInitializationEvent event) {
		
		config = new Configuration(event.getSuggestedConfigurationFile());

		init();
		if(StringUtils.isNotBlank(minecraftTitle))
			Display.setTitle(minecraftTitle);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Mod.Instance("MyUI")
	public static MyUI instance;
	
	@ForgeSubscribe
	public void openGui(GuiOpenEvent event) {
		if(event.gui instanceof GuiMainMenu)
			event.gui = new NewMenu();
	}
}
