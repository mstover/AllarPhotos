package com.lazerinc.servlet.actionhandler.admin;

import java.util.LinkedList;
import java.util.List;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Rights;

public class GetLibraries extends ActionHandlerBase {

	public String getName() {
		return "get_libraries";
	}

	public void performAction(HandlerData info) throws ActionException {
		List<ProductFamily> families = new LinkedList<ProductFamily>();
		for (String fam : this.getController().getConfigValue("product_tables")
				.split(",")) {
			families.add(dbUtil.getProductFamily(fam.trim()));
		}
		info.setRequestBean("libraries", families);

	}

}
