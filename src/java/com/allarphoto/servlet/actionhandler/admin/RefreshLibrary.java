package com.lazerinc.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.ProductFamily;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;
import com.lazerinc.utils.Resource;
import com.lazerinc.utils.Right;

public class RefreshLibrary extends ActionHandlerBase {

	public String getName() {
		return "refresh_library";
	}

	public void performAction(HandlerData info) throws ActionException {
		String library = info.getParameter("family", "");
		ProductFamily family = dbUtil.getProductFamily(library);
		if (getCurrentUserPerms(info).getPermission(library,
				Resource.DATATABLE, Right.ADMIN))
			family.refresh();
		else
			throw new LazerwebException(LazerwebException.INVALID_PERMISSION);
	}

}
