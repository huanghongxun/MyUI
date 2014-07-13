package org.jackhuang.myui;

import cpw.mods.fml.client.GuiModList;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import net.minecraft.client.gui.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.mco.ExceptionMcoService;
import net.minecraft.client.mco.GuiScreenClientOutdated;
import net.minecraft.client.mco.McoClient;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraft.world.storage.ISaveFormat;
import net.minecraft.world.storage.WorldInfo;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.jackhuang.myui.MyUI;
import org.jackhuang.myui.util.GLUtil;
import org.jackhuang.myui.util.LoadServerThread;
import org.jackhuang.myui.util.MD5Util;
import org.jackhuang.myui.util.NetUtil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

@SideOnly(Side.CLIENT)
public class NewMenu extends GuiScreen {
	/** The RNG used by the Main Menu Screen. */
	private static final Random rand = new Random();

	/** Counts the number of screen updates. */
	private float updateCounter;

	/** The splash message. */
	// private String splashText = "missingno";
	private GuiButton buttonResetDemo;

	/** Timer used to rotate the panorama, increases every tick. */
	private int panoramaTimer;

	/**
	 * Texture allocated for the current viewport of the main menu's panorama
	 * background.
	 */
	private DynamicTexture viewportTexture;
	private boolean field_96141_q = true;
	private static boolean field_96140_r;
	private static boolean field_96139_s;
	private final Object field_104025_t = new Object();
	private String field_92025_p;
	private String field_104024_v;
	private static final ResourceLocation splashTexts = new ResourceLocation(
			"texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");

	/** An array of all the paths to the panorama pictures. */
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	private String field_96138_a;
	private int field_92024_r;
	private int field_92023_s;
	private int field_92022_t;
	private int field_92021_u;
	private int field_92020_v;
	private int field_92019_w;
	private ResourceLocation field_110351_G;
	private GuiButton minecraftRealmsButton;

	private GuiButton fmlModButton = null;

	/**
	 * Customized buttons
	 */
	private GuiButton server1, server2, server3, website;
	private LoadServerThread thread;

