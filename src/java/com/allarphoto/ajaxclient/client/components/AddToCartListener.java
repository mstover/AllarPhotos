package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.icons.BaseIcon;
import com.allarphoto.ajaxclient.client.components.icons.CancelIcon;

public class AddToCartListener implements ClickListener {

	BaseIcon icon;

	boolean canceling = false;

	public AddToCartListener(BaseIcon i) {
		icon = i;
	}

	public AddToCartListener(BaseIcon cancel, boolean c) {
		icon = cancel;
		canceling = c;
	}

	public void onClick(Widget i) {
		if (canceling) {
			Services.getServices().cart
					.remove(((CancelIcon) icon).getRequest());
			BaseIcon backIcon = ((CancelIcon) icon).getRequest().getBackIcon();
			if (backIcon != null && backIcon.isAttached())
				backIcon.onClick(backIcon);
		} else {
			if (icon.getUrl().indexOf("cancel") == -1) {
				Services.getServices().cart.add(icon.createRequest());
			} else {
				Services.getServices().cart.remove(icon.createRequest());
			}
		}
	}
}