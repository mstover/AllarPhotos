package com.lazerinc.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.SwappablePanel;
import com.lazerinc.ajaxclient.client.components.CheckoutPanel;
import com.lazerinc.ajaxclient.client.components.ShoppingCart;

public class CheckoutIcon extends BaseIcon {
	CheckoutPanel checkoutUI;

	SwappablePanel swapPanel;

	ShoppingCart cartGui;

	public CheckoutIcon() {
		super();
		init();
	}

	public CheckoutIcon(SwappablePanel swap, ShoppingCart sc) {
		addStyleName("button-icon");
		swapPanel = swap;
		cartGui = sc;
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				if (checkoutUI == null) {
					checkoutUI = new CheckoutPanel(Services.getServices().cart);
				} else
					checkoutUI.refresh();
				swapPanel.swapin(checkoutUI);
				cartGui.setVisible(false);
			}

		};
	}

	public String getIconUrl() {
		return "checkout.gif";
	}

	public String getToolTip() {
		return "Checkout";
	}
}
