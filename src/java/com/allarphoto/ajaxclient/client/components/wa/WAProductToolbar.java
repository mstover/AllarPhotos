package com.allarphoto.ajaxclient.client.components.wa;

import com.allarphoto.ajaxclient.client.Services;
import com.allarphoto.ajaxclient.client.beans.AjaxProduct;
import com.allarphoto.ajaxclient.client.beans.AjaxResource;
import com.allarphoto.ajaxclient.client.components.ProductToolbar;
import com.allarphoto.ajaxclient.client.components.icons.EditIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToActiveIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToInactiveIcon;
import com.allarphoto.ajaxclient.client.components.icons.MoveToOfflineIcon;

public class WAProductToolbar extends ProductToolbar {

	public WAProductToolbar(AjaxProduct p) {
		super(p);
		// TODO Auto-generated constructor stub
	}

	protected void createAdminIcons() {
		if (Services.getServices().perms.hasPermission(new AjaxResource(product
				.getFamilyName(), AjaxResource.DATATABLE), "admin")) {
			add(new EditIcon(product));
			if (product.getPath().indexOf("/Active/") > -1) {
				add(new MoveToInactiveIcon(product));
				add(new MoveToOfflineIcon(product));
			} else
				add(new MoveToActiveIcon(product));
		}
	}

}
