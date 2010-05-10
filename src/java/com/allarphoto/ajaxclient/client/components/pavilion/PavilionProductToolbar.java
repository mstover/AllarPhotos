package com.allarphoto.ajaxclient.client.components.pavilion;

import java.util.Iterator;

import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxResource;
import com.allarphoto.ajaxclient.client.components.ProductToolbar;
import com.allarphoto.ajaxclient.client.components.icons.DownloadIcon;
import com.allarphoto.ajaxclient.client.components.icons.EditIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToActiveIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToObsoleteIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToOfflineIcon;

public class PavilionProductToolbar extends ProductToolbar {

	public PavilionProductToolbar(AjaxProduct p) {
		super(p);
	}

	protected void createOrderIcons() {
		if (product.isOrderable())
			add(new PavilionOrderIcon(product));
		Iterator iter = product.getDownloadableTypes().iterator();
		while (iter.hasNext()) {
			String[] download = (String[]) iter.next();
			add(new DownloadIcon(product, download[0], download[1]));
		}
	}

}
