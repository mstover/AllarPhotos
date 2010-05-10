package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.MouseListenerCollection;
import com.google.gwt.user.client.ui.SourcesMouseEvents;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.basic.Drag;

public class ThumbnailPanel extends VerticalPanel implements SourcesMouseEvents {
	protected AjaxProduct product;

	protected Request request;

	protected boolean cart;

	MouseListenerCollection mouseListeners = new MouseListenerCollection();

	protected int thumbsize = 150;

	public ThumbnailPanel(AjaxProduct p, boolean c) {
		this(p, c, 150);
	}

	public ThumbnailPanel(Request r) {
		this(r, 150);
	}

	public ThumbnailPanel(AjaxProduct p, boolean c, int ts) {
		thumbsize = ts;
		product = p;
		cart = c;
		init();
		addStyleName("thumbnail");
	}

	public ThumbnailPanel(Request r, int ts) {
		thumbsize = ts;
		request = r;
		product = request.getProduct();
		cart = true;
		init();
		addStyleName("thumbnail");
	}

	public void addMouseListener(MouseListener listener) {
		mouseListeners.add(listener);

	}

	public void removeMouseListener(MouseListener listener) {
		mouseListeners.remove(listener);

	}

	protected int calcScaling(int scaleTo, int alreadyScaled, int toBeScaled) {
		return toBeScaled * scaleTo / alreadyScaled;
	}

	protected void init() {
		sinkEvents(Event.MOUSEEVENTS);
		ProductDisplayImage img = new ProductDisplayImage(product, thumbsize);
		add(img);
		setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		Label l = AjaxSystem.getText(product.getName());
		add(l);
		setCellHorizontalAlignment(l, HasHorizontalAlignment.ALIGN_CENTER);
		Widget w = null;
		if (cart) {
			w = new ProductCartToolbar(request);
			add(w);
		} else {
			w = Services.getServices().factory.createComponent(
					"ProductToolbar", new Object[] { product });
			add(w);
		}
		setSpacing(5);
		setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
		this.addMouseListener(Drag.dragger);
	}

	public String getImageUrl() {
		return "/lazerweb/servlet/fetchpix?product_family="
				+ product.getFamilyName() + "&product_id=" + product.getId()
				+ "&thumb=true&mimetype=image/jpeg";
	}

	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {

		case Event.ONMOUSEDOWN:
		case Event.ONMOUSEUP:
		case Event.ONMOUSEMOVE:
		case Event.ONMOUSEOVER:
		case Event.ONMOUSEOUT:
			if (mouseListeners != null) {
				mouseListeners.fireMouseEvent(this, event);
			}
			break;
		}
	}

	public AjaxProduct getProduct() {
		return product;
	}

	public void setProduct(AjaxProduct product) {
		this.product = product;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
}
