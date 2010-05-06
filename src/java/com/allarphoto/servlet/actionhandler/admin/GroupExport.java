package com.lazerinc.servlet.actionhandler.admin;

import strategiclibrary.service.webaction.ActionException;
import strategiclibrary.service.webaction.HandlerData;
import strategiclibrary.service.webaction.ServletHandlerData;

import com.lazerinc.application.SecurityModel;
import com.lazerinc.client.beans.UserBean;
import com.lazerinc.ecommerce.UserGroup;
import com.lazerinc.servlet.actionhandler.ActionHandlerBase;

public class GroupExport extends ActionHandlerBase {

	public GroupExport() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return "group_export";
	}

	public void performAction(HandlerData info) throws ActionException {
		UserBean user = getUserBean(info);
		SecurityModel perms = user.getPermissions();
		UserGroup g = ugd.getGroup(info.getParameter("group_name", ""));
		info.setRequestBean("users", g.getUsers());
		((ServletHandlerData) info).getResponse().setContentType("text/csv");
	}

}
