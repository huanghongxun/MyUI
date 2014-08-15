/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "MyUI" is distributed under the MIT License.
 * Please check the contents of the license.
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
