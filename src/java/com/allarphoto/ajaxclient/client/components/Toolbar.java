package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.components.icons.CheckoutIcon;
import com.allarphoto.ajaxclient.client.components.icons.ClearCartIcon;
import com.allarphoto.ajaxclient.client.components.icons.ViewCartIcon;

public class Toolbar extends HorizontalPanel {

	SwappablePanel swapPanel;

	CheckoutPanel checkoutUI;

	public Toolbar(SwappablePanel swap) {
		swapPanel = swap;
		addStyleName("toolbar");
		init();
	}

	private void init() {
		// add(new ViewCartIcon(swapPanel));
		/*
		 * add(new CheckoutIcon(swapPanel)); add(new ClearCartIcon());
		 */
	}

}
