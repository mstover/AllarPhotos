package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.components.icons.CartSave;
import com.allarphoto.ajaxclient.client.components.icons.CheckoutIcon;
import com.allarphoto.ajaxclient.client.components.icons.ClearCartIcon;

public class VerticalShoppingCartToolbar extends VerticalPanel {

	SwappablePanel swapPanel;

	ShoppingCart currentCart;

	public VerticalShoppingCartToolbar(ShoppingCart currCart,
			SwappablePanel swap) {
		swapPanel = swap;
		addStyleName("toolbar");
		currentCart = currCart;
		init();
	}

	private void init() {
		Label expand = AjaxSystem.getLabel("Expand", "linked");
		expand.setTitle("Expand view of shopping cart");
		expand.addClickListener(new ClickListener() {

			public void onClick(Widget sender) {
				final PopupShoppingCart bigCart = new PopupShoppingCart(true,
						swapPanel);
				// swapPanel.swapin(bigCart);
				// currentCart.setVisible(false);
				Timer t = new Timer() {
					public void run() {
						bigCart.redraw();
					}
				};
				t.schedule(500);
			}

		});
		add(expand);
		add(new CheckoutIcon(swapPanel, currentCart));
		add(new ClearCartIcon());
		add(new CartSave());
	}
}
