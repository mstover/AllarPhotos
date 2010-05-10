package com.allarphoto.ajaxclient.client.components.icons;

import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;

public class OrderIcon extends BaseIcon {

	public OrderIcon() {
		super();
		init();
	}

	public OrderIcon(AjaxProduct p) {
		super(p);
	}

	public Request createRequest() {
		return new Request(product, this, getTitle());
	}

	public String getIconName() {
		return "order_product.gif";
	}

	public String getToolTip() {
		if (!Services.getServices().cart.contains(createRequest()))
			return "Order Original";
		else
			return "Remove from Shopping Cart";
	}

}
