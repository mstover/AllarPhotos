package com.lazerinc.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;

import com.lazerinc.beans.User;
import com.lazerinc.client.exceptions.InformationalException;
import com.lazerinc.client.exceptions.LazerwebException;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.server.UserService;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class DeleteGroups extends ActionHandlerBase {

	public DeleteGroups() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void performAction(HandlerData actionInfo) throws ActionException {
		UserService ugd = getUgd();
		int[] groupIds = actionInfo.getParameterValuesAsInt("group_id", 0);
		User u = getUserBean(actionInfo).getUser();
		for (int id : groupIds) {
			UserGroup group = ugd.getGroup(id);
			if (group.getUsers().contains(u)) {
				throw new InformationalException(
						LazerwebException.DELETE_OWN_GROUP);
			}
			ugd.deleteGroup(group.getName(), getCurrentUserPerms(actionInfo));
		}

	}

	public String getName() {
		return "delete_groups";
	}

}
