package com.lazerinc.ajaxclient.client.components;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.CartListener;
import com.lazerinc.ajaxclient.client.Redrawable;
import com.lazerinc.ajaxclient.client.SwappablePanel;
import com.lazerinc.ajaxclient.client.beans.AjaxCart;
import com.lazerinc.ajaxclient.client.beans.Request;

public class ShoppingCart extends VerticalPanel implements CartListener,
		Redrawable {

	protected AjaxCart cart;

	protected MultiProductPanel productPanel;

	protected Label itemCount = AjaxSystem.getTitle("0 items in cart");

	protected SwappablePanel swap;

	public ShoppingCart(AjaxCart c, SwappablePanel s) {
		cart = c;
		swap = s;
		cart.addListener(this);
		init();
	}

	protected void init() {
		add(itemCount);
		ShoppingCartToolbar sct = new ShoppingCartToolbar(swap, this);
		add(sct);
		setCellHorizontalAlignment(sct, HasHorizontalAlignment.ALIGN_CENTER);
		Iterator iter = cart.getRequests().iterator();
		while (iter.hasNext()) {
			Request r = (Request) iter.next();
			added(r);
		}
	}

	protected void initProductPanel() {
		if (productPanel == null) {
			productPanel = new ShoppingCartProductPanel(75);
			add(productPanel);
			productPanel.setCart(true);
		}
	}

	public void added(Request p) {
		initProductPanel();
		productPanel.add(p);
		updateTitle();

	}

	public void redraw() {
		setVisible(true);
		if (productPanel != null)
			productPanel.redraw();
	}

	protected void updateTitle() {
		itemCount.setText(cart.getRequests().size() + " items in cart");
	}

	public void removed(Request p) {
		productPanel.remove(p);
		updateTitle();
		if (cart.getRequests().size() == 0) {
			remove(productPanel);
			productPanel = null;
		}

	}

	public void cartCleared() {
		productPanel.clear();
		updateTitle();
		remove(productPanel);
		productPanel = null;
	}

}
