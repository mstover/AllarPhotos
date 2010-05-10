package com.allarphoto.servlet.actionhandler.admin;

import java.util.LinkedList;
import java.util.List;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.application.SecurityModel;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Rights;

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