	public NewMenu() {
		BufferedReader bufferedreader = null;
		String s;

		try {
			ArrayList arraylist = new ArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(Minecraft
					.getMinecraft().getResourceManager()
					.getResource(splashTexts).getInputStream(), Charsets.UTF_8));

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					arraylist.add(s);
				}
			}

			/*
			 * do { this.splashText =
			 * (String)arraylist.get(rand.nextInt(arraylist.size())); } while
			 * (this.splashText.hashCode() == 125780783);
			 */
		} catch (IOException ioexception) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException ioexception1) {
					;
				}
			}
		}

		this.updateCounter = rand.nextFloat();
		this.field_92025_p = "";
		String s1 = System.getProperty("os_architecture");
		s = System.getProperty("java_version");

		/*
		 * if ("ppc".equalsIgnoreCase(s1)) { this.field_92025_p = "" +
		 * EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET +
		 * " PowerPC compatibility will be dropped in Minecraft 1.6";
		 * this.field_104024_v = "http://tinyurl.com/javappc"; } else if (s !=
		 * null && s.startsWith("1.5")) { this.field_92025_p = "" +
		 * EnumChatFormatting.BOLD + "Notice!" + EnumChatFormatting.RESET +
		 * " Java 1.5 compatibility will be dropped in Minecraft 1.6";
		 * this.field_104024_v = "http://tinyurl.com/javappc"; }
		 */
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		++this.panoramaTimer;
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Fired when a key is typed. This is the equivalent of
	 * KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager()
				.getDynamicTextureLocation("background", this.viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		/*
		 * if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
		 * this.splashText = "Happy birthday, ez!"; } else if (calendar.get(2) +
		 * 1 == 6 && calendar.get(5) == 1) { this.splashText =
		 * "Happy birthday, Notch!"; } else if (calendar.get(2) + 1 == 12 &&
		 * calendar.get(5) == 24) { this.splashText = "Merry X-mas!"; } else if
		 * (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) { this.splashText
		 * = "Happy new year!"; } else if (calendar.get(2) + 1 == 10 &&
		 * calendar.get(5) == 31) { this.splashText = "OOoooOOOoooo! Spooky!"; }
		 */

		boolean flag = true;
		int i = this.height / 4 + 48;

		/*
		 * if (this.mc.isDemo()) { this.addDemoButtons(i, 24); } else {
		 */
		this.addSingleplayerMultiplayerButtons(i, 24);
		// }*/

		this.addCustomizedButtons(i, 24);

		Object object = this.field_104025_t;

		synchronized (this.field_104025_t) {
			this.field_92023_s = this.fontRenderer
					.getStringWidth(this.field_92025_p);
			this.field_92024_r = this.fontRenderer
					.getStringWidth(field_96138_a);
			// int j = Math.max(this.field_92023_s, this.field_92024_r);
			int j = this.width / 6 * 4;
			this.field_92022_t = (this.width - j) / 2;
			this.field_92021_u = i - 24;// ((GuiButton)this.buttonList.get(0)).yPosition
										// - 24;
			this.field_92020_v = this.field_92022_t + j;
			this.field_92019_w = this.field_92021_u + 24;
		}

		if (MyUI.instance.announcementContent != null) {
			String[] a = MyUI.instance.announcementContent.split("\n");
			this.field_92025_p = a[0];
			if (a.length > 1)
				this.field_96138_a = a[1];
		} else {
			this.field_92025_p = "暂无公告";
		}

		thread = new LoadServerThread(MyUI.instance.serverURL1);
		thread.start();
	}

	private void addCustomizedButtons(int top, int split) {
		int block = this.width / 6;

		// this.buttonList.add(new GuiButton(20, block * 2 + 2, top, block - 2,
		// 20, "自动配对线路"));
		this.buttonList.add(new GuiButton(21, block * 2 + 2, top,
				block * 3 / 2 - 2, 20, "电信入口"));
		this.buttonList.add(new GuiButton(22, block * 7 / 2 + 2, top,
				block * 3 / 2 - 2, 20, "联通入口"));
		this.buttonList.add(new GuiButton(23, block * 2 + 2, top + split,
				block * 3 / 2 - 2, 20, "进入服务器官网"));
		this.buttonList.add(new GuiButton(25, block * 7 / 2 + 2, top + split,
				block * 3 / 2 - 2, 20, "进入Q群"));
		//this.buttonList.add(new GuiButton(24, block * 4 + 2, top + split,
		//		block * 1 - 2, 20, "联系服主"));

		this.buttonList.add(new GuiButton(0, block * 3 + 2, top + 72 + 12,
				block - 2, 20, I18n.getString("menu.options")));
		this.buttonList.add(new GuiButton(4, block * 4 + 2, top + 72 + 12,
				block - 2, 20, I18n.getString("menu.quit")));
		this.buttonList.add(new GuiButtonLanguage(5, block * 2 + 2 - 24,
				top + 72 + 12));

	}

	private void drawAnnouncement() {
		int i = this.height / 4 + 48;
		int block = this.width / 6;

		// drawRect(0, this.height - 50, 100, this.height, 1428160512);
		drawRect(0, 0, this.width, 25, 1428160512);

		String s2 = "", s3 = "  点击此处刷新";

		if (thread != null && thread.online >= 0 && thread.maxplayer >= 0) {
			s2 += "在线人数/最大人数: " + thread.online + "/" + thread.maxplayer;
			s2 += "   延迟: " + thread.offset + "ms";
		} else {
			s2 += "无法连接服务器";
			s2 += "或服务器响应错误";
		}
		drawCenteredString(fontRenderer, s2 + s3, this.width / 2, 15, 16777125);
		// drawString(fontRenderer, s3, this.width -
		// fontRenderer.getStringWidth(s3), 15, 16777125);

		String s = "当前版本为" + EnumChatFormatting.YELLOW + MyUI.instance.version
				+ EnumChatFormatting.RESET + ", 最新版本为"
				+ EnumChatFormatting.GREEN + MyUI.instance.newestVersion
				+ EnumChatFormatting.RESET + ", ";
		if (MD5Util.tryParseDouble(MyUI.instance.newestVersion, 1) > MyUI.instance.versionDouble)
			s += EnumChatFormatting.UNDERLINE + "点击此处"
					+ EnumChatFormatting.RESET + "更新您的客户端";
		else
			s += "您的客户端为最新, 无需更新";
		drawCenteredString(fontRenderer, s, this.width / 2, 3, 14737632);

		drawRect(this.field_92022_t - 2, i, block * 2, i + 24 * 3 - 4, 1428160512);

		drawCenteredString(fontRenderer, MyUI.instance.announcementTitle,
				(block + 2) / 2 + block, i + 24 * 3, 16777125);

		BufferedImage image = GLUtil.loadImage("announcement_picture.png");
		if (image != null) {
			GLUtil.loadTexture(image);
			GLUtil.drawTexture(this.field_92022_t - 2, i, block * 2, i + 24 * 3 - 4);
		}
		// GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		/*
		 * GL11.glColor3f(255, 255, 255); GL11.glBegin(GL11.GL_QUADS);
		 * GL11.glTexCoord2d(0, 0); GL11.glVertex2i(0, 0); GL11.glTexCoord2d(1,
		 * 0); GL11.glVertex2i(0, 200); GL11.glTexCoord2d(1, 1);
		 * GL11.glVertex2i(200, 200); GL11.glTexCoord2d(0, 1);
		 * GL11.glVertex2i(200, 0); GL11.glEnd();
		 */
	}

	/**
	 * Adds Singleplayer and Multiplayer buttons on Main Menu for players who
	 * have bought the game.
	 */
	private void addSingleplayerMultiplayerButtons(int par1, int par2) {
		int block = this.width / 6;

		this.buttonList.add(new GuiButton(1, block * 2 + 2, par1 + 72 + 12,
				block - 2, 20, I18n.getString("menu.singleplayer")));
		// this.buttonList.add(new GuiButton(2, block*2+2, par1 + par2 * 2,
		// block-2, 20, I18n.getString("menu.multiplayer")));
		// If Minecraft Realms is enabled, halve the size of both buttons and
		// set them next to eachother.
		fmlModButton = new GuiButton(6, block * 2 + 2, par1 + par2 * 2,
				block * 3 - 2, 20, "模组");
		this.buttonList.add(fmlModButton);

		/*
		 * minecraftRealmsButton = new GuiButton(14, this.width / 2 - 100, par1
		 * + par2 * 2, I18n.getString("menu.online"));
		 * minecraftRealmsButton.width = 98; minecraftRealmsButton.xPosition =
		 * this.width / 2 - 100; this.buttonList.add(minecraftRealmsButton);
		 * this.minecraftRealmsButton.drawButton = false;
		 */
	}

	/**
	 * Adds Demo buttons on Main Menu for players who are playing Demo.
	 */
	/*
	 * private void addDemoButtons(int par1, int par2) { this.buttonList.add(new
	 * GuiButton(11, this.width / 2 - 100, par1,
	 * I18n.getString("menu.playdemo")));
	 * this.buttonList.add(this.buttonResetDemo = new GuiButton(12, this.width /
	 * 2 - 100, par1 + par2 * 1, I18n.getString("menu.resetdemo"))); ISaveFormat
	 * isaveformat = this.mc.getSaveLoader(); WorldInfo worldinfo =
	 * isaveformat.getWorldInfo("Demo_World");
	 * 
	 * if (worldinfo == null) { this.buttonResetDemo.enabled = false; } }
	 */

	public static void connectToServer(GuiScreen parent, String serverURL) {
		Minecraft.getMinecraft().displayGuiScreen(
				new GuiConnecting(parent, Minecraft.getMinecraft(),
						new ServerData("Server", serverURL)));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of
	 * ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (par1GuiButton.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (par1GuiButton.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this,
					this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (par1GuiButton.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (par1GuiButton.id == 2) {
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}

		if (par1GuiButton.id == 14 && this.minecraftRealmsButton.drawButton) {
			this.func_140005_i();
		}

		if (par1GuiButton.id == 4) {
			this.mc.shutdown();
		}

		if (par1GuiButton.id == 6) {
			this.mc.displayGuiScreen(new GuiModList(this));
		}

		if (par1GuiButton.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World",
					DemoWorldServer.demoWorldSettings);
		}

		if (par1GuiButton.id == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");

			if (worldinfo != null) {
				GuiYesNo guiyesno = GuiSelectWorld.getDeleteWorldScreen(this,
						worldinfo.getWorldName(), 12);
				this.mc.displayGuiScreen(guiyesno);
			}
		}
		String url = null;
		boolean b = false;
		if (par1GuiButton.id == 13) {
			url = this.field_104024_v;
		} else if (par1GuiButton.id == 23) {
			url = MyUI.instance.webiteURL;
		} else if (par1GuiButton.id == 24) {
			url = MyUI.instance.contactURL;
		} else if (par1GuiButton.id == 25) {
			url = MyUI.instance.qGroupURL;
		}
		if (url != null) {
			try {
				java.awt.Desktop.getDesktop().browse(new URI(url));
			} catch (Throwable e) {
			}
		}
		if (url != null)
			this.mc.displayGuiScreen(null);

		if (par1GuiButton.id == 20) {
			connectToServer(this, MyUI.instance.serverURL1);
		}

		if (par1GuiButton.id == 21) {
			connectToServer(this, MyUI.instance.serverURL2);
		}

		if (par1GuiButton.id == 22) {
			connectToServer(this, MyUI.instance.serverURL3);
		}
	}

	private void func_140005_i() {
		McoClient mcoclient = new McoClient(this.mc.getSession());

		try {
			if (mcoclient.func_140054_c().booleanValue()) {
				this.mc.displayGuiScreen(new GuiScreenClientOutdated(this));
			} else {
				this.mc.displayGuiScreen(new GuiScreenOnlineServers(this));
			}
		} catch (ExceptionMcoService exceptionmcoservice) {
			this.mc.getLogAgent().logSevere(exceptionmcoservice.toString());
		} catch (IOException ioexception) {
			this.mc.getLogAgent().logSevere(ioexception.getLocalizedMessage());
		}
	}

	public void confirmClicked(boolean par1, int par2) {
		if (par1 && par2 == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		}
	}

	/**
	 * Draws the main menu panorama
	 */
	private void drawPanorama(int par1, int par2, float par3) {
		Tessellator tessellator = Tessellator.instance;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		Project.gluPerspective(120.0F, 1.0F, 0.05F, 10.0F);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		byte b0 = 8;

		for (int k = 0; k < b0 * b0; ++k) {
			GL11.glPushMatrix();
			float f1 = ((float) (k % b0) / (float) b0 - 0.5F) / 64.0F;
			float f2 = ((float) (k / b0) / (float) b0 - 0.5F) / 64.0F;
			float f3 = 0.0F;
			GL11.glTranslatef(f1, f2, f3);
			GL11.glRotatef(
					MathHelper
							.sin(((float) this.panoramaTimer + par3) / 400.0F) * 25.0F + 20.0F,
					1.0F, 0.0F, 0.0F);
			GL11.glRotatef(-((float) this.panoramaTimer + par3) * 0.1F, 0.0F,
					1.0F, 0.0F);

			for (int l = 0; l < 6; ++l) {
				GL11.glPushMatrix();

				if (l == 1) {
					GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 2) {
					GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 3) {
					GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
				}

				if (l == 4) {
					GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
				}

				if (l == 5) {
					GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
				}

				this.mc.getTextureManager().bindTexture(titlePanoramaPaths[l]);
				tessellator.startDrawingQuads();
				tessellator.setColorRGBA_I(16777215, 255 / (k + 1));
				float f4 = 0.0F;
				tessellator.addVertexWithUV(-1.0D, -1.0D, 1.0D,
						(double) (0.0F + f4), (double) (0.0F + f4));
				tessellator.addVertexWithUV(1.0D, -1.0D, 1.0D,
						(double) (1.0F - f4), (double) (0.0F + f4));
				tessellator.addVertexWithUV(1.0D, 1.0D, 1.0D,
						(double) (1.0F - f4), (double) (1.0F - f4));
				tessellator.addVertexWithUV(-1.0D, 1.0D, 1.0D,
						(double) (0.0F + f4), (double) (1.0F - f4));
				tessellator.draw();
				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();
			GL11.glColorMask(true, true, true, false);
		}

		tessellator.setTranslation(0.0D, 0.0D, 0.0D);
		GL11.glColorMask(true, true, true, true);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
		GL11.glDepthMask(true);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	/**
	 * Rotate and blurs the skybox view in the main menu
	 */
	private void rotateAndBlurSkybox(float par1) {
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		byte b0 = 3;

		for (int i = 0; i < b0; ++i) {
			tessellator
					.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float) (i + 1));
			int j = this.width;
			int k = this.height;
			float f1 = (float) (i - b0 / 2) / 256.0F;
			tessellator.addVertexWithUV((double) j, (double) k,
					(double) this.zLevel, (double) (0.0F + f1), 0.0D);
			tessellator.addVertexWithUV((double) j, 0.0D, (double) this.zLevel,
					(double) (1.0F + f1), 0.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel,
					(double) (1.0F + f1), 1.0D);
			tessellator.addVertexWithUV(0.0D, (double) k, (double) this.zLevel,
					(double) (0.0F + f1), 1.0D);
		}

		tessellator.draw();
		GL11.glColorMask(true, true, true, true);
	}

	/**
	 * Renders the skybox in the main menu
	 */
	private void renderSkybox(int par1, int par2, float par3) {
		GL11.glViewport(0, 0, 256, 256);
		this.drawPanorama(par1, par2, par3);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f1 = this.width > this.height ? 120.0F / (float) this.width
				: 120.0F / (float) this.height;
		float f2 = (float) this.height * f1 / 256.0F;
		float f3 = (float) this.width * f1 / 256.0F;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		tessellator.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F);
		int k = this.width;
		int l = this.height;
		tessellator.addVertexWithUV(0.0D, (double) l, (double) this.zLevel,
				(double) (0.5F - f2), (double) (0.5F + f3));
		tessellator.addVertexWithUV((double) k, (double) l,
				(double) this.zLevel, (double) (0.5F - f2),
				(double) (0.5F - f3));
		tessellator.addVertexWithUV((double) k, 0.0D, (double) this.zLevel,
				(double) (0.5F + f2), (double) (0.5F - f3));
		tessellator.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel,
				(double) (0.5F + f2), (double) (0.5F + f3));
		tessellator.draw();
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		this.renderSkybox(par1, par2, par3);
		Tessellator tessellator = Tessellator.instance;
		short short1 = 274;
		int k = this.width / 2 - short1 / 2;
		byte b0 = 30;
		this.drawGradientRect(0, 0, this.width, this.height, -2130706433,
				16777215);
		this.drawGradientRect(0, 0, this.width, this.height, 0,
				Integer.MIN_VALUE);
		this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		if ((double) this.updateCounter < 1.0E-4D) {
			this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 99, 44);
			this.drawTexturedModalRect(k + 99, b0 + 0, 129, 0, 27, 44);
			this.drawTexturedModalRect(k + 99 + 26, b0 + 0, 126, 0, 3, 44);
			this.drawTexturedModalRect(k + 99 + 26 + 3, b0 + 0, 99, 0, 26, 44);
			this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
		} else {
			this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 155, 44);
			this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
		}

		drawAnnouncement();

		tessellator.setColorOpaque_I(16777215);
		// GL11.glPushMatrix();
		// GL11.glTranslatef((float)(this.width / 2 + 90), 70.0F, 0.0F);
		// GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		// float f1 = 1.8F -
		// MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() %
		// 1000L) / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
		// f1 = f1 * 100.0F /
		// (float)(this.fontRenderer.getStringWidth(this.splashText) + 32);
		// GL11.glScalef(f1, f1, f1);
		// this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8,
		// 16776960);
		// GL11.glPopMatrix();

		String s = "Minecraft 1.6.2";

		if (this.mc.isDemo()) {
			s = s + " Demo";
		}

		List<String> brandings = //Lists.reverse(FMLCommonHandler.instance().getBrandings());
				ImmutableList.<String>of("MCP, Minecraft Forge, FML, Optifine", s);
		for (int i = 0; i < brandings.size(); i++) {
			String brd = brandings.get(i);
			if (!Strings.isNullOrEmpty(brd)) {
				this.drawString(this.fontRenderer, brd, 2, this.height
						- (10 + i * (this.fontRenderer.FONT_HEIGHT + 1)),
						16777215);
			}
		}

		String s1 = "Copyright Mojang AB. Do not distribute!";
		String s2 = "Copyright huangyuhui. All Rights Reserved. Click here for about.";
		this.drawString(this.fontRenderer, s1,
				this.width - this.fontRenderer.getStringWidth(s1) - 2,
				this.height - 10, 16777215);
		this.drawString(this.fontRenderer, s2,
				this.width - this.fontRenderer.getStringWidth(s2) - 2,
				this.height - 20, 16777215);

		if (StringUtils.isNotBlank(this.field_92025_p)) {
			drawRect(this.field_92022_t - 2, this.field_92021_u - 2,
					this.field_92020_v + 2, this.field_92019_w - 1, 1428160512);
			// this.drawCenteredString(this.fontRenderer, this.field_92025_p,
			// this.field_92022_t, this.field_92021_u, 16777215);
			// this.drawCenteredString(this.fontRenderer, field_96138_a,
			// (this.width - this.field_92024_r) / 2,
			// ((GuiButton)this.buttonList.get(0)).yPosition - 12, 16777215);
			this.drawCenteredString(this.fontRenderer, this.field_92025_p,
					this.width / 2, this.field_92021_u, 16777215);
			this.drawCenteredString(this.fontRenderer, field_96138_a,
					this.width / 2, this.field_92021_u + 12, 16777215);
		}

		super.drawScreen(par1, par2, par3);
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		/*
		 * Object object = this.field_104025_t;
		 * 
		 * synchronized (this.field_104025_t) { if (this.field_92025_p.length()
		 * > 0 && par1 >= this.field_92022_t && par1 <= this.field_92020_v &&
		 * par2 >= this.field_92021_u && par2 <= this.field_92019_w) {
		 * GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(this,
		 * this.field_104024_v, 13, true); guiconfirmopenlink.func_92026_h();
		 * this.mc.displayGuiScreen(guiconfirmopenlink); } }
		 */

		if (15 <= par2 && par2 < 25)
			thread.run();
		if (par2 < 15) {
			try {
				java.awt.Desktop.getDesktop().browse(
						new URI(MyUI.instance.updateURL));
			} catch (Throwable t) {
			}
		}

		int block = width / 6;
		int i = this.height / 4 + 48;
		if ((block - 2) <= par1 && par1 <= (block * 2) && i <= par2
				&& par2 <= (i + 24 * 3 - 4)) {
			try {
				java.awt.Desktop.getDesktop().browse(
						new URI(MyUI.instance.announcementPictureURL));
			} catch (Throwable t) {
			}
		}

		if (par2 > this.height - 20 && par2 < this.height - 10
				&& par1 > this.width - 50) {
			mc.displayGuiScreen(new CopyrightScreen(this));
		}
	}
}
