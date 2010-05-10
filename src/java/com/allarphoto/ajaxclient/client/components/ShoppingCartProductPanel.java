package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;

public class ShoppingCartProductPanel extends MultiProductPanel {

	public ShoppingCartProductPanel() {
		// TODO Auto-generated constructor stub
	}

	public ShoppingCartProductPanel(int thumbSize) {
		super(thumbSize);
		// TODO Auto-generated constructor stub
	}

	protected ThumbnailPanel createThumbnail(AjaxProduct p) {
		return (ThumbnailPanel) Services.getServices().factory.createComponent(
				"CartThumbnailPanel", new Object[] { p, new Boolean(isCart),
						new Integer(thumbsize) });
	}

	protected ThumbnailPanel createThumbnail(Request r) {
		return (ThumbnailPanel) Services.getServices().factory.createComponent(
				"CartThumbnailPanel",
				new Object[] { r, new Integer(thumbsize) });
	}

	public int getWidth() {
		return Services.getServices().mainPanel.getEastWidth();
	}

	protected void addWidget(Widget w) {
		table.setWidget(row, col, w);
		table.getFlexCellFormatter().setHorizontalAlignment(row, col,
				HasHorizontalAlignment.ALIGN_LEFT);
		table.getFlexCellFormatter().setVerticalAlignment(row, col,
				HasVerticalAlignment.ALIGN_TOP);
		table.getFlexCellFormatter().addStyleName(row, col++, "cart-thumbnail");
		updateRowCol(w);
		setTheWidth();
	}

}
