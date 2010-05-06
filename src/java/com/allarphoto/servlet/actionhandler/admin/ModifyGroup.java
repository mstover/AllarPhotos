package com.lazerinc.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.server.UserService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class ModifyGroup extends ActionHandlerBase {

	public ModifyGroup() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserService ugd = getUgd();
		UserGroup old = ugd.getGroup(actionInfo.getParameterAsInt(
				"UserGroup.id", 0));
		if (old != null) {
			ugd.updateGroup(old.getName(), actionInfo
					.getParameter("UserGroup.name"),
					getCurrentUserPerms(actionInfo));
		}

	}

	public String getName() {
		return "modify_group";
	}

}
