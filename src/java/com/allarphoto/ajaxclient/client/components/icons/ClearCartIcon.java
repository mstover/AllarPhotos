package com.lazerinc.ajaxclient.client.components.icons;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.components.PopupWarning;

public class ClearCartIcon extends BaseIcon {

	public ClearCartIcon() {
		addStyleName("button-icon");
		init();
	}

	protected ClickListener createClickListener() {
		return new ClickListener() {

			public void onClick(Widget arg0) {
				Services.getServices().cart.clear();
				Services.getServices().orderService.saveShoppingCart(Services
						.getServices().cart, new AsyncCallback() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					public void onSuccess(Object result) {
						// TODO Auto-generated method stub

					}

				});
				new PopupWarning("Shopping Cart Cleared");
			}

		};
	}

	public String getIconUrl() {
		return "cart-clear.gif";
	}

	public String getToolTip() {
		return "Empty Shopping Cart";
	}

}
