package com.allarphoto.servlet.actionhandler.admin;

import java.util.LinkedList;
import java.util.List;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

public class GetProductFamilies extends ActionHandlerBase {

	public String getName() {
		return "get_families";
	}

	public void performAction(HandlerData info) throws ActionException {
		List<String> families = new LinkedList<String>();
		SecurityModel perms = getCurrentUserPerms(info);
		for (ProductFamily fam : getProductService().getProductFamilies()) {
			if (perms.getPermission(fam.getTableName(), Resource.DATATABLE,
					Right.ADMIN)) {
				families.add(fam.getTableName());
			}
		}
		info.setRequestBean("productFamilies", families);

	}

}
