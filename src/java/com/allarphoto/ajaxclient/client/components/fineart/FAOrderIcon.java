package com.lazerinc.ajaxclient.client.components.fineart;

import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.ajaxclient.client.components.icons.OrderIcon;

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
