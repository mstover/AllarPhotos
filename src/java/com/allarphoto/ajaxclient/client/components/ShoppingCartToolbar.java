package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.lazerinc.ajaxclient.client.SwappablePanel;
import com.lazerinc.ajaxclient.client.components.icons.CartSave;
import com.lazerinc.ajaxclient.client.components.icons.CheckoutIcon;
import com.lazerinc.ajaxclient.client.components.icons.ClearCartIcon;

public class ShoppingCartToolbar extends HorizontalPanel {

	SwappablePanel swapPanel;

	CheckoutPanel checkoutUI;

	ShoppingCart cartGui;

	public ShoppingCartToolbar(SwappablePanel swap, ShoppingCart sc) {
		swapPanel = swap;
		addStyleName("toolbar");
		cartGui = sc;
		init();
	}

	private void init() {
		add(new CheckoutIcon(swapPanel, cartGui));
		add(new ClearCartIcon());
		add(new CartSave()); // Working on a bug in WelchAllyn Feb 08
	}
}
