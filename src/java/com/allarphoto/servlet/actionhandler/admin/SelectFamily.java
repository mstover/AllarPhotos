package com.allarphoto.servlet.actionhandler.admin;

import org.coinjema.util.Functor;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.util.DynamicArraySortWrapper;

import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class SelectFamily extends ActionHandlerBase {

	public SelectFamily() {
		super();
	}

	public String getName() {
		return "select_family";
	}

	public void performAction(HandlerData info) throws ActionException {
		if (info.hasParam("product_family", emptyStrings)) {
			ProductFamily family = dbUtil.getProductFamily(info
					.getParameter("product_family"));
			info.setUserBean("productFamily", family);
			info.setUserBean("fields", new DynamicArraySortWrapper(new Functor(
					family, "getFields")));
		}

	}

}
