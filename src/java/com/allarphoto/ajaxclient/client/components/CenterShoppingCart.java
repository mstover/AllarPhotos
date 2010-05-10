package com.allarphoto.ajaxclient.client.components;

import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;

public class CenterShoppingCart extends ShoppingCart {

	public CenterShoppingCart(AjaxCart c, SwappablePanel s) {
		super(c, s);
		// TODO Auto-generated constructor stub
	}

	protected void initProductPanel() {
		if (productPanel == null) {
			productPanel = new CenterShoppingCartProductPanel(75);
			add(productPanel);
			productPanel.setCart(true);
		}
	}

}
