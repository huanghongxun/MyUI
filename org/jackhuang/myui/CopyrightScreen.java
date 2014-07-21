/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "MyUI" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package org.jackhuang.myui;

public class CopyrightScreen extends GuiScreen {
	private GuiScreen parent;
	
	public CopyrightScreen(GuiScreen parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(1, this.width / 2 - 75, this.height - 38, I18n.getString("gui.done")));
	}
	
	@Override
	protected void actionPerformed(GuiButton p_146284_1_) {
		if ((p_146284_1_.enabled) && (p_146284_1_.id == 1)) {
			FMLClientHandler.instance().showGuiScreen(this.parent);
		}
	}
	
	@Override
	public void drawScreen(int par1, int par2, float par3) {
		drawDefaultBackground();
		drawCenteredString(this.fontRenderer, "MyUI Mod 版权许可(以下为侵犯版权的行为)", this.width / 2, 40, 16777215);
		
		List<String> list = ImmutableList.<String>of(
				"1) 未经作者(huangyuhui/huanghongxun)许可，发表其作品",
				"2) 没有参加创作，为谋取个人名利，在作者作品上署名的",
				"3) 篡改,剽窃作者作品",
				"4) 使用作者作品，应当支付报酬而未支付的",
				"5) 未经著作权人许可，复制、发行、汇编、通过信息网络向公众传播其作品的",
				"6) 未经著作权人或者与著作权有关的权利人许可，故意避开或者破坏权利人为其作品",
				"                         以上摘自百度百科",
				"第十一条 创作作品的公民是作者。",
				"第十七条 受委托创作的作品，著作权的归属由委托人和受托人通过合同约定。",
				"      合同未作明确约定或者没有订立合同的，著作权属于受托人",
				"第二十七条 许可使用合同和转让合同中著作权人未明确许可、转让的权利，",
				"      未经著作权人同意，另一方当事人不得行使。",
				"                         以上摘自著作权法",
				"禁止任何形式的转售作者的软件，禁止肆意传播。"
		);
		

		for (int i = list.size()-1; i >= 0; i--) {
			String brd = list.get(i);
			if (!Strings.isNullOrEmpty(brd)) {
				this.drawString(this.fontRenderer, brd, 2, 60 + i * (this.fontRenderer.FONT_HEIGHT + 1),
						16777215);
			}
		}
		
		
		super.drawScreen(par1, par2, par3);
	}
}
