package com.allarphoto.ajaxclient.client.components.icons;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.components.BusyPopup;
import com.allarphoto.ajaxclient.client.components.PopupWarning;

public class CartSave extends BaseIcon {

	public CartSave() {
		addStyleName("button-icon");
		addStyleName("button-save-cart");
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				BusyPopup.waitFor("Saving Shopping Cart");
				Services.getServices().orderService.saveShoppingCart(Services
						.getServices().cart, new AsyncCallback() {

					public void onFailure(Throwable caught) {
						BusyPopup.done("Saving Shopping Cart");

					}

					public void onSuccess(Object result) {
						BusyPopup.done("Saving Shopping Cart");
					}

				});
			}

		};
	}

	public String getIconUrl() {
		return "cart-save.gif";
	}

	public String getToolTip() {
		return "Save Current Shopping Cart";
	}

}
