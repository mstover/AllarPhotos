package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.allarphoto.ajaxclient.client.CartListener;
import com.allarphoto.ajaxclient.client.Redrawable;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.SwappablePanel;
import com.allarphoto.ajaxclient.client.beans.Request;

public class PopupShoppingCart extends PopupPanel implements CartListener,
		Redrawable {

	CenterShoppingCart cartView;

	SwappablePanel swap;

	public PopupShoppingCart(SwappablePanel s) {
		swap = s;
		init();
	}

	public PopupShoppingCart(boolean autoHide, SwappablePanel s) {
		super(autoHide);
		swap = s;
		init();
	}

	protected void init() {
		cartView = new CenterShoppingCart(Services.getServices().cart, swap);
		add(new ScrollPanel(cartView));
		addStyleName("popup-info");
		setPopupPosition(Services.getServices().mainPanel
				.getAbsoluteCenterLeft(), Services.getServices().mainPanel
				.getAbsoluteCenterTop());
		setHeight(Services.getServices().mainPanel.getCenterHeight() + "px");
		setWidth((Services.getServices().mainPanel.getCenterWidth() - 70)
				+ "px");
		setVisible(true);
		show();
		redraw();
	}

	public void added(Request p) {
		cartView.added(p);
	}

	public void cartCleared() {
		cartView.cartCleared();
	}

	public void clear() {
		cartView.clear();
	}

	public void redraw() {
		cartView.redraw();
		setHeight(Services.getServices().mainPanel.getCenterHeight() + "px");
		setWidth((Services.getServices().mainPanel.getCenterWidth() - 70)
				+ "px");
	}

	public void removed(Request p) {
		cartView.removed(p);
	}
}
