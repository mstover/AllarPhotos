package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.components.ShoppingCart;

public class ViewCartIcon extends BaseIcon {
	ShoppingCart cartDisplay;

	SwappablePanel swapPanel;

	public ViewCartIcon() {
		super();
		init();
	}

	public ViewCartIcon(SwappablePanel swap) {
		addStyleName("button-icon");
		swapPanel = swap;
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				if (cartDisplay == null)
					cartDisplay = (ShoppingCart) Services.getServices().factory
							.createComponent("ShoppingCart", new Object[] {
									Services.getServices().cart, swapPanel });
				// swapPanel.swapin(cartDisplay);
				Services.getServices().mainPanel.setEast(cartDisplay);
				cartDisplay.redraw();
			}

		};
	}

	public String getIconUrl() {
		return "shopping_cart.gif";
	}

	public String getToolTip() {
		return "View Current Order/Download Requests";
	}

}
