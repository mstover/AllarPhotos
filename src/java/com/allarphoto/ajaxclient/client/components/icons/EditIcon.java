package com.lazerinc.ajaxclient.client.components.icons;

import com.google.gwt.user.client.ui.ClickListener;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.components.ProductEditPopup;

public class EditIcon extends BaseIcon {

	public EditIcon() {
		addStyleName("button-icon");
		init();
	}

	public EditIcon(AjaxProduct p) {
		super(p);
	}

	protected ClickListener createClickListener() {
		return new ProductEditPopup(product);
	}

	public String getIconName() {
		return "edit.gif";
	}

	public String getIconUrl() {
		return "edit.gif";
	}

	public String getToolTip() {
		return "Edit Keywords";
	}

}
