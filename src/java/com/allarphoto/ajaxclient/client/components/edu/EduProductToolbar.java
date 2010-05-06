package com.lazerinc.ajaxclient.client.components.edu;

import com.lazerinc.ajaxclient.client.Services;
import com.lazerinc.ajaxclient.client.beans.AjaxProduct;
import com.lazerinc.ajaxclient.client.beans.AjaxResource;
import com.lazerinc.ajaxclient.client.components.ProductToolbar;
import com.lazerinc.ajaxclient.client.components.icons.EditIcon;
import com.lazerinc.ajaxclient.client.components.icons.MoveToActiveIcon;
import com.lazerinc.ajaxclient.client.components.icons.MoveToInactiveIcon;
import com.lazerinc.ajaxclient.client.components.icons.MoveToOfflineIcon;

public class EduProductToolbar extends ProductToolbar {

	public EduProductToolbar(AjaxProduct p) {
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
