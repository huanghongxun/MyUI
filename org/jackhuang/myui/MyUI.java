package org.jackhuang.myui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;
import org.jackhuang.myui.util.HttpDownloader;
import org.jackhuang.myui.util.MD5Util;
import org.jackhuang.myui.util.NetUtil;
import org.lwjgl.opengl.Display;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Session;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.Property;
import net.minecraftforge.event.ForgeSubscribe;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.asm.transformers.PatchingTransformer;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.server.FMLServerHandler;

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
