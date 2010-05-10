package com.allarphoto.ajaxclient.client.components.fineart;

import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.icons.OrderIcon;

public class FAOrderIcon extends OrderIcon {

	public FAOrderIcon() {
		super();
		init();
	}

	public FAOrderIcon(AjaxProduct p) {
		super(p);
	}

	public String getToolTip() {
		if (!Services.getServices().cart.contains(createRequest()))
			return "Order Print";
		else
			return "Remove from Shopping Cart";
	}

}
