package com.lazerinc.ajaxclient.client.components.pavilion;

import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.ajaxclient.client.components.icons.OrderIcon;

public class PavilionOrderIcon extends OrderIcon {

	public PavilionOrderIcon() {
		super();
		init();
	}

	public PavilionOrderIcon(AjaxProduct p) {
		super(p);
	}

	public String getToolTip() {
		if (!Services.getServices().cart.contains(createRequest()))
			return "Order Print";
		else
			return "Remove from Shopping Cart";
	}

}
