package com.allarphoto.servlet.actionhandler.search;

import org.coinjema.context.CoinjemaDynamic;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

public class LoadFamily extends AbstractSearchAction {

	public void performAction(HandlerData info) throws ActionException {
		String[] families = getFamilies();
		if (families != null) {
			for (String family : families)
				dbUtil.getProductFamily(family).getProducts(
						getCurrentUserPerms(info));
		}
	}

	public String getName() {
		return "load_families";
	}

	@CoinjemaDynamic(alias = "families")
	public String[] getFamilies() {
		return null;
	}

}
