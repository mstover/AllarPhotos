package com.allarphoto.ajaxclient.client.factory;

import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.admin.LazerMenu;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.components.CheckoutPanel;
import com.allarphoto.ajaxclient.client.components.mcc.MccSearchTree;
import com.allarphoto.ajaxclient.client.components.mcc.MccVerifyScreen;

public class MccComponentFactory extends DefaultComponentFactory {

	public Widget[] getBottomComponentStack() {
		// TODO Auto-generated method stub
		return super.getBottomComponentStack();
	}

	public Widget getFooter() {
		// TODO Auto-generated method stub
		return super.getFooter();
	}

	public LazerMenu getMainMenu() {
		return new MccMenu(getCommands());
	}

	public Widget getSplashTop() {
		return null;
	}

	public Widget[] getTopComponentStack() {
		return new Widget[] { getMainMenu() };
	}

	public String getStartingSection() {
		return "Browse lw_demo";
	}

	public void setSectionTitle(String title) {
	}

	public Widget createComponent(String classname, Object[] args) {
		if ("SearchTree".equals(classname)) {
			return new MccSearchTree((SwappablePanel) args[0],
					(String) args[1], (args.length > 2 ? (String[]) args[2]
							: null));
		} else if ("VerifyScreen".equals(classname)) {
			return new MccVerifyScreen((AjaxCart) args[0],
					(CheckoutPanel) args[1]);
		} else
			return super.createComponent(classname, args);
	}

}
