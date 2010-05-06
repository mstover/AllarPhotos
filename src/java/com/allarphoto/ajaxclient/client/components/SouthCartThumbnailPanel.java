package com.lazerinc.ajaxclient.client.components;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
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

public class SouthCartThumbnailPanel extends CartThumbnailPanel {

	public SouthCartThumbnailPanel(AjaxProduct p, boolean c, int ts) {
		super(p, c, ts);
		// TODO Auto-generated constructor stub
	}

	public SouthCartThumbnailPanel(AjaxProduct p, boolean c) {
		super(p, c);
		// TODO Auto-generated constructor stub
	}

	public SouthCartThumbnailPanel(Request r, int ts) {
		super(r, ts);
		// TODO Auto-generated constructor stub
	}

	public SouthCartThumbnailPanel(Request r) {
		super(r);
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		sinkEvents(Event.MOUSEEVENTS);
		setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(0);
		hp.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		ProductDisplayImage img = new ProductDisplayImage(product, thumbsize);
		Widget w = null;
		if (cart) {
			w = new ProductCartToolbar(request);
		} else {
			w = Services.getServices().factory.createComponent(
					"ProductToolbar", new Object[] { product });
		}
		hp.add(img);
		hp.add(w);
		add(hp);
		img.addClickListener(new ProductPopup(request));
		Label l = AjaxSystem.getText(product.getName());
		add(l);
		add(new Label(request.toString()));
		setSpacing(1);
		this.addMouseListener(Drag.dragger);
	}

}
