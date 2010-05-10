package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.icons.CancelIcon;

public class ProductCartToolbar extends VerticalPanel {
	Request request;

	public ProductCartToolbar(Request p) {
		request = p;
		addStyleName("toolbar");
		init();
	}

	private void init() {
		// add(AjaxSystem.getText(request.toString()));
		add(new CancelIcon(request));
	}

}
