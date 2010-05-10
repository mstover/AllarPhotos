package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.AddToCartListener;

public class CancelIcon extends BaseIcon {
	Request request;

	public CancelIcon() {
		addStyleName("button-icon");
		init();
	}

	public CancelIcon(Request r) {
		addStyleName("button-icon");
		product = r.getProduct();
		request = r;
		init();
	}

	protected ClickListener createClickListener() {
		return new AddToCartListener(this, true);
	}

	public String getIconName() {
		return "cancel.gif";
	}

	public String getToolTip() {
		return "Cancel";
	}

	public String getIconUrl() {
		return "cancel.gif";
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

}
