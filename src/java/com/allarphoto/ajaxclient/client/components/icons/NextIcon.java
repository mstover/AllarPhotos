package com.allarphoto.ajaxclient.client.components.icons;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.PopupWarning;
import com.allarphoto.ajaxclient.client.components.ProductPopup;
import com.allarphoto.ajaxclient.client.components.SearchTree;

public class NextIcon extends PreviousIcon {

	public NextIcon() {
		addStyleName("button-icon");
		init();
	}

	public NextIcon(ProductPopup p) {
		this(p, false);
	}

	public NextIcon(ProductPopup p, boolean cart) {
		super(p, cart);
	}

	protected Object getDestProduct() {
		if (!isCart) {
			Widget west = Services.getServices().mainPanel.getWest();
			if (west != null && west instanceof SearchTree) {
				AjaxProduct[] products = ((SearchTree) west)
						.getCurrentProductList();
				int i = 0;
				for (i = 0; i < products.length; i++) {
					if (products[i].equals(popup.getProduct()))
						break;
				}
				if (i >= products.length - 1)
					return null;
				else {
					has = true;
					return products[i + 1];
				}
			}
			return null;
		} else {
			AjaxCart cart = Services.getServices().cart;
			Collection requests = cart.getRequests();
			Iterator iter = requests.iterator();
			Request next = null;
			int i = 0;
			while (iter.hasNext()) {
				Request req = (Request) iter.next();
				if (req.equals(popup.getRequest())) {
					if (iter.hasNext())
						next = (Request) iter.next();
					break;
				}
				i++;
			}
			if (next != null) {
				has = true;
				return next;
			} else
				return null;

		}

	}

	public String getIconUrl() {
		if (has)
			return "next.gif";
		else
			return "next_disabled.gif";
	}

	public String getToolTip() {
		return "View Next Image";
	}

}
