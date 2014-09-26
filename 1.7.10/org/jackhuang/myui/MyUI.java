/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
 */
package org.jackhuang.myui;


@Mod(modid = "MyUI", name = "MyUI", version = "1.1", guiFactory = "org.jackhuang.myui.client.MyUIModGuiFactory")
public class MyUI {

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		MinecraftForge.EVENT_BUS.register(new NewMenuHandler());
	}

	@Mod.Instance("MyUI")
	public static MyUI instance;
}
