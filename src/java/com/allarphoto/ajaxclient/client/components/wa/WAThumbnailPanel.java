package com.allarphoto.ajaxclient.client.components.wa;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
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

public class WAThumbnailPanel extends ThumbnailPanel {

	public WAThumbnailPanel(AjaxProduct p, boolean c) {
		super(p, c);
		// TODO Auto-generated constructor stub
	}

	public WAThumbnailPanel(Request r) {
		super(r);
		// TODO Auto-generated constructor stub
	}

	public WAThumbnailPanel(AjaxProduct p, boolean c, int ts) {
		super(p, c, ts);
		// TODO Auto-generated constructor stub
	}

	public WAThumbnailPanel(Request r, int ts) {
		super(r, ts);
		// TODO Auto-generated constructor stub
	}

	protected void init() {
		sinkEvents(Event.MOUSEEVENTS);
		ProductDisplayImage img = new ProductDisplayImage(product, thumbsize);
		add(img);
		setCellHorizontalAlignment(img, HasHorizontalAlignment.ALIGN_CENTER);
		HorizontalPanel hp = new HorizontalPanel();
		hp.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		hp.setSpacing(0);
		String tempModNum = product.getValue("Model Number");
		Label modelNumber = new Label();
		if(tempModNum  != null 
				&& tempModNum.length() > 1) {
			modelNumber = AjaxSystem.getText(tempModNum, "model_number");
		}else{
			modelNumber = AjaxSystem.getText("...", "model_number");
		}
		modelNumber.addStyleName("text");
		hp.add(modelNumber);
		hp.setCellHorizontalAlignment(modelNumber,
				HasHorizontalAlignment.ALIGN_LEFT);
		add(hp);
		hp.setWidth(thumbsize + "px");
		
		Label descript = new Label();
		if (product.getValue("Item Description") != null
				&& product.getValue("Item Description").length() > 1) {
			descript = AjaxSystem.getText(truncateDescription(product
					.getValue("Item Description")), "item_description");
		} else {
			String modDescript = product.getValue("Product") + " : " + product.getValue("Product Family");
			descript = AjaxSystem.getText(truncateDescription(modDescript), "item_description");
		}
		add(descript);
		descript.setWidth(thumbsize + "px");
		setCellHorizontalAlignment(descript, HasHorizontalAlignment.ALIGN_CENTER);
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
