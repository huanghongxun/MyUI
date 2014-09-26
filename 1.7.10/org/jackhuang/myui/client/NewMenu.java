/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
 */
package org.jackhuang.myui.client;

public class NewMenu extends GuiScreen {
	private static final AtomicInteger field_146973_f = new AtomicInteger(0);
	private static final Logger logger = LogManager.getLogger();
	private static final Random rand = new Random();
	private float updateCounter;
	private String splashText;
	private GuiButton buttonResetDemo;
	private int panoramaTimer;
	private DynamicTexture viewportTexture;
	private boolean field_96141_q = true;
	private static boolean field_96140_r;
	private static boolean field_96139_s;
	private final Object field_104025_t = new Object();
	private String outdate_first;
	private String outdate_second;
	private String outdate_link;
	private static final ResourceLocation splashTexts = new ResourceLocation(
			"texts/splashes.txt");
	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation(
			"textures/gui/title/minecraft.png");
	private static final ResourceLocation[] titlePanoramaPaths = new ResourceLocation[] {
			new ResourceLocation("textures/gui/title/background/panorama_0.png"),
			new ResourceLocation("textures/gui/title/background/panorama_1.png"),
			new ResourceLocation("textures/gui/title/background/panorama_2.png"),
			new ResourceLocation("textures/gui/title/background/panorama_3.png"),
			new ResourceLocation("textures/gui/title/background/panorama_4.png"),
			new ResourceLocation("textures/gui/title/background/panorama_5.png") };
	public static final String outdate_default = "Please click "
			+ EnumChatFormatting.UNDERLINE + "here" + EnumChatFormatting.RESET
			+ " for more information.";
	private int outdate_second_width;
	private int outdate_first_width;
	private int outdate_l;
	private int outdate_b;
	private int outdate_r;
	private int outdate_t;
	private String username=null;
	private String player=null;
	private ResourceLocation field_110351_G;
	private GuiButton minecraftRealmsButton;
	private static final String __OBFID = "CL_00001154";

	private GuiButton fmlModButton = null;
	private GuiButton multiplayerButton=null;
	private static double version = 1.0;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NewMenu() {
		this.outdate_second = outdate_default;
		this.splashText = "missingno";
		BufferedReader bufferedreader = null;

		try {
			ArrayList arraylist = new ArrayList();
			bufferedreader = new BufferedReader(new InputStreamReader(Minecraft
					.getMinecraft().getResourceManager()
					.getResource(splashTexts).getInputStream(), Charsets.UTF_8));
			String s;

			while ((s = bufferedreader.readLine()) != null) {
				s = s.trim();

				if (!s.isEmpty()) {
					arraylist.add(s);
				}
			}

			if (!arraylist.isEmpty()) {
				do {
					this.splashText = (String) arraylist.get(rand
							.nextInt(arraylist.size()));
				} while (this.splashText.hashCode() == 125780783);
			}
		} catch (IOException ioexception1) {
			;
		} finally {
			if (bufferedreader != null) {
				try {
					bufferedreader.close();
				} catch (IOException ioexception) {
					;
				}
			}
		}
	}

	public void updateScreen() {
		++this.panoramaTimer;
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void initGui() {
		this.viewportTexture = new DynamicTexture(256, 256);
		this.field_110351_G = this.mc.getTextureManager()
				.getDynamicTextureLocation("background", this.viewportTexture);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());

		if (calendar.get(2) + 1 == 11 && calendar.get(5) == 9) {
			this.splashText = "Happy birthday, ez!";
		} else if (calendar.get(2) + 1 == 6 && calendar.get(5) == 1) {
			this.splashText = "Happy birthday, Notch!";
		} else if (calendar.get(2) + 1 == 12 && calendar.get(5) == 24) {
			this.splashText = "Merry X-mas!";
		} else if (calendar.get(2) + 1 == 1 && calendar.get(5) == 1) {
			this.splashText = "Happy new year!";
		} else if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31) {
			this.splashText = "OOoooOOOoooo! Spooky!";
		} else if (calendar.get(2) + 1 == 5 && calendar.get(5) == 15) {
			this.splashText = "Happy Birthday, Brian!";
		}

		boolean flag = true;
		int i = this.height / 4 + 48;
		int split = 24;

		if (this.mc.isDemo()) {
		} else {
			this.addSingleplayerMultiplayerButtons(i, split);
		}
		
		this.addCustomizedButtons(i, 24);

		loginframe_l = 114;
		loginframe_r = this.width / 4*3 - 2;
		
