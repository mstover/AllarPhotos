package com.lazerinc.ajaxclient.client.components;

import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.AjaxSystem;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;

public class VerticalProductPanel extends MultiProductPanel {

	public void add(AjaxProduct p) {
		changeNumShown(imagesShown.size());
		ProductToolbar toolbar = (ProductToolbar) Services.getServices().factory
				.createComponent("ProductToolbar", new Object[] { p });
		table.setWidget(row, 0, toolbar);
		ProductDisplayImage img = new ProductDisplayImage(p, thumbsize);
		table.setWidget(row, 1, img);
		table.setWidget(row, 2, getDescription(p));
		table.getCellFormatter().setVerticalAlignment(row, 2,
				HasVerticalAlignment.ALIGN_TOP);
		table.getCellFormatter().setVerticalAlignment(row, 0,
				HasVerticalAlignment.ALIGN_MIDDLE);
		table.getCellFormatter().setVerticalAlignment(row, 1,
				HasVerticalAlignment.ALIGN_TOP);
		table.setCellPadding(10);
		table.getRowFormatter().addStyleName(row, "thumbnail");
		updateRowCol(img);
		imagesShown.add(img);
		numShown++;
	}

	protected Widget getDescription(AjaxProduct p) {
		VerticalPanel vp = new VerticalPanel();
		Label model = AjaxSystem.getLabel(p.getValue("Model Number"));
		vp.add(model);
		Label desc = new Label(p.getValue("Item Description"));
		vp.add(desc);
		Label date = new Label(p.getDateCataloged());
		vp.add(date);

		vp.setCellHorizontalAlignment(model, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(desc, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setCellHorizontalAlignment(date, HasHorizontalAlignment.ALIGN_LEFT);
		vp.setSpacing(5);
		vp.setWidth("100%");
		setTheWidth();
		return vp;
	}

	public void add(AjaxProduct[] prods) {
		table.setCellPadding(10);
		changeNumShown(imagesShown.size());
		for (int i = 0; i < prods.length; i++) {
			ProductToolbar toolbar = (ProductToolbar) Services.getServices().factory
					.createComponent("ProductToolbar",
							new Object[] { prods[i] });
			table.setWidget(row, 0, toolbar);
			ProductDisplayImage img = new ProductDisplayImage(prods[i], thumbsize);
			table.setWidget(row, 1, img);
			table.setWidget(row, 2, getDescription(prods[i]));
			table.getCellFormatter().setVerticalAlignment(row, 2,
					HasVerticalAlignment.ALIGN_TOP);
			table.getCellFormatter().setVerticalAlignment(row, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			table.getCellFormatter().setVerticalAlignment(row, 1,
					HasVerticalAlignment.ALIGN_TOP);
			table.getRowFormatter().addStyleName(row, "thumbnail");
			updateRowCol(img);
			imagesShown.add(img);
			numShown++;
		}
	}

	public void add(List prods) {
		table.setCellPadding(10);
		changeNumShown(imagesShown.size());
		Iterator iter = prods.iterator();
		while (iter.hasNext()) {
			AjaxProduct prod = (AjaxProduct) iter.next();
			ProductToolbar toolbar = (ProductToolbar) Services.getServices().factory
					.createComponent("ProductToolbar", new Object[] { prod });
			table.setWidget(row, 0, toolbar);
			ProductDisplayImage img = new ProductDisplayImage(prod, thumbsize);
			table.setWidget(row, 1, img);
			table.setWidget(row, 2, getDescription(prod));
			table.getCellFormatter().setVerticalAlignment(row, 2,
					HasVerticalAlignment.ALIGN_TOP);
			table.getCellFormatter().setVerticalAlignment(row, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			table.getCellFormatter().setVerticalAlignment(row, 1,
					HasVerticalAlignment.ALIGN_TOP);
			table.getRowFormatter().addStyleName(row, "thumbnail");
			updateRowCol(img);
			imagesShown.add(img);
			numShown++;
		}
	}

	public void add(Request r) {
		// not being called for now
	}

	public void remove(AjaxProduct p) {
		for (int i = 0; i < imagesShown.size(); i++) {
			if (((ProductDisplayImage) imagesShown.get(i)).product.equals(p)) {
				table.removeRow(findRow((ProductDisplayImage) imagesShown
						.get(i)));
				imagesShown.remove(i);
				if (i < numShown)
					numShown--;
				return;
			}
		}
	}

	protected int findRow(ProductDisplayImage img) {
		for (int row = 0; row < table.getRowCount(); row++) {
			if (table.getWidget(row, 1) == img)
				return row;
		}
		return -1;
	}

	public VerticalProductPanel() {
		super(100);
	}

	public VerticalProductPanel(int thumbSize) {
		super(thumbSize);
	}

	protected boolean currentRowFull(Widget w) {
		return true;
	}

	public void redraw() {
		row = 0;
		col = 0;
		table.clear();
		remove(table);
		setWidth("0px");
		add(table);
		table.setCellPadding(10);

		Iterator iter = imagesShown.iterator();
		int count = 0;
		while (iter.hasNext() && count < numShown) {
			ProductDisplayImage img = (ProductDisplayImage) iter.next();
			ProductToolbar toolbar = (ProductToolbar) Services.getServices().factory
					.createComponent("ProductToolbar",
							new Object[] { img.product });
			table.setWidget(row, 0, toolbar);
			table.setWidget(row, 1, img);
			table.setWidget(row, 2, getDescription(img.product));
			table.getCellFormatter().setVerticalAlignment(row, 2,
					HasVerticalAlignment.ALIGN_TOP);
			table.getCellFormatter().setVerticalAlignment(row, 0,
					HasVerticalAlignment.ALIGN_MIDDLE);
			table.getCellFormatter().setVerticalAlignment(row, 1,
					HasVerticalAlignment.ALIGN_TOP);
			table.getRowFormatter().addStyleName(row, "thumbnail");
			updateRowCol(img);
			count++;
		}
		setWidth((getWidth() - 20) + "px");
	}

}
