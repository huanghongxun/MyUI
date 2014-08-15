/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "MyUI" is distributed under the MIT License.
 * Please check the contents of the license.
 */
package org.jackhuang.myui.client;

public class NewMenuHandler {
	
	public static NewMenuHandler instance = new NewMenuHandler();

	@SubscribeEvent
	public void openMainMenu(GuiOpenEvent event) throws IOException {
		if (event.gui instanceof GuiMainMenu) {
			event.gui = new NewMenu();
		}
	}
}
