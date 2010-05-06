package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;

public class CenterShoppingCartProductPanel extends ShoppingCartProductPanel {

	int[] largest = new int[] { 0, 0 };

	public CenterShoppingCartProductPanel() {
	}

	public CenterShoppingCartProductPanel(int thumbSize) {
		super(thumbSize);
	}

	public int getWidth() {
		return Services.getServices().mainPanel.getCenterWidth() - 30;
	}

	protected boolean currentRowFull(Widget w) {
		if (w.getOffsetWidth() > largest[0]) {
			largest[1] = largest[0];
			largest[0] = w.getOffsetWidth();
		} else if (w.getOffsetWidth() > largest[1]) {
			largest[1] = w.getOffsetWidth();
		}
		if ((largest[1] + 2) * (col + 1) >= (getWidth()))
			return true;
		else
			return false;
	}

}
