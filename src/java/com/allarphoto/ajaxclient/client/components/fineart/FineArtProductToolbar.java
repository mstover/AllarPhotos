package com.lazerinc.ajaxclient.client.components.fineart;

import java.util.Iterator;

import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.AjaxResource;
import com.lazerinc.ajaxclient.client.components.ProductToolbar;
import com.lazerinc.ajaxclient.client.components.icons.DownloadIcon;
import com.lazerinc.ajaxclient.client.components.icons.EditIcon;
import com.lazerinc.ajaxclient.client.components.icons.MoveToActiveIcon;
import com.lazerinc.ajaxclient.client.components.icons.MoveToObsoleteIcon;
import com.lazerinc.ajaxclient.client.components.icons.MoveToOfflineIcon;

public class FineArtProductToolbar extends ProductToolbar {

	public FineArtProductToolbar(AjaxProduct p) {
		super(p);
	}

	protected void createOrderIcons() {
		if (product.isOrderable())
			add(new FAOrderIcon(product));
		Iterator iter = product.getDownloadableTypes().iterator();
		while (iter.hasNext()) {
			String[] download = (String[]) iter.next();
			add(new DownloadIcon(product, download[0], download[1]));
		}
	}

}
