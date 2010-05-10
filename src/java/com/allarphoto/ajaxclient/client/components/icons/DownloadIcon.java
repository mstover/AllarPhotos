package com.allarphoto.ajaxclient.client.components.icons;

import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;

public class DownloadIcon extends BaseIcon {
	String type;

	String description;

	public DownloadIcon() {
		addStyleName("button-icon");
		init();
	}

	public DownloadIcon(AjaxProduct p, String t, String desc) {
		addStyleName("button-icon");
		product = p;
		type = t;
		description = desc;
		init();
	}

	public Request createRequest() {
		return new Request(product, true, type, this, getTitle());
	}

	public String getIconName() {
		return type + "_download.gif";
	}

	public String getToolTip() {
		if (!Services.getServices().cart.contains(createRequest()))
			return description;
		else
			return "Remove from Shopping Cart";
	}

}
