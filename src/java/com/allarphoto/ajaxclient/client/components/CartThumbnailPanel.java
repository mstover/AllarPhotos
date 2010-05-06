package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;
import com.lazerinc.ajaxclient.client.components.basic.Drag;

public class CartThumbnailPanel extends ThumbnailPanel {

	public CartThumbnailPanel(AjaxProduct p, boolean c, int ts) {
		super(p, c, ts);
		// TODO Auto-generated constructor stub
	}

	public CartThumbnailPanel(AjaxProduct p, boolean c) {
		super(p, c);
		// TODO Auto-generated constructor stub
	}

	public CartThumbnailPanel(Request r, int ts) {
		super(r, ts);
		// TODO Auto-generated constructor stub
	}

	public CartThumbnailPanel(Request r) {
		super(r);
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		sinkEvents(Event.MOUSEEVENTS);
		HorizontalPanel hp = new HorizontalPanel();
		ProductDisplayImage img = new ProductDisplayImage(product, thumbsize);
		hp.add(img);
		Widget w = null;
		if (cart) {
			w = new VerticalPanel();
			((VerticalPanel) w).add(AjaxSystem.getText(request.toString()));
			((VerticalPanel) w).add(new ProductCartToolbar(request));
			hp.add(w);
		} else {
			w = Services.getServices().factory.createComponent(
					"ProductToolbar", new Object[] { product });
			hp.add(w);
		}
		hp.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_LEFT);
		hp.setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_LEFT);
		add(hp);
		setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_LEFT);
		img.addClickListener(new ProductPopup(request));
		Label l = AjaxSystem.getText(product.getName());
		add(l);
		setCellHorizontalAlignment(l, HasHorizontalAlignment.ALIGN_CENTER);
		setSpacing(1);
		this.addMouseListener(Drag.dragger);
	}

}
