package com.lazerinc.ajaxclient.client.components;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.SwappablePanel;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.Request;

public class SouthShoppingCart extends ShoppingCart {

	HorizontalPanel hp;

	public SouthShoppingCart(AjaxCart c, SwappablePanel s) {
		super(c, s);
	}

	protected void init() {
		VerticalPanel vp = new VerticalPanel();
		hp = new HorizontalPanel();
		itemCount = AjaxSystem.getLabel("0 items in cart");
		vp.add(itemCount);
		VerticalShoppingCartToolbar sct = new VerticalShoppingCartToolbar(this,
				swap);
		vp.add(sct);
		hp.add(vp);
		add(hp);
		setCellHorizontalAlignment(itemCount, HasHorizontalAlignment.ALIGN_LEFT);
		setCellHorizontalAlignment(hp, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(sct, HasHorizontalAlignment.ALIGN_LEFT);
		Iterator iter = cart.getRequests().iterator();
		while (iter.hasNext()) {
			Request r = (Request) iter.next();
			added(r);
		}
		setSpacing(0);
	}

	protected void initProductPanel() {
		if (productPanel == null) {
			productPanel = new SouthShoppingCartProductPanel(60);
			hp.add(productPanel);
			hp.setCellHorizontalAlignment(productPanel,
					HasHorizontalAlignment.ALIGN_LEFT);
			productPanel.setCart(true);
		}
	}

	public void added(Request p) {
		initProductPanel();
		productPanel.add(p);
		updateTitle();

	}

	public void removed(Request p) {
		productPanel.remove(p);
		updateTitle();
		if (cart.getRequests().size() == 0) {
			hp.remove(productPanel);
			productPanel = null;
		}

	}

	public void cartCleared() {
		productPanel.clear();
		updateTitle();
		hp.remove(productPanel);
		productPanel = null;
	}

}
