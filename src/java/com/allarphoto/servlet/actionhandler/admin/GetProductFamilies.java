package com.lazerinc.servlet.actionhandler.admin;

import java.util.LinkedList;
import java.util.List;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

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
