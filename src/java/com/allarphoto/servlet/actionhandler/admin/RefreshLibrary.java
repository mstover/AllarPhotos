package com.allarphoto.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.client.exceptions.LazerwebException;
import com.allarphoto.ecommerce.ProductFamily;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;
import com.allarphoto.utils.Resource;
import com.allarphoto.utils.Right;

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
