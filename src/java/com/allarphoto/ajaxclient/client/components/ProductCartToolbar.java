package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.ajaxclient.client.components.icons.CancelIcon;

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
