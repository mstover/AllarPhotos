package com.lazerinc.ajaxclient.client.components;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Widget;
import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.Request;

public class MultiProductPanel extends FlowPanel {

	static int fudgeFactor = 75;

	protected List imagesShown = new ArrayList();

	boolean isCart = false;

	boolean widSet = false;

	protected FlexTable table;

	int largest = 0;

	int thumbsize = 150;

	protected boolean widthSet = false;

	protected int numShown = 0;

	int row = 0;

	int col = 0;

	public MultiProductPanel() {
		this(150);
	}

	public MultiProductPanel(int thumbSize) {
		super();
		this.thumbsize = thumbSize;
		addStyleName("image-viewer");
		table = new FlexTable();
		table.setVisible(true);
		add(table);
	}

	public void add(AjaxProduct p) {
		changeNumShown(imagesShown.size());
		Widget w = createThumbnail(p);
		imagesShown.add(w);
		addWidget(w);
		numShown++;
	}

	public int getNumShown() {
		return numShown;
	}

	protected void setTheWidth() {
		if (!widthSet) {
			setWidth((getWidth() - 40) + "px");
			widthSet = true;
		}
	}

	protected ThumbnailPanel createThumbnail(AjaxProduct p) {
		return (ThumbnailPanel)Services.getServices().factory.createComponent("ThumbnailPanel",new Object[]{p,new Boolean(isCart),new Integer(thumbsize)});
	}

	protected void updateRowCol(Widget w) {
		int count = 0;
		while (w.getOffsetWidth() == 0 && count++ < 100)
			w.setVisible(true);
		if (currentRowFull(w)) {
			col = 0;
			row++;
		}
	}

	protected boolean currentRowFull(Widget w) {
		if (w.getOffsetWidth() > largest)
			largest = w.getOffsetWidth();
		if ((largest + 10) * (col + 1) >= (getWidth() - 20))
			return true;
		else
			return false;
	}

	public int getWidth() {
		return Services.getServices().mainPanel.getCenterWidth();
	}

	public void add(Request r) {
		changeNumShown(imagesShown.size());
		Widget w = createThumbnail(r);
		imagesShown.add(w);
		addWidget(w);
		numShown++;
	}

	protected ThumbnailPanel createThumbnail(Request r) {
		return (ThumbnailPanel)Services.getServices().factory.createComponent("ThumbnailPanel",new Object[]{r,new Integer(thumbsize)});
	}

	public void remove(Request r) {
		for (int i = 0; i < imagesShown.size(); i++) {
			if (((ThumbnailPanel) imagesShown.get(i)).request.equals(r)) {
				table.remove((ThumbnailPanel) imagesShown.get(i));
				imagesShown.remove(i);
				if (i < numShown)
					numShown--;
				return;
			}
		}
	}

	public void remove(AjaxProduct p) {
		for (int i = 0; i < imagesShown.size(); i++) {
			if (((ThumbnailPanel) imagesShown.get(i)).product.equals(p)) {
				table.remove((ThumbnailPanel) imagesShown.get(i));
				imagesShown.remove(i);
				if (i < numShown)
					numShown--;
				return;
			}
		}
	}

	public void add(AjaxProduct[] prods) {
		changeNumShown(imagesShown.size());
		for (int i = 0; i < prods.length; i++) {
			Widget w = createThumbnail(prods[i]);
			imagesShown.add(w);
			addWidget(w);
			numShown++;
		}
	}

	public void add(List prods) {
		changeNumShown(imagesShown.size());
		Iterator iter = prods.iterator();
		while (iter.hasNext()) {
			Widget w = createThumbnail((AjaxProduct) iter.next());
			imagesShown.add(w);
			addWidget(w);
			numShown++;
		}
	}

	public void redraw() {
		row = 0;
		col = 0;
		table.clear();
		remove(table);
		setWidth("0px");
		add(table);

		Iterator iter = imagesShown.iterator();
		int count = 0;
		while (iter.hasNext() && count < numShown) {
			Widget w = (Widget) iter.next();
			addWidget(w);
			count++;
		}
		setWidth((getWidth() - 40) + "px");
	}

	protected void addWidget(Widget w) {
		table.setWidget(row, col, w);
		table.getFlexCellFormatter().setHorizontalAlignment(row, col,
				HasHorizontalAlignment.ALIGN_LEFT);
		table.getFlexCellFormatter().setVerticalAlignment(row, col++,
				HasVerticalAlignment.ALIGN_BOTTOM);
		updateRowCol(w);
		setTheWidth();
	}

	public int size() {
		return imagesShown.size();
	}

	public void changeNumShown(int num) {
		if (num < numShown || num > numShown) {
			numShown = num;
			redraw();
		}
	}

	public void clear() {
		row = 0;
		col = 0;
		largest = 0;
		table.clear();
		imagesShown.clear();
		numShown = 0;
	}

	public boolean isCart() {
		return isCart;
	}

	public void setCart(boolean isCart) {
		this.isCart = isCart;
	}

}
