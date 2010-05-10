package com.allarphoto.ajaxclient.client.components.icons;

import java.util.Collection;
import java.util.Iterator;

import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxCart;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.ProductPopup;
import com.allarphoto.ajaxclient.client.components.SearchTree;

public class PreviousIcon extends BaseIcon {

	protected boolean has = false;

	protected boolean isCart;

	protected ProductPopup popup;

	public PreviousIcon() {
		addStyleName("button-icon");
		init();
	}

	public PreviousIcon(ProductPopup p, boolean cart) {
		addStyleName("button-icon");
		isCart = cart;
		popup = p;
		this.init();
	}

	public PreviousIcon(ProductPopup p) {
		this(p, false);
	}

	protected ClickListener createClickListener() {
		final Object dest = getDestProduct();
		if (dest != null)
			return new ClickListener() {

				public void onClick(Widget sender) {
					if (dest instanceof AjaxProduct)
						popup.setProduct((AjaxProduct) dest);
					else
						popup.setRequest((Request) dest);
					popup.clear();
					popup.init();
				}

			};
		else
			return new ClickListener() {

				public void onClick(Widget sender) {
				}

			};
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
				if (i == 0) {
					return null;
				} else {
					has = true;
					return products[i - 1];
				}
			}
			return null;
		} else {
			AjaxCart cart = Services.getServices().cart;
			Collection requests = cart.getRequests();
			Iterator iter = requests.iterator();
			Request previous = null;
			while (iter.hasNext()) {
				Request req = (Request) iter.next();
				if (req.equals(popup.getRequest())) {
					break;
				}
				previous = req;
			}
			if (previous != null) {
				has = true;
				return previous;
			} else
				return null;

		}
	}

	public String getIconUrl() {
		if (has)
			return "previous.gif";
		else
			return "previous_disabled.gif";
	}

	public String getToolTip() {
		return "View Previous Image";
	}

}
