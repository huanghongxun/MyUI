/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * This code is the property of huanghongxun
 * and may not be used with explicit written
 * permission.
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
"以下购买人为甲方，受托人为乙方。",
"mcbbs: huanghongxun, baidu: huanghongxun20, qq: 1542806507",
"1 甲方权利和义务",
"1.1 甲方向乙方提供软件定制时所需的定制软件规划。",
"1.2 根据项目需要提供有关资料、图片等，要求保证所有资料完整、真实。",
"1.3 甲方在使用乙方提供的软件产品时，不得转为第三方使用，本次定制的最终产品是乙方专门为甲方定制，其知识产权属于乙方，甲方为永久使用权。",
"1.4 如甲方需要乙方提供其他软件、技术支持、相关开发等，双方应另行签署其他协议；",
"1.5 甲方同意按双方约定的付款方式和时间及时向乙方支付软件费用，以及在乙方进行软件开发工作时提供其他必要的帮助。",
"1.6 甲方承诺，向乙方提供的内容、资料等不会侵犯任何第三方权利；若发生侵犯第三方权利的情形，由甲方承担全部责任。因甲方在使用本软件给第三方造成损害的，由甲方自行承担责任。",
"1.7 甲方当事人应当保守在履行服务协议中获知的对方商业秘密。",
"1.8 甲方对乙方提供服务过程中使用的技术、软件等所涉及的包括知识产权在内的一切法律问题不承担任何责任。",
"2 乙方的权利和义务",
"2.1 可以根据甲方的要求指导甲方使用定制软件；",
"2.2 依合同收取费用；",
"2.3 在开发过程中，对甲方陆续提出的修改要求，乙方应协助实现，（但如果超出合同要求工作量过大，则需要协商修改合同条款），并由甲方验收，对验收时不合格的地方进行修改；",
"2.4 本合同项目完成后，甲方有对这些相关程序的永久使用权，包括二次开发。乙方完全拥有本程序代码的所有权。除乙方授权外，甲方不得向乙方源代码或源代码所生产的产品直接销售给第三方。",
"2.5 乙方当事人应当保守在履行服务协议中获知的对方商业秘密。",
"2.6 乙方在签订合同后，开始定制工作，在设计阶段和功能开发阶段要及时与甲方沟通，若甲乙双方在功能理解上存在分歧不能协商好，本着甲方对乙方专业水准的认可前提下，以乙方的理解为主。"
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
