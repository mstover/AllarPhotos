package com.allarphoto.servlet.actionhandler.admin;

import java.util.Collection;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.allarphoto.ecommerce.UserGroup;
import com.allarphoto.server.UserService;
import com.allarphoto.servlet.actionhandler.ActionHandlerBase;

public class GetMyGroups extends ActionHandlerBase {

	public GetMyGroups() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserService ugd = getUgd();
		Collection<UserGroup> groups = ugd
				.getGroups(getCurrentUserPerms(actionInfo));
		actionInfo.setRequestBean("groups", groups);

	}

	public String getName() {
		return "get_groups_for_admin";
	}

}
