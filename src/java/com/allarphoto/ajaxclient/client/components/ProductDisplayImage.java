package com.allarphoto.ajaxclient.client.components;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;

public class ProductDisplayImage extends HTML implements MouseListener {

	AjaxProduct product;

	int preferredSize;

	public ProductDisplayImage() {
		// TODO Auto-generated constructor stub
	}

	public ProductDisplayImage(String url) {
		super(url);
		// TODO Auto-generated constructor stub
	}

	public ProductDisplayImage(AjaxProduct p, int size) {
		super();
		setElement(DOM.createElement("img"));
	    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS);
		preferredSize = size;
		product = p;
		init();
	}

	protected boolean isThumb() {
		return preferredSize > 0;
	}

	protected String getImageUrl() {
		return "/lazerweb/servlet/fetchpix?product_family="
				+ product.getFamilyName() + "&product_id=" + product.getId()
				+ "&thumb=" + isThumb() + "&mimetype=image/jpeg";
	}

	protected void init() {
		setTitle("Click for details");
		unsinkEvents(Event.ONMOUSEDOWN | Event.ONMOUSEUP);
		if (isThumb() && product.getHeight() > product.getWidth()) {
			setHeight(preferredSize + "px");
			setWidth(calcScaling(preferredSize, product.getHeight(), product
					.getWidth())
					+ "px");

		} else if (isThumb()) {
			setWidth(preferredSize + "px");
			setHeight(calcScaling(preferredSize, product.getWidth(), product
					.getHeight())
					+ "px");
		}
		if (product.isExpired()) {
			addStyleName("expired-image");
		}
		addMouseListener(this);
		if (isThumb())
		{
			addClickListener(new ProductPopup(product));
		}
		if (product.isExpired())
			addStyleName("expired-image");
		DOM.setElementAttribute(getElement(), "src", getImageUrl());
	}

	protected int calcScaling(int scaleTo, int alreadyScaled, int toBeScaled) {
		return toBeScaled * scaleTo / alreadyScaled;
	}

	public void onMouseDown(Widget sender, int x, int y) {

	}

	public void onMouseEnter(Widget sender) {
		sender.addStyleName("mouse");

	}

	public void onMouseLeave(Widget sender) {
		sender.removeStyleName("mouse");

	}

	public void onMouseMove(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

	public void onMouseUp(Widget sender, int x, int y) {
		// TODO Auto-generated method stub

	}

}
