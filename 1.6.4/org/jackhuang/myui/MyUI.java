/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
 */
package org.jackhuang.myui;

@Mod(modid = "MyUI", name = "MyUI", version = "1.1")
public class MyUI {

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
