package com.lazerinc.ajaxclient.client.components;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;

public class SouthShoppingCartProductPanel extends ShoppingCartProductPanel {

	HorizontalPanel bag;

	public SouthShoppingCartProductPanel() {
		super();
		remove(table);
		bag = new HorizontalPanel();
		add(bag);
		bag.addStyleName("south-cart");
		removeStyleName("image-viewer");
	}

	public SouthShoppingCartProductPanel(int thumbSize) {
		super(thumbSize);
		remove(table);
		bag = new HorizontalPanel();
		add(bag);
		bag.addStyleName("south-cart");
		removeStyleName("image-viewer");
	}

	protected void addWidget(Widget w) {
		bag.add(w);
		w.addStyleName("cart-thumbnail");
	}

	public void redraw() {
		bag.clear();
		bag.addStyleName("south-cart");
		remove(bag);
		add(bag);

		Iterator iter = imagesShown.iterator();
		int count = 0;
		while (iter.hasNext() && count < numShown) {
			Widget w = (Widget) iter.next();
			addWidget(w);
			count++;
		}
	}

	public void clear() {
		bag.clear();
		bag.addStyleName("south-cart");
	}

	public void remove(Request r) {
		for (int i = 0; i < imagesShown.size(); i++) {
			if (((ThumbnailPanel) imagesShown.get(i)).getRequest().equals(r)) {
				bag.remove((ThumbnailPanel) imagesShown.get(i));
				imagesShown.remove(i);
				if (i < numShown)
					numShown--;
				break;
			}
		}
		redraw();
	}

	public void remove(AjaxProduct p) {
		for (int i = 0; i < imagesShown.size(); i++) {
			if (((ThumbnailPanel) imagesShown.get(i)).getProduct().equals(p)) {
				bag.remove((ThumbnailPanel) imagesShown.get(i));
				imagesShown.remove(i);
				if (i < numShown)
					numShown--;
				break;
			}
		}
		redraw();
	}

	public int getWidth() {
		return Services.getServices().mainPanel.getSouthWidth();
	}

}
