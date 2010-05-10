package com.allarphoto.ajaxclient.client.components;

import java.util.Iterator;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxResource;
import com.allarphoto.ajaxclient.client.components.icons.DownloadIcon;
import com.allarphoto.ajaxclient.client.components.icons.EditIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToActiveIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToObsoleteIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToOfflineIcon;
import com.allarphoto.ajaxclient.client.components.icons.OrderIcon;

public class ProductToolbar extends HorizontalPanel {

	protected AjaxProduct product;

	public ProductToolbar(AjaxProduct p) {
		product = p;
		addStyleName("toolbar");
		init();
	}

	private void init() {
		createOrderIcons();
		createAdminIcons();
	}

	protected void createAdminIcons() {
		if (Services.getServices().perms.hasPermission(new AjaxResource(product
				.getFamilyName(), AjaxResource.DATATABLE), "admin")) {
			add(new EditIcon(product));
			if (product.getPath().indexOf("/Active/") > -1) {
				add(new MoveToObsoleteIcon(product));
				add(new MoveToOfflineIcon(product));
			} else
				add(new MoveToActiveIcon(product));
		}
	}

	protected void createOrderIcons() {
		if (product.isOrderable())
			add(new OrderIcon(product));
		Iterator iter = product.getDownloadableTypes().iterator();
		while (iter.hasNext()) {
			String[] download = (String[]) iter.next();
			add(new DownloadIcon(product, download[0], download[1]));
		}
	}

}