		Object object = this.field_104025_t;

		synchronized (this.field_104025_t) {
			this.outdate_first_width = this.fontRendererObj
					.getStringWidth(this.outdate_first);
			this.outdate_second_width = this.fontRendererObj
					.getStringWidth(this.outdate_second);
			int j = this.width / 6 * 4;
			this.outdate_l = (this.width - j) / 2;
			this.outdate_b = i - 24;
			this.outdate_r = this.outdate_l + j;
			this.outdate_t = this.outdate_b + 24;
		}

		if (MyUI.instance.announcementContent != null) {
			String[] a = MyUI.instance.announcementContent.split("\n");
			this.outdate_first = a[0];
			if (a.length > 1)
				this.outdate_second = a[1];
		} else {
			this.outdate_first = "暂无公告";
		}

		thread = new LoadServerThread(MyUI.instance.serverURL1);
		thread.func_147224_a(MyUI.instance.serverURL1);
	}

	private void addCustomizedButtons(int top, int split) {
		int block = this.width / 6;

		this.buttonList.add(new GuiButton(20, block * 2 + 2, top, block - 2,
				20, "自动配对线路"));
		this.buttonList.add(new GuiButton(21, block * 3 + 2, top, block - 2,
				20, "电信入口"));
		this.buttonList.add(new GuiButton(22, block * 4 + 2, top, block - 2,
				20, "联通入口"));
		this.buttonList.add(new GuiButton(23, block * 2 + 2, top + split,
				block * 1 - 2, 20, "进入官网"));
		this.buttonList.add(new GuiButton(25, block * 3 + 2, top + split,
				block * 1 - 2, 20, "进入Q群"));
		this.buttonList.add(new GuiButton(24, block * 4 + 2, top + split,
				block * 1 - 2, 20, "联系服主"));

		this.buttonList.add(new GuiButton(0, block * 3 + 2, top + 72 + 12,
				block - 2, 20, I18n.format("menu.options")));
		this.buttonList.add(new GuiButton(4, block * 4 + 2, top + 72 + 12,
				block - 2, 20, I18n.format("menu.quit")));
		this.buttonList.add(new GuiButtonLanguage(5, block * 2 + 2 - 24,
				top + 72 + 12));

	}

	private void drawAnnouncement() {
		if(thread != null && thread.isLoading)
			thread.processReceivedMessages();
		
		int i = this.height / 4 + 48;
		int block = this.width / 6;

		drawRect(0, 0, this.width, 25, 1428160512);

		String s2 = "", s3 = "点击此处刷新";

		if (thread != null && thread.online >= 0 && thread.maxplayer >= 0) {
			s2 += "在线人数/最大人数: " + thread.online + "/" + thread.maxplayer;
			s2 += "   延迟: " + thread.offset + "ms";
		} else if(thread != null && thread.isLoading) {
			s2 += "加载中"; s3 = "点击此处取消";
		} else {
			s2 += EnumChatFormatting.YELLOW + "无法连接服务器、服务器响应错误或请求被取消";
		}
		drawCenteredString(fontRendererObj, s2 + "  " + EnumChatFormatting.UNDERLINE + s3 + EnumChatFormatting.RESET, this.width / 2, 15, 14737632);

		String s = "当前版本为" + EnumChatFormatting.YELLOW + MyUI.instance.version
				+ EnumChatFormatting.RESET + ", 最新版本为"
				+ EnumChatFormatting.GREEN + MyUI.instance.newestVersion
				+ EnumChatFormatting.RESET + ", ";
		if (MD5Util.tryParseDouble(MyUI.instance.newestVersion, 1) > MyUI.instance.versionDouble)
			s += EnumChatFormatting.UNDERLINE + "点击此处"
					+ EnumChatFormatting.RESET + "更新您的客户端";
		else
			s += "您的客户端为最新, 无需更新";
		drawCenteredString(fontRendererObj, s, this.width / 2, 3, 14737632);

		drawRect(this.outdate_l - 2, i, block * 2, i + 24 * 3 - 4, 1428160512);

		drawCenteredString(fontRendererObj, MyUI.instance.announcementTitle,
				(block + 2) / 2 + block, i + 24 * 3, 14737632);

		BufferedImage image = GLUtil.loadImage("announcement_picture.png");
		if (image != null) {
			GLUtil.loadTexture(image);
			GLUtil.drawTexture(this.outdate_l - 2, i, block * 2, i + 24 * 3 - 4);
		}
	}

	@SuppressWarnings("unchecked")
	private void addSingleplayerMultiplayerButtons(int par1, int par2) {
		int block = this.width / 6;

		this.buttonList.add(new GuiButton(1, block * 2 + 2, par1 + 72 + 12,
				block - 2, 20, I18n.format("menu.singleplayer")));
		fmlModButton = new GuiButton(6, block * 2 + 2, par1 + par2 * 2,
				block * 3 - 2, 20, "模组");
		this.buttonList.add(fmlModButton);
	}

	protected void actionPerformed(GuiButton p_146284_1_) {
		if (p_146284_1_.id == 0) {
			this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
		}

		if (p_146284_1_.id == 5) {
			this.mc.displayGuiScreen(new GuiLanguage(this,
					this.mc.gameSettings, this.mc.getLanguageManager()));
		}

		if (p_146284_1_.id == 1) {
			this.mc.displayGuiScreen(new GuiSelectWorld(this));
		}

		if (p_146284_1_.id == 4) {
			this.mc.shutdown();
		}

		if (p_146284_1_.id == 6) {
			this.mc.displayGuiScreen(new GuiModList(this));
		}

		if (p_146284_1_.id == 11) {
			this.mc.launchIntegratedServer("Demo_World", "Demo_World",
					DemoWorldServer.demoWorldSettings);
		}
		
		String url = null;
		boolean b = false;
		if (p_146284_1_.id == 13) {
			openLink(this.outdate_link);
		} else if (p_146284_1_.id == 23) {
			openLink(MyUI.instance.webiteURL);
		} else if (p_146284_1_.id == 24) {
			openLink(MyUI.instance.contactURL);
		} else if (p_146284_1_.id == 25) {
			openLink(MyUI.instance.qGroupURL);
		}

		if (p_146284_1_.id == 20) {
			FMLClientHandler.instance().setupServerList();
			FMLClientHandler.instance().connectToServer(this,
					new ServerData("", MyUI.instance.serverURL1));
		}

		if (p_146284_1_.id == 21) {
			FMLClientHandler.instance().setupServerList();
			FMLClientHandler.instance().connectToServer(this,
					new ServerData("", MyUI.instance.serverURL2));
		}

		if (p_146284_1_.id == 22) {
			FMLClientHandler.instance().setupServerList();
			FMLClientHandler.instance().connectToServer(this,
					new ServerData("", MyUI.instance.serverURL3));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void confirmClicked(boolean par1, int par2) {
		if (par1 && par2 == 12) {
			ISaveFormat isaveformat = this.mc.getSaveLoader();
			isaveformat.flushCache();
			isaveformat.deleteWorldDirectory("Demo_World");
			this.mc.displayGuiScreen(this);
		} else if (par2 == 13) {
			if (par1) {
				try {
					Class oclass = Class.forName("java.awt.Desktop");
					Object object = oclass
							.getMethod("getDesktop", new Class[0]).invoke(
									(Object) null, new Object[0]);
					oclass.getMethod("browse", new Class[] { URI.class })
							.invoke(object,
									new Object[] { new URI(this.outdate_link) });
				} catch (Throwable throwable) {
					logger.error("Couldn\'t open link", throwable);
				}
			}

			this.mc.displayGuiScreen(this);
		}
	}

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
		GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDepthMask(false);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
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
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	private void rotateAndBlurSkybox(float par1) {
		this.mc.getTextureManager().bindTexture(this.field_110351_G);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
				GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
				GL11.GL_LINEAR);
		GL11.glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, 256, 256);
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glColorMask(true, true, true, false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		byte b0 = 3;

		for (int i = 0; i < b0; ++i) {
			tessellator
					.setColorRGBA_F(1.0F, 1.0F, 1.0F, 1.0F / (float) (i + 1));
			int j = this.width;
			int k = this.height;
			float f1 = (float) (i - b0 / 2) / 256.0F;
			tessellator.addVertexWithUV((double) j, (double) k,
					(double) this.zLevel, (double) (0.0F + f1), 1.0D);
			tessellator.addVertexWithUV((double) j, 0.0D, (double) this.zLevel,
					(double) (1.0F + f1), 1.0D);
			tessellator.addVertexWithUV(0.0D, 0.0D, (double) this.zLevel,
					(double) (1.0F + f1), 0.0D);
			tessellator.addVertexWithUV(0.0D, (double) k, (double) this.zLevel,
					(double) (0.0F + f1), 0.0D);
		}

		tessellator.draw();
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glColorMask(true, true, true, true);
	}

	private void renderSkybox(int par1, int par2, float par3) {
		this.mc.getFramebuffer().unbindFramebuffer();
		GL11.glViewport(0, 0, 256, 256);
		this.drawPanorama(par1, par2, par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.rotateAndBlurSkybox(par3);
		this.mc.getFramebuffer().bindFramebuffer(true);
		GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		float f1 = this.width > this.height ? 120.0F / (float) this.width
				: 120.0F / (float) this.height;
		float f2 = (float) this.height * f1 / 256.0F;
		float f3 = (float) this.width * f1 / 256.0F;
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

	public void drawScreen(int par1, int par2, float par3) {
		if(!MyUI.instance.service)
			mc.displayGuiScreen(new GuiMainMenu());
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		this.renderSkybox(par1, par2, par3);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
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

		tessellator.setColorOpaque_I(-1);
		GL11.glPushMatrix();
		GL11.glTranslatef((float) (this.width / 2 + 90), 70.0F, 0.0F);
		GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
		float f1 = 1.8F - MathHelper
				.abs(MathHelper.sin((float) (Minecraft.getSystemTime() % 1000L)
						/ 1000.0F * (float) Math.PI * 2.0F) * 0.1F);
		f1 = f1
				* 100.0F
				/ (float) (this.fontRendererObj.getStringWidth(this.splashText) + 32);
		GL11.glScalef(f1, f1, f1);
		this.drawCenteredString(this.fontRendererObj, this.splashText, 0, -8,
				-256);
		GL11.glPopMatrix();
		String s = "Minecraft 1.7.2";

		if (this.mc.isDemo()) {
			s = s + " Demo";
		}

		drawAnnouncement();

		List<String> brandings = Lists.reverse(FMLCommonHandler.instance()
				.getBrandings(true));
		for (int i = 0; i < brandings.size(); i++) {
			String brd = brandings.get(i);
			if (!Strings.isNullOrEmpty(brd)) {
				this.drawString(this.fontRendererObj, brd, 2, this.height
						- (10 + i * (this.fontRendererObj.FONT_HEIGHT + 1)),
						16777215);
			}
		}

		String s1 = "Copyright Mojang AB. Do not distribute!";
		String s2 = "Copyright huangyuhui. All Rights Reserved. Click here for about.";

		this.drawString(this.fontRendererObj, s2, this.width
				- this.fontRendererObj.getStringWidth(s2) - 2,
				this.height - 20, -1);
		this.drawString(this.fontRendererObj, s1, this.width
				- this.fontRendererObj.getStringWidth(s1) - 2,
				this.height - 10, -1);
		
		if (!StringUtils.isBlank(this.outdate_first)) {
			drawRect(this.outdate_l - 2, this.outdate_b - 2,
					this.outdate_r + 2, this.outdate_t - 1, 1428160512);
			this.drawCenteredString(this.fontRendererObj, this.outdate_first,
					this.width / 2, this.outdate_b, -1);
			this.drawCenteredString(this.fontRendererObj, this.outdate_second,
					this.width / 2,
					this.outdate_b + 12, -1);
		}

		super.drawScreen(par1, par2, par3);
	}
	
	@Override
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		
		if (15 <= par2 && par2 < 25) {
			if(thread.isLoading)
				thread.cancel();
			else
				thread.func_147224_a(MyUI.instance.serverURL1);
		}
		
		if (par2 < 15) {
			openLink(MyUI.instance.updateURL);
		}

		int block = width / 6;
		int i = this.height / 4 + 48;
		if ((block - 2) <= par1 && par1 <= (block * 2) && i <= par2
				&& par2 <= (i + 24 * 3 - 4)) {
			openLink(MyUI.instance.announcementPictureURL);
		}

		if (par2 > this.height - 20 && par2 < this.height - 10
				&& par1 > this.width - 50) {
			mc.displayGuiScreen(new MyUIModGuiFactory.CopyrightScreen(this));
		}
	}
	
	public void openLink(String url) {
		GuiConfirmOpenLink guiconfirmopenlink = new GuiConfirmOpenLink(
				new MyGuiYesNoCallback(url), url, 13, true);
		guiconfirmopenlink.func_146358_g();
		this.mc.displayGuiScreen(guiconfirmopenlink);
	}
	
	public static class MyGuiYesNoCallback implements GuiYesNoCallback {
		String url;
		
		public MyGuiYesNoCallback(String url) {
			this.url = url;
		}

		@Override
		public void confirmClicked(boolean arg0, int arg1) {
			
		}
		
	}
}