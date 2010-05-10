package com.allarphoto.ajaxclient.client.components.pavilion;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.allarphoto.ajaxclient.client.AjaxSystem;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.Request;
import com.allarphoto.ajaxclient.client.components.ProductCartToolbar;
import com.allarphoto.ajaxclient.client.components.ProductDisplayImage;
import com.allarphoto.ajaxclient.client.components.ThumbnailPanel;
import com.allarphoto.ajaxclient.client.components.basic.Drag;

public class PavilionThumbnailPanel extends ThumbnailPanel {

	public PavilionThumbnailPanel(AjaxProduct p, boolean c) {
		super(p, c);
		// TODO Auto-generated constructor stub
	}

	public PavilionThumbnailPanel(Request r) {
		super(r);
		// TODO Auto-generated constructor stub
	}

	public PavilionThumbnailPanel(AjaxProduct p, boolean c, int ts) {
		super(p, c, ts);
		// TODO Auto-generated constructor stub
	}

	public PavilionThumbnailPanel(Request r, int ts) {
		super(r, ts);
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		sinkEvents(Event.MOUSEEVENTS);
		ProductDisplayImage img = new ProductDisplayImage(product, thumbsize);
		add(img);
		setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		Label l = AjaxSystem.getText(product.getName());
		add(l);
		setCellHorizontalAlignment(l, HasHorizontalAlignment.ALIGN_CENTER);

		if (product.getValue("Item Description") != null
				&& product.getValue("Item Description").length() > 1) {		
			Label descript = new Label();
			descript = AjaxSystem.getText(truncateDescription(product
					.getValue("Item Description")), "item_description");
			descript.setWidth(thumbsize + "px");
			setCellHorizontalAlignment(descript, HasHorizontalAlignment.ALIGN_CENTER);
			add(descript);
		}
		Widget w = null;
		if (cart) {
			w = new ProductCartToolbar(request);
			add(w);
		} else {
			w = Services.getServices().factory.createComponent(
					"ProductToolbar", new Object[] { product });
			add(w);
		}
		setSpacing(0);
		setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
		this.addMouseListener(Drag.dragger);
	}

	private String truncateDescription(String des) {
		if (des == null)
			return "";
		if (des.length() > 60)
			return des.substring(0, 60);
		return des;
	}

}
