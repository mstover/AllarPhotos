package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.AddToCartListener;

public class BaseIcon extends Image implements ClickListener, MouseListener {

	protected AjaxProduct product;

	public BaseIcon() {
		addStyleName("button-icon");
	}

	public BaseIcon(AjaxProduct p) {
		addStyleName("button-icon");
		product = p;
		init();
	}

	protected void init() {
		addClickListener(createClickListener());
		addClickListener(this);
		addMouseListener(this);
		setUrl(getIconUrl());
		setTitle(getToolTip());
	}

	public void onClick(Widget arg0) {
		DeferredCommand.add(new Command() {
			public void execute() {
				setUrl(getIconUrl());
				setTitle(getToolTip());
			}
		});
	}

	protected ClickListener createClickListener() {
		return new AddToCartListener(this);
	}

	public void setUrl(String arg0) {
		super.setUrl(Services.getServices().factory.getIconFolder() + "/"
				+ arg0);
	}

	public String getToolTip() {
		if (!Services.getServices().cart.contains(createRequest()))
			return "Order Original";
		else
			return "Remove from Shopping Cart";
	}

	public String getIconUrl() {
		if (!Services.getServices().cart.contains(createRequest()))
			return getIconName();
		else
			return "cancel_" + getIconName();
	}

	public String getIconName() {
		return "default.gif";
	}

	public Request createRequest() {
		return new Request(product, this, getTitle());
	}

	public void onMouseDown(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onMouseEnter(Widget sender) {
		sender.addStyleName("mouse");

	}

	public void onMouseLeave(Widget sender) {
		sender.removeStyleName("mouse");

	}

	public void onMouseMove(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onMouseUp(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

}
